package com.majorcascanueces.psa.data.repository;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public interface SignInRepository {
    interface OnSignInListener {
        void onSignInComplete();
        void onSignInFailure(String cause);
    };
    FirebaseUser getCurrentUser();
    void signInWithEmailAndPassword(String email, String password, OnSignInListener listener);
    void signInWithLocalAccount(OnSignInListener listener);
    void signInWithGoogle(OnSignInListener listener);
    Intent initGoogleSignIn();
    boolean receiveGoogleSignIn(Intent data);
    void signOut();

}
