package com.example.fire_base_demo.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fire_base_demo.R;
import com.example.fire_base_demo.ui.Add_Product_Fragment;
import com.example.fire_base_demo.ui.Show_All_Product_Fragment;
import com.example.fire_base_demo.ui.View_Product_Fragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        appBarLayout = findViewById(R.id.appBar);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ProfileActivity.this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_add_product){
                    addFragment(new Add_Product_Fragment());
                }
                if (item.getItemId() == R.id.menu_view_product){
                    addFragment(new View_Product_Fragment());
                }
                if (item.getItemId() == R.id.menu_show_all_product){
                    addFragment(new Show_All_Product_Fragment());
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }
}