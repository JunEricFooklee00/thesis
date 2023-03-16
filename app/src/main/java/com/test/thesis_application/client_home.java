package com.test.thesis_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.test.thesis_application.fragments.fragment_Dashboard;
import com.test.thesis_application.fragments.fragment_maps;
import com.test.thesis_application.fragments.fragment_profile;
import com.test.thesis_application.fragments.fragment_project;

public class client_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    TextView navName,navUsername;
    String imagepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.menu_Open,R.string.menu_Close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new fragment_Dashboard()).commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }

        Intent username = getIntent(), name = getIntent(),user_type = getIntent(), user_avatar = getIntent();

        View headerView = navigationView.getHeaderView(0); //used for calling the navigation items
        navName = headerView.findViewById(R.id.user_name);
        navUsername = headerView.findViewById(R.id.user_username);
        ImageView nav_avatar = headerView.findViewById(R.id.user_profilePic);

        String usertype = user_type.getStringExtra("user_Type");
        navUsername.setText(username.getStringExtra("username"));
        navName.setText(name.getStringExtra("name"));
        imagepath = user_avatar.getStringExtra("avatar");



    }// end of onCreate

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_dashboard:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_Dashboard()).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_maps()).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
//                Intent GoogleMaps = new Intent(client_home.this,MapsActivity.class);
//                startActivity(GoogleMaps);

                break;
            case R.id.nav_myproject:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_project())
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_profile:

//                fragment_profile profile_fragment = fragment_profile.newInstance(navName.getText().toString(),navUsername.getText().toString());
//                Toast.makeText(getApplicationContext(),navName.getText().toString(),Toast.LENGTH_LONG).show();
                fragment_profile profile_fragment =  new fragment_profile();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle data = new Bundle();
                data.putString("username",navUsername.getText().toString());
                data.putString("name",navName.getText().toString());
                profile_fragment.setArguments(data);
                fragmentTransaction.replace(R.id.fragment_container,profile_fragment).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new fragment_profile()).commit();
                break;
            case R.id.nav_logout:
                Intent logout = new Intent(client_home.this,MainActivity.class);
                startActivity(logout);
                finish();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}