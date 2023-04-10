package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.thesis_application.JobsOrderClass;
import com.test.thesis_application.R;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class fragment_project extends Fragment  {
    //declare variables
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
     List<JobsOrderClass> orders = new ArrayList<JobsOrderClass>();
    JobsOrderClass jobOrder = new JobsOrderClass();

    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;

    FloatingActionButton insert;
    private String userid;
    private ObjectId objectId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_projects, container, false);
        insert = view.findViewById(R.id.insert_job);

        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("user_ID");
        }

        objectId = new ObjectId(userid);

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("JobOrder");
        mongoCollection = mongoDatabase.getCollection("joborders");

        loadJobsOrders(); // Call the loadJobsOrders() method passing the Realm instance

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up adapter
        adapter = new OrdersAdapter (orders);
        recyclerView.setAdapter(adapter);

        insert.setOnClickListener(v -> insertjobtransaction());


        return view;
    }

    private void loadJobsOrders() {
        List<JobsOrderClass> orders = new ArrayList<>();
        ObjectId objectId = new ObjectId(userid);
        // Create a filter using the objectId
        Document filter = new Document("userId", objectId);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
               if (task.isSuccess()) {
                   MongoCursor<Document> results = task.get();
                   Log.v("EXAMPLE", "success");
                   while (results.hasNext()) {
                       Document document = results.next();
                       jobOrder.set_id(document.getObjectId("_id"));
                       jobOrder.setUserId(document.getObjectId("userId"));
                       jobOrder.setScopeofwork(document.getString("scopeofwork"));
                       jobOrder.setJobTitle(document.getString("jobTitle"));
                       jobOrder.setArea(document.getString("Area"));
                       jobOrder.setLocation(document.getString("Location"));
                       jobOrder.setStartingDate(document.getString("StartingDate"));
                       jobOrder.setExpectedFinishDate(document.getString("ExpectedFinishDate"));

                       orders.add(jobOrder);
                       Log.v("EXAMPLE", results.next().toString());


                   }
               } else {
                   Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
               }

        });
    }

    private void insertjobtransaction() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        insertjob fragment = new insertjob();

        // Create a Bundle to pass your data
        Bundle bundle = new Bundle();
        bundle.putString("uid", userid); // Example of adding a String to the Bundle

        // Set the Bundle as an argument for your fragment
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(requireContext(),userid,Toast.LENGTH_LONG).show();
    }
}

