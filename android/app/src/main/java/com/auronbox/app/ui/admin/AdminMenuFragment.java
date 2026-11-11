package com.auronbox.app.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.R;
import com.auronbox.app.databinding.FragmentAdminMenuBinding;

public class AdminMenuFragment extends Fragment {

    private FragmentAdminMenuBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.cardProdutos.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.adminProductsFragment));
        binding.cardPedidos.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.adminOrdersFragment));
        binding.cardUsuarios.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.adminUsersFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
