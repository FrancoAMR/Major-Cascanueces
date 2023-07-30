package com.majorcascanueces.psa.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.majorcascanueces.psa.databinding.ActivitySiginBinding;

public class SignInActivity extends AppCompatActivity {
    private ActivitySiginBinding binding;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initGoogleServices();
        initWidgetsActions();
    }

    private void initGoogleServices() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
    }

    private void initWidgetsActions() {
        binding.imageButtonGoogleSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
        //TODO entrys de normal sign y boton de incio
        //TODO intento de crear cuenta log in;
    }

    //google signIn
    private void googleSignIn() {
        Intent googleSignInInt = gsc.getSignInIntent();
        startActivityForResult(googleSignInInt,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToAppActivity();
                finish();
            } catch (ApiException e) {
                //PopUp
            }
        }
    }

    private void navigateToAppActivity() {
        Intent appIntent = new Intent(SignInActivity.this, AppActivity.class);
        startActivity(appIntent);
    }

    //Internal signIn
    private void signIn() {
        //TODO:
    }

}
