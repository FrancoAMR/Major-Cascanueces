package com.majorcascanueces.psa.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.majorcascanueces.psa.R;

public class ReviewFragment extends Fragment {

    private LinearLayout reviewsLinearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review, container, false);

        reviewsLinearLayout = root.findViewById(R.id.reviewsLinearLayout);

        // Agrega reseñas de ejemplo
        addReview("Evento 1", "Descripción del evento 1", R.drawable.circle);
        addReview("Evento 2", "Descripción del evento 2", R.drawable.google_logo);

        return root;
    }

    private void addReview(String title, String description, int imageResId) {
        View reviewView = getLayoutInflater().inflate(R.layout.item_review, reviewsLinearLayout, false);
        ImageView reviewImageView = reviewView.findViewById(R.id.reviewImageView);
        TextView titleTextView = reviewView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = reviewView.findViewById(R.id.descriptionTextView);

        reviewImageView.setImageResource(imageResId);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        reviewsLinearLayout.addView(reviewView);
    }
}

