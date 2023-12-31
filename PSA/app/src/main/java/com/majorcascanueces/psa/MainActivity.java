package com.majorcascanueces.psa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.majorcascanueces.psa.di.AuthServices;
import com.majorcascanueces.psa.ui.activities.AppActivity;
import com.majorcascanueces.psa.ui.activities.AuthActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthServices as = new AuthServices(this);
        if (as.userAlreadyLogged()) {
            startActivity(new Intent(MainActivity.this, AppActivity.class));
        }
        else
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        finish();
    }
}