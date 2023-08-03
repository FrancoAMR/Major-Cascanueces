package com.majorcascanueces.psa.data.repository;

public interface MapRepository {
    String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    int FLOOR_1 = 1;
    int FLOOR_2 = 2;
    int FLOOR_3 = 3;
    void setMapSettings();

    void saveInstanceState();

    void setMapSavedInstance();

    void setFloor(int to);

}
