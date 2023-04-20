package com.test.thesis_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.test.thesis_application.fragments.fragment_Dashboard;
import com.test.thesis_application.fragments.fragment_maps;
import com.test.thesis_application.fragments.fragment_profile;
import com.test.thesis_application.fragments.fragment_project;
import com.test.thesis_application.fragments.fragment_settings;
import com.test.thesis_application.fragments.weekView;

import org.bson.Document;
import org.bson.types.ObjectId;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class client_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    TextView navName, navUsername;
    private String imagepath,str_email,str_contact,str_birthday, str_address,str_zipcode,str_UID,newstr_UID;

    ImageView nav_avatar;
    String Appid = "employeems-mcwma";
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        Intent account = getIntent();
        str_UID = account.getStringExtra("user_ID");

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("clients");

        ObjectId objectId = new ObjectId(str_UID);
        Document filter = new Document("_id", objectId);

        mongoCollection.findOne(filter).getAsync(result -> {
            if (result.isSuccess()){
                Document resultdata = result.get();


                newstr_UID = resultdata.getObjectId("_id").toString();
                str_email = resultdata.getString("email");
                str_birthday = resultdata.getString("birthday");
//                str_contact = String.valueOf(resultdata.getDouble("contactNumber"));
//                contact = resultdata.getString("contactNumber").toString();
                str_contact = String.valueOf(resultdata.getDouble("contactNumber"));
                str_zipcode = resultdata.getString("zipcode");
                str_address = resultdata.getString("address");
                navUsername.setText(resultdata.getString("username"));
                navName.setText(resultdata.getString("name"));
                imagepath = resultdata.getString("resume");
//                str_contact = contact.toString();
                Picasso.get()
                        .load(imagepath).transform(new CropCircleTransformation())
                        .fit()
                        .centerCrop()
                        .into(nav_avatar);
            }
            else {
                Toast.makeText(getApplicationContext(),"Sorry Something is wrong with the application.",Toast.LENGTH_LONG).show();
            }

        });


    }// end of onCreate

    @SuppressLint("NonConstantResourceId")
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
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new fragment_maps()).setReorderingAllowed(true)
//                        .addToBackStack(null)
//                        .commit();
                fragment_maps fragmentMaps = new fragment_maps();
                FragmentTransaction mapsTransaction = getSupportFragmentManager().beginTransaction();
                //bundle is to pass data
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID",str_UID); // to fragment_maps()
                fragmentMaps.setArguments(mapsuid);
                mapsTransaction.replace(R.id.fragment_container,fragmentMaps).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.Mycalendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new weekView()).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_myproject:
                fragment_project project_fragment =  new fragment_project();
                FragmentTransaction projectTransaction = getSupportFragmentManager().beginTransaction();
                //bundle is to pass data

                Bundle projectuserid = new Bundle();
                projectuserid.putString("user_ID",str_UID);
                projectuserid.putString("name",navName.getText().toString());
                project_fragment.setArguments(projectuserid);// to pass data

                projectTransaction.replace(R.id.fragment_container,project_fragment).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new fragment_settings())
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
                data.putString("uid",newstr_UID);
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