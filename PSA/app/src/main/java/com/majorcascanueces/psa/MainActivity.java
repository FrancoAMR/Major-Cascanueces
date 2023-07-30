package com.majorcascanueces.psa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.majorcascanueces.psa.ui.activities.AppActivity;
import com.majorcascanueces.psa.ui.activities.SignInActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(this);
        if (gsa != null)
            startActivity(new Intent(MainActivity.this, AppActivity.class));
        else
            startActivity(new Intent(MainActivity.this,SignInActivity.class));
        finish();
    }
}