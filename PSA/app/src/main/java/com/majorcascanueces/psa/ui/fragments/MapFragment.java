package com.majorcascanueces.psa.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
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

        //googleMap.clear();
        LatLngBounds imageBounds = new LatLngBounds(
                new LatLng(-75.5, -1),
                new LatLng(-75.0, 1)
        );

        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.piso1))
                .positionFromBounds(imageBounds);

        googleMap.addGroundOverlay(groundOverlayOptions);

        LatLngBounds quad = new LatLngBounds(
                new LatLng(-75.5, -0.5),
                new LatLng(-75.0, 0.5)
        );

        googleMap.setLatLngBoundsForCameraTarget(quad);

        float tilt = 45.0f;
        float bearing = 0f;
        LatLng location = new LatLng(-75.25,0);
        float zoomLevel = 5f;
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(location)
                        .zoom(zoomLevel)
                        .tilt(tilt)
                        .bearing(bearing)
                        .build()));

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(new LatLng(-75.5, -0.5))
                .add(new LatLng(-75.25, 0))
                .add(new LatLng(-75, 0.5))
                .width(10)
                .color(Color.RED);

        googleMap.addPolyline(polylineOptions);
    }
}