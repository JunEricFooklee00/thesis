package com.test.thesis_application.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.test.thesis_application.R;

import org.bson.Document;
import org.bson.types.ObjectId;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.options.UpdateOptions;

public class fragment_maps extends Fragment {
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;

    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    private String userid, employeeid;
    Double employeelongitude, employeelatitude, lat, longitude;
    String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("user_ID"); //coming from client home
            employeeid = projectuserid.getString("employeeID");
        }
        Log.v("MongoDB", "Employee ID: " + employeeid);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        client = LocationServices.getFusedLocationProviderClient(requireActivity());

        // all required for mongodb
        app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;


        Dexter.withContext(getContext())
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        loademployeelocation();
                        getmylocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


        return view;
    }

    public void loademployeelocation() {
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        ObjectId employeeId = new ObjectId(employeeid);
        // Create a filter using the objectId
        Document employee = new Document("_id", employeeId);
        mongoCollection.findOne(employee).getAsync(result1 -> {
            if (result1.isSuccess()) {
                Document resultdata = result1.get();
                Log.v("Mongodb", resultdata.toString());
                name = resultdata.getString("name");
                employeelongitude = resultdata.getDouble("longitude") != null ? resultdata.getDouble("longitude") : 0;
                employeelatitude = resultdata.getDouble("latitude") != null ? resultdata.getDouble("latitude") : 0;
                Log.v("Mongodb", employeelongitude.toString() + " " + employeelatitude.toString());
                lat = employeelatitude;
                longitude = employeelongitude;
            } else {
                Log.v("maps", "Error");
            }
        });
    }

    public void getmylocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("Users");
                mongoCollection = mongoDatabase.getCollection("clients");
                ObjectId objectId = new ObjectId(userid);
                // Create a filter using the objectId
                Document filter = new Document("_id", objectId);
                Log.v("MongoDB", "UserID: " + objectId);
                // Create a document with the location data
                Document update = new Document("$set", new Document("latitude", location.getLatitude()).append("longitude", location.getLongitude()));
                // Use the upsert option to insert a new document if the filter doesn't match any document
                UpdateOptions options = new UpdateOptions().upsert(true);

                // Update the document in the collection
                mongoCollection.updateOne(filter, update, options).getAsync(result -> {
                    if (result.isSuccess()) {
                        Log.v("MongoDB", "Document updated successfully!");
                    } else {
                        Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                    }
                });


                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if (employeelatitude != null && employeelongitude != null && employeelatitude != 0 && employeelongitude != 0) {
                            LatLng employeelatlng = new LatLng(employeelatitude, employeelongitude);
                            MarkerOptions employeemarker = new MarkerOptions().position(employeelatlng);
                            googleMap.addMarker(employeemarker);
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        googleMap.setMyLocationEnabled(true);
                    }
                });
            }
        });
    }
}
