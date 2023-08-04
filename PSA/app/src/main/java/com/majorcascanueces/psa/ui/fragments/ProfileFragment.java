package com.majorcascanueces.psa.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.majorcascanueces.psa.R;
import com.majorcascanueces.psa.databinding.FragmentPerfilBinding;
import com.majorcascanueces.psa.di.AuthServices;
import com.majorcascanueces.psa.task.LoadUrlImage;
import com.majorcascanueces.psa.ui.activities.AuthActivity;
import com.majorcascanueces.psa.ui.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private AuthServices as;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        as= new AuthServices(this.getContext());

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
         binding.buttonSignOut.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 regresarAuthActivity();
             }
         });
        mostrarPerfil();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void mostrarPerfil(){
        FirebaseUser user = as.getCurrentUser();
        if (user == null) return;

        String name = user.getDisplayName();
        if (name != null) binding.nameTextView.setText(name.isEmpty()? "Fisi 2023" : name);

        String em = user.getEmail();
        if (em != null)  binding.emailTextView.setText(em.isEmpty()? "Major_Cascanueces 2023": em);

        Uri url = user.getPhotoUrl();
        if (url != null)
            new LoadUrlImage(binding.profileImageView).execute(url.toString());
        else
            binding.profileImageView.setImageResource(R.drawable.user_default_photo);
    }

    public void regresarAuthActivity() {
        as.signOut();
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ((Activity)getContext()).finish();
    }

}