package com.majorcascanueces.psa.di;

import android.content.Context;

import com.majorcascanueces.psa.data.repository.AuthRepositoryImpl;

public class AuthServices extends AuthRepositoryImpl {
    public AuthServices(Context context) {
        super(context);
    }

    public boolean userAlreadyLogged() {
        return getCurrentUser() != null;
    }

}
