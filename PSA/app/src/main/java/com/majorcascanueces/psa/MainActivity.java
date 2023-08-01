package com.majorcascanueces.psa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.majorcascanueces.psa.di.SignInServices;
import com.majorcascanueces.psa.ui.activities.AppActivity;
import com.majorcascanueces.psa.ui.activities.SignInActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignInServices sis = new SignInServices(this);
        if (sis.userAlreadyLogged()) {
            startActivity(new Intent(MainActivity.this, AppActivity.class));
        }
        else
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }
}