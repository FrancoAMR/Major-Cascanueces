package com.majorcascanueces.psa.data.repository;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public interface AuthRepository {
    interface OnSignInListener {
        void onSignInComplete();
        void onSignInFailure(String cause);
    };
    interface OnLogInListener {
        void onLogInComplete();
        void onLogInFailure(String cause);
    }
    FirebaseUser getCurrentUser();
    void signInWithEmailAndPassword(String email, String password, OnSignInListener listener);
    void signInWithLocalAccount(OnSignInListener listener);
    void signInWithGoogle(OnSignInListener listener);
    Intent initGoogleSignIn();
    boolean receiveGoogleSignIn(Intent data);
    void logInWithEmailAndPassword(String email, String password, OnLogInListener listener);
    void signOut();

}
