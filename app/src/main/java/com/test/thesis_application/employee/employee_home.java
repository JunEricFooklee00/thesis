package com.test.thesis_application.employee;

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
import com.squareup.picasso.Picasso;
import com.test.thesis_application.MainActivity;
import com.test.thesis_application.R;
import com.test.thesis_application.fragments.fragment_Dashboard;
import com.test.thesis_application.fragments.fragment_maps;
import com.test.thesis_application.fragments.fragment_profile;
import com.test.thesis_application.fragments.fragment_project;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class employee_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    TextView navName,navUsername;
    private String imagepath,str_email,str_contact,str_birthday,str_address,str_zipcode;
    ImageView nav_avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.menu_Open,R.string.menu_Close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0); //used for calling the navigation items
        navName = headerView.findViewById(R.id.user_name);
        navUsername = headerView.findViewById(R.id.user_username);
        nav_avatar = headerView.findViewById(R.id.user_profilePic);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new fragment_Dashboard()).commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }

        Intent username = getIntent(), name = getIntent(),user_type = getIntent(), user_avatar = getIntent(), email=getIntent(),contact = getIntent(),birthday = getIntent()
                ,address = getIntent(),zipcode = getIntent();
//        home_screen.putExtra("email", resultData.getString("email"));
//        home_screen.putExtra("contactNumber", resultData.getString("contactNumber"));
//        home_screen.putExtra("age", resultData.getString("age"));
//        home_screen.putExtra("address", resultData.getString("address"));
//        home_screen.putExtra("zipcode", resultData.getString("zipcode"));


        str_email = email.getStringExtra("email");
        str_birthday = birthday.getStringExtra("age");
        str_contact = contact.getStringExtra("contact");
        str_zipcode = zipcode.getStringExtra("user_Type");
        str_address = address.getStringExtra("address");

//        String usertype = user_type.getStringExtra("user_Type");
        navUsername.setText(username.getStringExtra("username"));
        navName.setText(name.getStringExtra("name"));
        imagepath = user_avatar.getStringExtra("resume");
        Picasso.get().load(imagepath).resize(200,200).transform(new CropCircleTransformation()).into(nav_avatar);

    }

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
                data.putString("resume",imagepath);
                data.putString("email",str_email);
                data.putString("birthday",str_birthday);
                data.putString("contactNumber",str_contact);
                data.putString("zipcode",str_zipcode);
                data.putString("address",str_address);

                profile_fragment.setArguments(data);
                fragmentTransaction.replace(R.id.fragment_container,profile_fragment).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new fragment_profile()).commit();
                break;
            case R.id.nav_logout:
                Intent logout = new Intent(employee_home.this, MainActivity.class);
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