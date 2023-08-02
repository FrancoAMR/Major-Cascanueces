package com.majorcascanueces.psa.ui.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.majorcascanueces.psa.databinding.FragmentMapBinding;
import com.majorcascanueces.psa.ui.viewmodels.MapViewModel;
import com.majorcascanueces.psa.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private GoogleMap googleMap;
    private MapView mapView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //final TextView textView = binding.textViewMap;
        //mapViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void onResume() {
        super.onResume();
        if (mapView!=null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView!=null)
            mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView!=null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView!=null)
            mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this.getContext(), R.raw.map_style));

            if (!success) {
                Log.e(this.getTag(), "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(this.getTag(), "Can't find style. Error: ", e);
        }

        LatLngBounds imageBounds = new LatLngBounds(
                new LatLng(-12.053675, -77.0866083333), //SW
                new LatLng(-12.0524638889, -77.0843611111) //NE
        );

        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.piso1))
                .positionFromBounds(imageBounds);
        googleMap.addGroundOverlay(groundOverlayOptions);



        LatLngBounds bounds = new LatLngBounds(
                new LatLng(-12.053675, -77.0866083333), //SW
                new LatLng(-12.0524638889, -77.0843611111) //NE
        );

        googleMap.setLatLngBoundsForCameraTarget(bounds);
        googleMap.setMinZoomPreference(18.5f);
        float tilt = 45.0f;
        float bearing = 0f;
        LatLng location = new LatLng(-12.05306,-77.08545);
        float zoomLevel = 19.5f;

        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Max se la come"));

        googleMap.setBuildingsEnabled(false);

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(location)
                        .zoom(zoomLevel)
                        .tilt(tilt)
                        .bearing(bearing)
                        .build()));

        /*PolylineOptions polylineOptions = new PolylineOptions()
                .width(10)
                .add(new LatLng(-75.5, -0.5))
                .color(Color.RED)
                .add(new LatLng(-75.25, 0))
                .color(Color.BLUE)
                .add(new LatLng(-75, 0.5)
        );

        googleMap.addPolyline(polylineOptions);*/
        //PolygonOptions polygonOptions = new PolygonOptions().clickable(true);
    }
}