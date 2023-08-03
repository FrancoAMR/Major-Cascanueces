package com.majorcascanueces.psa.data.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.majorcascanueces.psa.R;

public class MapRepositoryImpl implements MapRepository{
    private final GoogleMap googleMap;
    private final Context context;
    private static final String cPref = "CameraPref";
    private static final String fPref = "FloorPref";
    private  int current_floor = FLOOR_1;

    public MapRepositoryImpl(@NonNull Context context,@NonNull GoogleMap map) {
        this.context = context;
        this.googleMap = map;
    }

    @Override
    public void setMapSettings() {
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                           context, R.raw.map_style));
            if (!success) {
                Log.e(this.getClass().toString(), "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(this.getClass().toString(), "Can't find style. Error: ", e);
        }
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(-12.053675, -77.0866083333), //SW
                new LatLng(-12.0524638889, -77.0843611111) //NE
        );
        googleMap.setLatLngBoundsForCameraTarget(bounds);
        googleMap.setMinZoomPreference(18.5f);
    }

    @Override
    public void saveInstanceState() {
        SharedPreferences cameraPref = context.getSharedPreferences(cPref,Context.MODE_PRIVATE);
        SharedPreferences.Editor cameraEditor = cameraPref.edit();
        CameraPosition camera = googleMap.getCameraPosition();
        cameraEditor.putFloat("tilt",camera.tilt);
        cameraEditor.putFloat("bearing",camera.bearing);
        cameraEditor.putFloat("posLat",(float)camera.target.latitude);
        cameraEditor.putFloat("posLon",(float)camera.target.longitude);
        cameraEditor.putFloat("zoom", camera.zoom);
        cameraEditor.apply();

        SharedPreferences floorPref = context.getSharedPreferences(fPref,Context.MODE_PRIVATE);
        SharedPreferences.Editor floorEditor = floorPref.edit();
        floorEditor.putInt("floor",current_floor);
        floorEditor.apply();
    }

    private int getFloorDrawable(int floor) {
        if (floor == FLOOR_1)
            return R.drawable.piso1;
        if (floor == FLOOR_2)
            return R.drawable.piso2;
        if (floor == FLOOR_3)
            return R.drawable.piso3;
        return R.drawable.piso1;
    }

    @Override
    public void setMapSavedInstance() {
        SharedPreferences cameraPref = context.getSharedPreferences(cPref, Context.MODE_PRIVATE);
        SharedPreferences floorPref = context.getSharedPreferences(fPref,Context.MODE_PRIVATE);

        LatLngBounds imageBounds = new LatLngBounds(
                new LatLng(-12.053675, -77.0866083333), //SW
                new LatLng(-12.0524638889, -77.0843611111) //NE
        );

        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(
                        getFloorDrawable(floorPref.getInt("floor",FLOOR_1))))
                .positionFromBounds(imageBounds);
        googleMap.addGroundOverlay(groundOverlayOptions);

        googleMap.setBuildingsEnabled(false);

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(cameraPref.getFloat("posLat",-12.05306f),
                                cameraPref.getFloat("posLon",-77.08545f)))
                        .zoom(cameraPref.getFloat("zoom",19.5f))
                        .tilt(cameraPref.getFloat("tilt",45.0f))
                        .bearing(cameraPref.getFloat("bearing",0f))
                        .build()));
    }

    @Override
    public void setFloor(int to) {
        if(current_floor == FLOOR_1)
            ShowBuildingFloorOne();
        if (current_floor == to)
            return;
        current_floor = to;
        googleMap.clear();
        saveInstanceState();
        setMapSavedInstance();

    }
    private void ShowBuildingFloorOne(){

        PolygonOptions aulasPrimerPiso = new PolygonOptions()
                .add(new LatLng(-12.0534875,-77.0856905))//Abajo Izquierda
                .add(new LatLng(-12.0534298,-77.0852658))//Abajo Derecha
                .add(new LatLng(-12.0533543,-77.0852734))//Arriba Derecha
                .add(new LatLng(-12.0534111,-77.0856998))//Arriba Izquierda
                .strokeWidth(0)
                .fillColor(0);
        Polygon polygon = googleMap.addPolygon(aulasPrimerPiso);
        polygon.setClickable(true);
        polygon.setTag("alpha");
        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                if(polygon.getTag().equals("alpha")){
                    Toast.makeText((Activity)context, "Aulas Primer Piso", Toast.LENGTH_SHORT).show();
                    polygon.setFillColor(Color.rgb(116, 155, 194));
                    polygon.setTag("beta");
                }else{
                    polygon.setFillColor(0);
                    polygon.setTag("alpha");
                }
            }
        });
    }

}
