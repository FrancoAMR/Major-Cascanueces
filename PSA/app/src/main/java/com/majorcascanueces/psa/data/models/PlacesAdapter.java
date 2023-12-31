package com.majorcascanueces.psa.data.models;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majorcascanueces.psa.R;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<Place> placeList;

    public PlacesAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);

        // Configura los datos en el ViewHolder
        holder.placeImageView.setImageResource(place.getImageResId());
        holder.placeTitleTextView.setText(place.getName());
        holder.placeDescriptionTextView.setText(place.getDescription());
        // Configura otros datos aquí
    }



    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImageView;

        TextView placeTitleTextView;
        TextView placeDescriptionTextView;


        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImageView = itemView.findViewById(R.id.placeImageView);
            placeTitleTextView = itemView.findViewById(R.id.placeTitleTextView);
            placeDescriptionTextView = itemView.findViewById(R.id.placeDescriptionTextView);
            // Inicializa otros elementos aquí
        }
    }
}

