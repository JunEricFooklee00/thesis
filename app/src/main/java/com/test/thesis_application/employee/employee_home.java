package com.test.thesis_application.employee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.test.thesis_application.fragments.calendarview;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class employee_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    TextView navName, navUsername;
    private String imagepath,str_email,str_birthday, str_address,str_UID,newstr_UID,usertype;
    String value;
    double str_contact,str_zipcode;
    ImageView nav_avatar;
    String Appid = "employeems-mcwma";
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
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
        Intent account = getIntent();
        str_UID = account.getStringExtra("user_ID");

        if(savedInstanceState == null){
            employeedashboard employeeDash =  new employeedashboard();
            FragmentTransaction empdashTransac = getSupportFragmentManager().beginTransaction();
            Bundle employeedashbundle = new Bundle();
            employeedashbundle.putString("user_ID",str_UID);
            employeeDash.setArguments(employeedashbundle);// to pass data
            empdashTransac.replace(R.id.fragment_container,employeeDash).setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");

        ObjectId objectId = new ObjectId(str_UID);
        Document filter = new Document("_id", objectId);

        mongoCollection.findOne(filter).getAsync(result -> {
            if (result.isSuccess()){
                Document resultdata = result.get();
                usertype = resultdata.getString("user");
                newstr_UID = resultdata.getObjectId("_id").toString();
                str_email = resultdata.getString("email");
                navUsername.setText(resultdata.getString("username"));
                navName.setText(resultdata.getString("name"));
                imagepath = resultdata.getString("avatar");

                Picasso.get()
                        .load(imagepath).transform(new CropCircleTransformation())
                        .fit()
                        .centerCrop()
                        .into(nav_avatar);
            }
            else {
                Log.v("ERRORTHESISEMS","ERROR");
            }

            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0);
            value = df.format(str_contact);

        });

    }//end of oncreate
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_dashboard:

                employeedashboard employeeDash =  new employeedashboard();
                FragmentTransaction empdashTransac = getSupportFragmentManager().beginTransaction();
                Bundle employeedashbundle = new Bundle();
                employeedashbundle.putString("user_ID",str_UID);
                employeeDash.setArguments(employeedashbundle);// to pass data
                empdashTransac.replace(R.id.fragment_container,employeeDash).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

                break;
            case R.id.Mycalendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new calendarview()).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.nav_mytask:
                mytasks mytaskss =  new mytasks();
                FragmentTransaction projectTransaction = getSupportFragmentManager().beginTransaction();
                Bundle projectuserid = new Bundle();
                projectuserid.putString("user_ID",str_UID);
                mytaskss.setArguments(projectuserid);// to pass data
                projectTransaction.replace(R.id.fragment_container,mytaskss).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.nav_profile:

                employee_profile profile_fragment =  new employee_profile();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle data = new Bundle();
                data.putString("uid",newstr_UID);
                profile_fragment.setArguments(data);
                fragmentTransaction.replace(R.id.fragment_container,profile_fragment).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

                break;
            case R.id.nav_logout:
                Intent logout = new Intent(employee_home.this,MainActivity.class);
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