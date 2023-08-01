package com.majorcascanueces.psa.data.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.majorcascanueces.psa.R;


public class AuthRepositoryImpl implements AuthRepository {

    private final String localUserFilename = "LocalUser.txt";
    private final String emailUserFilename = "EmailUser.txt";
    public static final int googleRequestCode = 1000;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount gsa;
    private final FirebaseAuth mAuth;
    private Context context;

    public AuthRepositoryImpl(Context context) {
        this.context = context;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(context, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public void signInWithEmailAndPassword(String email, String password, AuthRepository.OnSignInListener listener) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task->{
            if (task.isSuccessful()) {
                listener.onSignInComplete();
            } else {
                String errorMessage = task.getException().getMessage();
                listener.onSignInFailure(errorMessage);
            }
        });
    }

    @Override
    public void signInWithLocalAccount(AuthRepository.OnSignInListener listener) {
        mAuth.signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onSignInComplete();
            } else {
                String errorMessage = task.getException().getMessage();
                listener.onSignInFailure(errorMessage);
            }
        });
    }

    @Override
    public void signInWithGoogle(AuthRepository.OnSignInListener listener) {
        if (gsa != null)
            mAuth.signInWithCredential(GoogleAuthProvider.getCredential(gsa.getIdToken(), null))
                .addOnCompleteListener(task -> {
                    listener.onSignInComplete();
                });
    }

    @Override
    public Intent initGoogleSignIn() {
        return gsc.getSignInIntent();
    }

    @Override
    public boolean receiveGoogleSignIn(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            gsa = task.getResult(ApiException.class);
            return true;
        } catch (ApiException e) {
            Log.w(getClass().toString(),"Google ApiException");
            return false;
        }
    }

    @Override
    public void logInWithEmailAndPassword(String email, String password, OnLogInListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onLogInComplete();
            } else {
                String errorMessage = task.getException().getMessage();
                listener.onLogInFailure(errorMessage);
            }
        });
    }

    @Override
    public void signOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            boolean isGoogleUser = user.getProviderData().stream()
                    .anyMatch(userInfo -> userInfo.getProviderId()
                            .equals(GoogleAuthProvider.PROVIDER_ID));
            if (isGoogleUser) {
                gsc.signOut();
            } else if (user.isAnonymous()) {
                user.delete();
            }
            mAuth.signOut();
        }
    }

}
