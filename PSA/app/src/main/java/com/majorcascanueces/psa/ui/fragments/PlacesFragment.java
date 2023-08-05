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
        samplePlaces.add(new Place(R.drawable.salones_100, "Salones 100", "Salones con capacidad promedio de 50 alumnos, aquí se dictan la teoría de los cursos."));
        System.out.println("");
        samplePlaces.add(new Place(R.drawable.salones_np_100, "Salones NP-100", "Nuevos salones con capacidad promedio de 50 personas, aquí se dictan la teoría de los cursos."));
        samplePlaces.add(new Place(R.drawable.laboratorio, "Laboratorio", "Lugar donde se encuentran las computadoras y se realizan las prácticas."));
        samplePlaces.add(new Place(R.drawable.patio, "Patio de la facultad", "Sitio al aire libre, ideal para comer o relajarse un poco."));
        samplePlaces.add(new Place(R.drawable.canchita, "Campo de fútbol", "Cancha de grass sintético para jugar fútbol."));
        samplePlaces.add(new Place(R.drawable.puerta_1, "Puerta 1", "Puerta principal de la FISI."));

        // Agrega más lugares aquí si es necesario

        return samplePlaces;
    }

}