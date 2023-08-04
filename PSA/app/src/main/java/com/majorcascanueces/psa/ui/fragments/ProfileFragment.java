package com.majorcascanueces.psa.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.RSDriverException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.majorcascanueces.psa.databinding.FragmentPerfilBinding;
import com.majorcascanueces.psa.di.AuthServices;
import com.majorcascanueces.psa.ui.activities.AuthActivity;
import com.majorcascanueces.psa.ui.viewmodels.ProfileViewModel;


public class ProfileFragment extends Fragment {
    private TextView emailTextView, nameTextView;
    //private ImageView profileImageView;
    //Vincula lo del layout (widget) a tu actividad
    private FragmentPerfilBinding binding;
    private AuthServices as;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        as= new AuthServices(this.getContext());

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
         binding.button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 regresarAuthActivity();
             }
         });
        //final TextView textView = binding.textViewProfile;
        //profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mostrarPerfil();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void mostrarPerfil(){
        binding.emailTextView.setText(as.getCurrentUser().getEmail());
        binding.nameTextView.setText(as.getCurrentUser().getDisplayName());
    }

    public void regresarAuthActivity() {
        // Crear un Intent para la actividad AuthActivity
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        // Limpiar todas las actividades anteriores y hacer que AuthActivity sea la nueva raíz
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Iniciar la actividad AuthActivity
        startActivity(intent);
        as.signOut();
        // Finalizar la actividad actual para que no se pueda regresar a ella presionando el botón de retroceso
        ((Activity)getContext()).finish();
    }

}