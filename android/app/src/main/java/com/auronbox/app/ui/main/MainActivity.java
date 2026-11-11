package com.auronbox.app.ui.main;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController nav;
    private AppBarConfiguration appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        nav = host.getNavController();
        appBar = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.cartFragment, R.id.ordersFragment, R.id.profileFragment).build();
        NavigationUI.setupActionBarWithNavController(this, nav, appBar);
        NavigationUI.setupWithNavController(binding.bottomNav, nav);

        AuronApp.sessao().quantidadeItens().observe(this, this::atualizarBadge);
    }

    private void atualizarBadge(Integer quantidade) {
        int n = quantidade == null ? 0 : quantidade;
        BadgeDrawable badge = binding.bottomNav.getOrCreateBadge(R.id.cartFragment);
        badge.setBackgroundColor(Color.BLACK);
        badge.setBadgeTextColor(Color.WHITE);
        badge.setNumber(n);
        badge.setVisible(n > 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(nav, appBar) || super.onSupportNavigateUp();
    }
}
