package com.majorcascanueces.psa.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.majorcascanueces.psa.data.repository.MapRepositoryImpl;

public class MapServices extends MapRepositoryImpl {

    public MapServices(@NonNull Context context, @NonNull GoogleMap map) {
        super(context, map);
    }
}
