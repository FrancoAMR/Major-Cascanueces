package com.majorcascanueces.psa.data.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.majorcascanueces.psa.R;
import com.majorcascanueces.psa.data.models.GeoPoint;
import com.majorcascanueces.psa.data.models.GraphHelper;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Set;

public class MapRepositoryImpl implements MapRepository{
    private final GoogleMap googleMap;
    private final Context context;
    private static final String cPref = "CameraPref";
    private static final String fPref = "FloorPref";
    private int current_floor = FLOOR_1;
    private OnClickBuildingListener innerListener;
    private Graph<GeoPoint, DefaultWeightedEdge> floor1_graph;


    public MapRepositoryImpl(@NonNull Context context,@NonNull GoogleMap map) {
        this.context = context;
        this.googleMap = map;
        floor1_graph = GraphHelper.loadGraph(context.getResources().openRawResource(R.raw.floor1_graph));
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

    private void setObjects(int floor) {
        googleMap.clear();
        LatLngBounds imageBounds = new LatLngBounds(
                new LatLng(-12.053675, -77.0866083333), //SW
                new LatLng(-12.0524638889, -77.0843611111) //NE
        );
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(
                        getFloorDrawable(floor)))
                .positionFromBounds(imageBounds);
        googleMap.addGroundOverlay(groundOverlayOptions);

        if (floor == FLOOR_1) {
            showBuildingFloorOne();
            showMarkersFloorOne();
        }
    }

    @Override
    public void setMapSavedInstance() {
        SharedPreferences cameraPref = context.getSharedPreferences(cPref, Context.MODE_PRIVATE);
        SharedPreferences floorPref = context.getSharedPreferences(fPref,Context.MODE_PRIVATE);
        googleMap.setBuildingsEnabled(false);

        setObjects(floorPref.getInt("floor",FLOOR_1));

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
        if (current_floor == to)
            return;
        current_floor = to;
        setObjects(current_floor);
    }

    private void showBuildingFloorOne(){
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

    private void showMarkersFloorOne() {
        if (floor1_graph == null)
            return;
        for (GeoPoint gp : floor1_graph.vertexSet()) {
            if (gp.label.startsWith("p"))
                continue;
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(gp.latitude,gp.longitude))
                    .title(gp.label));
        }
    }

    public void doPath(String start, String end, OnPathListener listener) {
        if (start == null || end == null) {
            listener.onFail("Lamentablemente, no se logró realizar una búsqueda satisfactoria.");
            return;
        }
        if (start.equals(end)) {
            listener.onFail("Ya se encuentra en el lugar de destino");
            return;
        }
        if (!floor1_graph.vertexSet().contains(new GeoPoint(start)) ||
             !floor1_graph.vertexSet().contains(new GeoPoint(end))) {
            listener.onFail("Lamentablemente, no se encontraron lugares destacados con el nombre proporcionado.");
            return;
        }
        doPath(GraphHelper.geoPoint(floor1_graph.vertexSet(),start),
                GraphHelper.geoPoint(floor1_graph.vertexSet(),end));
    }

    private void doPath(GeoPoint start, GeoPoint end) {
        GraphPath<GeoPoint, DefaultWeightedEdge> path = GraphHelper.dijkstra(floor1_graph, start, end);
        PolylineOptions po = new PolylineOptions();
        for (GeoPoint gp : path.getVertexList()) {
            po.add(new LatLng(gp.latitude,gp.longitude));
        }
        po.width(8f)
        .color(Color.BLUE);
        googleMap.addPolyline(po);
    }

    public void setOnClickBuildingListener(OnClickBuildingListener listener) {
        innerListener = listener;
    }

    public interface OnClickBuildingListener {
        void onClick();
    }

    public interface OnPathListener {
        void successful();
        void onFail(String cause);
    }

}
