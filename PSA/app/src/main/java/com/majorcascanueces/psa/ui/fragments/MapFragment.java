package com.majorcascanueces.psa.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.majorcascanueces.psa.data.repository.MapRepository;
import com.majorcascanueces.psa.databinding.FragmentMapBinding;
import com.majorcascanueces.psa.di.MapServices;
import com.majorcascanueces.psa.ui.viewmodels.MapViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private FragmentMapBinding binding;
    private MapView mapView;
    private MapServices ms;

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
        initWidgetsActions();
        return root;
    }

    private void initWidgetsActions() {
        binding.buttonLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ms != null)
                    ms.setFloor(MapRepository.FLOOR_1);
            }
        });

        binding.buttonLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ms != null)
                    ms.setFloor(MapRepository.FLOOR_2);
            }
        });

        binding.buttonLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ms != null)
                    ms.setFloor(MapRepository.FLOOR_3);
            }
        });
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (ms != null)
            ms.saveInstanceState();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        ms = new MapServices(this.requireContext(),map);
        ms.setMapSettings();
        ms.setMapSavedInstance();
    }
}