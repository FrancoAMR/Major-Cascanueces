package com.majorcascanueces.psa.data.models;

import java.util.Objects;

public class GeoPoint {

    public String label;
    public double longitude;
    public double latitude;

    public GeoPoint(String label, double latitude, double longitude) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPoint geoPoint = (GeoPoint) o;
        return Objects.equals(label, geoPoint.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}