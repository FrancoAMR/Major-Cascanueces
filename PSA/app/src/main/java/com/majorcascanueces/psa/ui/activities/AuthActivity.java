package com.majorcascanueces.psa.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.majorcascanueces.psa.data.repository.AuthRepository;
import com.majorcascanueces.psa.databinding.ActivityAuthBinding;
import com.majorcascanueces.psa.di.AuthServices;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private AuthServices as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        as = new AuthServices(this);
        initWidgetsActions();
    }

    private void initWidgetsActions() {
        binding.imageButtonGoogleSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = as.initGoogleSignIn();
                startActivityForResult(googleIntent, AuthServices.googleRequestCode);
            }
        });

        binding.btnSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    return;
                }
                as.signInWithEmailAndPassword(email,password,new AuthRepository.OnSignInListener() {
                    @Override
                    public void onSignInComplete() {
                        startActivity(new Intent(AuthActivity.this, AppActivity.class));
                        finish();
                    }
                    @Override
                    public void onSignInFailure(String cause) {
                        showEmailAuthFailure(cause);
                    }
                });
            }
        });

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    return;
                }
                as.logInWithEmailAndPassword(email, password, new AuthRepository.OnLogInListener() {
                    @Override
                    public void onLogInComplete() {
                        startActivity(new Intent(AuthActivity.this, AppActivity.class));
                        finish();
                    }

                    @Override
                    public void onLogInFailure(String cause) {
                        showEmailAuthFailure(cause);
                    }
                });
            }
        });

        binding.buttonLogInAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as.signInWithLocalAccount(new AuthRepository.OnSignInListener() {
                    @Override
                    public void onSignInComplete() {
                        startActivity(new Intent(AuthActivity.this, AppActivity.class));
                        finish();
                    }

                    @Override
                    public void onSignInFailure(String cause) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (AuthServices.googleRequestCode == requestCode) {
            if (as.receiveGoogleSignIn(data)) {
                as.signInWithGoogle(new AuthRepository.OnSignInListener() {
                    @Override
                    public void onSignInComplete() {
                        startActivity(new Intent(AuthActivity.this, AppActivity.class));
                        finish();
                    }
                    @Override
                    public void onSignInFailure(String cause) {
                        showGoogleSigInAlert();
                    }
                });
            }
            else {
                showGoogleSigInAlert();
            }
        }
    }

    private void showGoogleSigInAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage("No se pudo iniciar sesión con Google. Intente de nuevo más tarde.");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showEmailAuthFailure(String cause) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage(cause);
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
