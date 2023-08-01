package com.majorcascanueces.psa.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseUser;
import com.majorcascanueces.psa.data.repository.SignInRepository;
import com.majorcascanueces.psa.databinding.ActivitySiginBinding;
import com.majorcascanueces.psa.di.SignInServices;

public class SignInActivity extends AppCompatActivity {
    private ActivitySiginBinding binding;
    private SignInServices sis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sis = new SignInServices(this);
        initWidgetsActions();
    }

    private void initWidgetsActions() {
        binding.imageButtonGoogleSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = sis.initGoogleSignIn();
                startActivityForResult(googleIntent, SignInServices.googleRequestCode);
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
                sis.signInWithEmailAndPassword(email,password,new SignInRepository.OnSignInListener() {
                    @Override
                    public void onSignInComplete() {
                        startActivity(new Intent(SignInActivity.this, AppActivity.class));
                        finish();
                    }
                    @Override
                    public void onSignInFailure(String cause) {
                        showEmailSignInFailure(cause);
                    }
                });
            }
        });

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:franmento para resultado
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (SignInServices.googleRequestCode == requestCode) {
            if (sis.receiveGoogleSignIn(data)) {
                sis.signInWithGoogle(new SignInRepository.OnSignInListener() {
                    @Override
                    public void onSignInComplete() {
                        startActivity(new Intent(SignInActivity.this, AppActivity.class));
                        finish();
                    }
                    @Override
                    public void onSignInFailure(String cause) {

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
    private void showEmailSignInFailure(String cause) {
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
