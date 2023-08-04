package com.majorcascanueces.psa.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majorcascanueces.psa.databinding.FragmentPlacesBinding;
import com.majorcascanueces.psa.ui.viewmodels.PlacesViewModel;

import com.majorcascanueces.psa.R;
import com.majorcascanueces.psa.data.models.Place;
import com.majorcascanueces.psa.data.models.PlacesAdapter;
import com.majorcascanueces.psa.ui.viewmodels.PlacesViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    private FragmentPlacesBinding binding;

    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_places, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        List<Place> placeList = generateSamplePlaces();
        placesAdapter = new PlacesAdapter(placeList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placesAdapter);

        return root;
    }

    private List<Place> generateSamplePlaces() {
        List<Place> samplePlaces = new ArrayList<>();
        samplePlaces.add(new Place(R.drawable.salones_100, "Salones 100", "Descripción de los Salones 100"));
        System.out.println("");
        samplePlaces.add(new Place(R.drawable.salones_np_100, "Salones NP-100", "Descripción de los Salones NP-100"));
        samplePlaces.add(new Place(R.drawable.laboratorio, "Laboratorio", "Descripción del Laboratorio"));
        samplePlaces.add(new Place(R.drawable.patio, "Patio de la facultad", "Descripción del Patio de la facultad"));
        samplePlaces.add(new Place(R.drawable.canchita, "Canchita", "Descripción de la Canchita"));
        samplePlaces.add(new Place(R.drawable.puerta_1, "Puerta 1", "Descripción de la Puerta 1"));

        // Agrega más lugares aquí si es necesario

        return samplePlaces;
    }

}