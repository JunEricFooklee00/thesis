package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.thesis_application.Jobinterface;
import com.test.thesis_application.JobsOrderClass;
import com.test.thesis_application.R;

import org.bson.Document;

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

public class fragment_currentJob extends Fragment implements Jobinterface {
    String userid,name;
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    List<JobsOrderClass> orders = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_job, container, false);
        Bundle currentbundle = getArguments();
        if (currentbundle != null) {
            userid = currentbundle.getString("user_ID");
            name = currentbundle.getString("name");
        }
        Log.v("currentjob",userid);
        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("acceptedorders");

        orders.clear();
        loadJobsOrders(); // Call the loadJobsOrders() method passing the Realm instance

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.Jobslist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }
    private void loadJobsOrders() {
        Document filter = new Document("idUser", userid);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Toast.makeText(requireContext(),results.toString(),Toast.LENGTH_LONG).show();

                while (results.hasNext()) {
                    JobsOrderClass jobOrder = new JobsOrderClass();

                    Document document = results.next();

                    Log.v("EXAMPLE", "ClientName: " + document.getString("ClientName"));
                    Log.v("EXAMPLE", "TypeOfWork: " + document.getString("TypeOfWork"));
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
                    jobOrder.setWorker1(document.containsKey("Worker1") ? document.getString("Worker1") : null);
                    jobOrder.setWorker2(document.containsKey("Worker2") ? document.getString("Worker2") : null);
                    jobOrder.setWorker2(document.containsKey("Worker3") ? document.getString("Worker3") : null);
                    jobOrder.setWorker2(document.containsKey("Worker4") ? document.getString("Worker4") : null);
                    jobOrder.setWorker2(document.containsKey("Worker5") ? document.getString("Worker5") : null);
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

        OngoinJobs fragmentjob = new OngoinJobs(); // open pending job

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