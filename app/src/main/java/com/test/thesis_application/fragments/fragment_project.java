package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.thesis_application.Jobinterface;
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

public class fragment_project extends Fragment implements Jobinterface {
    //declare variables
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
     List<JobsOrderClass> orders = new ArrayList<>();

    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;

    FloatingActionButton insert;
    private String userid,name,contactnumber;
    private ObjectId objectId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_projects, container, false);

        insert = view.findViewById(R.id.insert_job);

        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("user_ID");
            name = projectuserid.getString("name");
            contactnumber = projectuserid.getString("contactnumber");
        }
        Log.v("fragmentproject",contactnumber);
//        objectId = new ObjectId(userid);

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("JobOrder");
        mongoCollection = mongoDatabase.getCollection("joborders");


        orders.clear();
        loadJobsOrders(); // Call the loadJobsOrders() method passing the Realm instance

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_orders);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));



        insert.setOnClickListener(v -> insertjobtransaction());


        return view;
    }


    private void loadJobsOrders() {
        Document filter = new Document("idUser", userid);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
               if (task.isSuccess()) {
                   MongoCursor<Document> results = task.get();
                   Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();
                   Log.v("EXAMPLE", results.toString());
                   while (results.hasNext()) {
                       JobsOrderClass jobOrder = new JobsOrderClass();

                       Document document = results.next();

//                       Log.v("RESULTSDATA",);
                        // Change things here if may binago sa database
                       jobOrder.set_id(document.getObjectId("_id"));
                       jobOrder.setUserId(document.getString("idUser"));
                       jobOrder.setName(document.getString("ClientName"));
                       jobOrder.setContactNumber(document.getDouble("ContactNumber"));
                       jobOrder.setScopeofwork(document.getString("TypeOfWork"));
                       jobOrder.setJobTitle(document.getString("ProjectName"));
                       jobOrder.setArea(document.getString("Area"));
                       jobOrder.setUnit(document.getString("Unit"));
                       jobOrder.setLocation(document.getString("Location"));
                       jobOrder.setStartingDate(document.getString("StartingDate"));
                       jobOrder.setExpectedFinishDate(document.getString("ExpectedFinishDate"));
                       orders.add(jobOrder);

                       // Set up adapter
                       adapter = new OrdersAdapter (orders,this);
//                       adapter.notifyItemInserted();
                       recyclerView.setAdapter(adapter);
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
        bundle.putString("name",name);
        bundle.putString("contactnumber",contactnumber);
        // Set the Bundle as an argument for your fragment
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(requireContext(),userid,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemclick(int position) {

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        jobsinfo fragmentjob = new jobsinfo();

        // Create a Bundle to pass your data
        Bundle bundle = new Bundle();
        bundle.putString("Name",orders.get(position).getName());  // from joborderclass
        bundle.putDouble("contactNumber",orders.get(position).getContactNumber());
        bundle.putString("Unit",orders.get(position).getUnit());
        bundle.putString("scopeofwork", orders.get(position).getScopeofwork());
        bundle.putString("area", orders.get(position).getArea());
        bundle.putString("location", orders.get(position).getLocation());
        bundle.putString("startingdate", orders.get(position).getStartingDate());
        bundle.putString("_id", orders.get(position).get_id().toString());
        bundle.putString("jobtitle", orders.get(position).getJobTitle());
        bundle.putString("expectedfinishdate", orders.get(position).getExpectedFinishDate());
        bundle.putString("idUser", orders.get(position).getUserId());

        fragmentjob.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container,fragmentjob);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(requireContext(),orders.get(position).getJobTitle(),Toast.LENGTH_LONG).show();
    }
}

