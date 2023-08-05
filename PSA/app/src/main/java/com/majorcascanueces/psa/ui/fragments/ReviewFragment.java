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
        addReview("Patio de la facultad", "Uno de los lugares más inolvidables de la FISI." +
                        "Este sitio alberga los recuerdos de las ginkanas y las fiestas de fin de ciclo." +
                        "También es el patio de juegos de nuestro gato favorito Tesla, el cual esta absolutamnete " +
                        "prohibido darle de comer.", R.drawable.tesla);
        addReview("Campo de fútbol", "Sitio ideal para los amantes del fútbol. Se guarda númerosos pichangas, torneos " +
                "y su respectiva rehidratación al final del campeonato.", R.drawable.futbol);
        addReview("Puerta 1", "El sitio que para muchos pasa desapercibido, pero que realmente es el más importante"
                +" Aquí es el primer vistazo a nuestra escuela, el primer paso hacia un futuro prometedor y"  +
                " una última foto con increíbles recuerdos.", R.drawable.entrada1);

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

