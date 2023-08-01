package com.majorcascanueces.psa.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.majorcascanueces.psa.R;
import com.majorcascanueces.psa.data.models.User;
import com.majorcascanueces.psa.data.repository.SignInRepository;
import com.majorcascanueces.psa.databinding.ActivityAppBinding;
import com.majorcascanueces.psa.di.SignInServices;
import com.majorcascanueces.psa.task.LoadUrlImage;

public class AppActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAppBinding binding;
    private SignInServices sis;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sis = new SignInServices(this);
        initWidgetsActions();
        checkSignInStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initWidgetsActions() {
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saliendo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sis.signOut();
                startActivity(new Intent(AppActivity.this, SignInActivity.class));
                finish();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void checkSignInStatus() {
        if (!sis.userAlreadyLogged()) {
            startActivity(new Intent(AppActivity.this, SignInActivity.class));
            finish();
        }
        else {
            initUserView();
        }
    }

    private void initUserView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView username = hView.findViewById(R.id.textViewUsername);
        TextView email = hView.findViewById(R.id.textViewAddress);
        ImageView photo = hView.findViewById(R.id.imageViewUser);
        user = sis.getCurrentUser();
        username.setText(user.getDisplayName());
        email.setText(user.getEmail());
        if (user.getPhotoUrl() != null)
            new LoadUrlImage(photo).execute(user.getPhotoUrl().toString());
        else
            photo.setImageResource(R.mipmap.ic_launcher_round);
    }

}
