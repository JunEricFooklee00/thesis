package com.test.thesis_application.employee;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.thesis_application.Jobinterface;
import com.test.thesis_application.JobsOrderClass;
import com.test.thesis_application.R;
import com.test.thesis_application.fragments.OrdersAdapter;

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

public class mytasks extends Fragment implements Jobinterface {
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
    private String userid, name, contactnumber;
    private ObjectId objectId;
    String workerIdToFind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mytasks, container, false);


        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("user_ID");
        }
//        objectId = new ObjectId(userid);

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("acceptedorders");
        workerIdToFind = userid; // replace this with the ID sent by your application
        Log.d("MyApp", "Starting to load job orders");


        orders.clear();
        loadJobsOrders(); // Call the loadJobsOrders() method passing the Realm instance
        loadJobsOrders2();
        loadJobsOrders3();
        loadJobsOrders4();
        loadJobsOrders5();
        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_orders);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        return view;
    }

    private void loadJobsOrders() {
        Document query1 = new Document("Worker1", workerIdToFind); // create an empty query
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(query1).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
//                Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();
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
            }

        });
    }
    private void loadJobsOrders2() {
        Document query2 = new Document("Worker2", workerIdToFind); // create an empty query
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(query2).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
//                Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();
                Log.v("EXAMPLE", results.toString());
                while (results.hasNext()) {
                    JobsOrderClass jobOrder = new JobsOrderClass();

                    Document document = results.next();

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
    private void loadJobsOrders3() {
        Document query3 = new Document("Worker3", workerIdToFind); // create an empty query
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(query3).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
//                Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();
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
    private void loadJobsOrders4() {
        Document query4 = new Document("Worker4", workerIdToFind); // create an empty query
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(query4).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
//                Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();
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
    private void loadJobsOrders5() {
        Document query5 = new Document("Worker5", workerIdToFind); // create an empty query
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(query5).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
//                Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemclick(int position) {

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        openedtask taskopened = new openedtask();

        // Create a Bundle to pass your data
        Bundle bundle = new Bundle();
        bundle.putString("Name", orders.get(position).getName());  // from joborderclass
        bundle.putDouble("contactNumber", orders.get(position).getContactNumber());
        bundle.putString("Unit", orders.get(position).getUnit());
        bundle.putString("scopeofwork", orders.get(position).getScopeofwork());
        bundle.putString("area", orders.get(position).getArea());
        bundle.putString("location", orders.get(position).getLocation());
        bundle.putString("startingdate", orders.get(position).getStartingDate());
        bundle.putString("_id", orders.get(position).get_id().toString());
        bundle.putString("jobtitle", orders.get(position).getJobTitle());
        bundle.putString("expectedfinishdate", orders.get(position).getExpectedFinishDate());
        bundle.putString("idUser", orders.get(position).getUserId());
        bundle.putString("employeeid",userid);
        taskopened.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container, taskopened);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//        Toast.makeText(requireContext(), orders.get(position).getJobTitle(), Toast.LENGTH_LONG).show();
    }
}