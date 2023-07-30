package com.majorcascanueces.psa.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

import com.majorcascanueces.psa.R;
import com.majorcascanueces.psa.databinding.ActivityAppBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAppBinding binding;
    private GoogleSignInClient gsc;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount gsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWidgetsActions();
        //TODO:Primero ver si el user inicio con google o con sign in interno
        initGoogleService();
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
                googleSignOut();
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

    private void initGoogleService() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        gsa = GoogleSignIn.getLastSignedInAccount(this);
        if (gsa == null) {
            startActivity(new Intent(AppActivity.this, SignInActivity.class));
            finish();
        }
        else {
            initGoogleUser();
        }
    }

    private void initGoogleUser() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView username = hView.findViewById(R.id.textViewUsername);
        TextView email = hView.findViewById(R.id.textViewAddress);
        ImageView user = hView.findViewById(R.id.imageViewUser);
        username.setText(gsa.getDisplayName());
        email.setText(gsa.getEmail());

        new LoadProfileImage(user).execute(gsa.getPhotoUrl().toString());
    }

    private void googleSignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(AppActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadProfileImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                // Si la imagen no se pudo cargar, puedes establecer una imagen de respaldo o dejar el ImageView vacío.
                imageView.setImageResource(R.mipmap.ic_launcher_round);
            }
        }
    }
}
