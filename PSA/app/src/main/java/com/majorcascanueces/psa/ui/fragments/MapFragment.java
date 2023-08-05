package com.majorcascanueces.psa.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.majorcascanueces.psa.R;
import com.majorcascanueces.psa.data.models.GraphHelper;
import com.majorcascanueces.psa.data.repository.MapRepository;
import com.majorcascanueces.psa.data.repository.MapRepositoryImpl;
import com.majorcascanueces.psa.databinding.FragmentMapBinding;
import com.majorcascanueces.psa.di.MapServices;
import com.majorcascanueces.psa.ui.viewmodels.MapViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private FragmentMapBinding binding;
    private MapView mapView;
    private MapServices ms;
    private AutoCompleteTextView aStart;
    private AutoCompleteTextView aEnd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        aEnd = binding.autoCompleteTextViewEnd;
        aStart = binding.autoCompleteTextViewStart;
        mapView = binding.mapView;

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

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

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ms != null)
                    ms.doPath(aStart.getText().toString(),aEnd.getText().toString(), new MapRepositoryImpl.OnPathListener() {
                        @Override
                        public void successful() {

                        }
                        @Override
                        public void onFail(String cause) {
                            Toast.makeText(getContext(),cause,Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
        Transition transition = new Fade();

        TransitionManager.beginDelayedTransition(binding.getRoot(), transition);
        binding.searchViewContainer.setVisibility(
                binding.toggleButton.isChecked()? View.VISIBLE:View.GONE
        );

        binding.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.toggleButton.isChecked()) {
                    // Aplicamos la transición Fade para mostrar el searchViewContainer
                    TransitionManager.beginDelayedTransition(binding.getRoot(), transition);
                    binding.searchViewContainer.setVisibility(View.VISIBLE);
                } else {
                    // Aplicamos la transición Fade para ocultar el searchViewContainer
                    TransitionManager.beginDelayedTransition(binding.getRoot(), transition);
                    binding.searchViewContainer.setVisibility(View.GONE);
                }
            }
        });

        String[] optList = GraphHelper.getOptions(getResources().openRawResource(R.raw.graph_options));
        if(optList != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line,optList);
            aEnd.setAdapter(adapter);
            aStart.setAdapter(adapter);
        }
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
        if (mapView != null)
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
    }
}