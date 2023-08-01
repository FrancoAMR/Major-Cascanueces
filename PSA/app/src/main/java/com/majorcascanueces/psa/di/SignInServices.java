package com.majorcascanueces.psa.di;

import android.content.Context;

import com.majorcascanueces.psa.data.repository.SignInRepositoryImpl;

public class SignInServices extends SignInRepositoryImpl {
    public SignInServices(Context context) {
        super(context);
    }

    public boolean userAlreadyLogged() {
        return getCurrentUser() != null;
    }

}
