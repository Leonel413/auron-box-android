package com.auronbox.app.ui.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private NavController nav;
    private AppBarConfiguration appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!AuronApp.sessao().estaLogado() || !AuronApp.sessao().admin()) {
            finish();
            return;
        }
        ActivityAdminBinding binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_admin);
        nav = host.getNavController();
        appBar = new AppBarConfiguration.Builder(R.id.adminMenuFragment).build();
        NavigationUI.setupActionBarWithNavController(this, nav, appBar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(nav, appBar) || super.onSupportNavigateUp();
    }
}
