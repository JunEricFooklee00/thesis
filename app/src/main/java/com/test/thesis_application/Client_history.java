package com.test.thesis_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.thesis_application.employee.openedtask;
import com.test.thesis_application.fragments.OngoinJobs;
import com.test.thesis_application.fragments.OrdersAdapter;
import com.test.thesis_application.fragments.openhistoryclient;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;
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


public class Client_history extends Fragment implements Jobinterface  {

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
        View view = inflater.inflate(R.layout.fragment_client_history, container, false);



        Bundle ejh = getArguments();
        if (ejh != null) {
            userid = ejh.getString("user_ID");
        }
        Log.v("historyjobs",userid);
//        objectId = new ObjectId(userid);

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("jobhistories");
        workerIdToFind = userid; // replace this with the ID sent by your application
        Log.d("MyApp", "Starting to load job orders");


        orders.clear();
        loadJobsOrders(); // Call the loadJobsOrders() method passing the Realm instance
//        loadJobsOrders2();
//        loadJobsOrders3();
//        loadJobsOrders4();
//        loadJobsOrders5();
        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_orders);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }

    private void loadJobsOrders() {
        Document query1 = new Document("idUser", workerIdToFind); // create an empty query
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(query1).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Toast.makeText(requireContext(), results.toString(), Toast.LENGTH_LONG).show();
                Log.v("EXAMPLE", results.toString());
                while (results.hasNext()) {
                    JobsOrderClass jobOrder = new JobsOrderClass();
                    double contact1 = 0;

                    Document document = results.next();
                    Object contactNumberObj = document.get("ContactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact1 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contact1 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);

//                  Log.v("RESULTSDATA",);
                    // Change things here if may binago sa database
                    jobOrder.set_id(document.getObjectId("_id"));
                    jobOrder.setUserId(document.getString("idUser"));
                    jobOrder.setName(document.getString("ClientName"));
                    jobOrder.setContactNumber(contact1);
                    jobOrder.setScopeofwork(document.getString("TypeOfWork"));
                    jobOrder.setJobTitle(document.getString("ProjectName"));
                    jobOrder.setArea(document.getString("Area"));
                    jobOrder.setUnit(document.getString("Unit"));
                    jobOrder.setLocation(document.getString("Location"));
                    jobOrder.setStartingDate(document.getString("StartingDate"));
                    jobOrder.setExpectedFinishDate(document.getString("ExpectedFinishDate"));
                    orders.add(jobOrder);

                    // Set up adapter
                    adapter = new OrdersAdapter(orders, this);
//                       adapter.notifyItemInserted();
                    recyclerView.setAdapter(adapter);
                }

            } else {
            }

        });
    }

    @Override
    public void onItemclick(int position) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        openhistoryclient fragmentjob = new openhistoryclient(); // open pending job

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
        bundle.putString("Worker1", orders.get(position).getWorker1() != null ? orders.get(position).getWorker1() : "");
        bundle.putString("Worker2", orders.get(position).getWorker2() != null ? orders.get(position).getWorker2() : "");
        bundle.putString("Worker3", orders.get(position).getWorker3() != null ? orders.get(position).getWorker3() : "");
        bundle.putString("Worker4", orders.get(position).getWorker4() != null ? orders.get(position).getWorker4() : "");
        bundle.putString("Worker5", orders.get(position).getWorker5() != null ? orders.get(position).getWorker5() : "");

        fragmentjob.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container,fragmentjob);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}