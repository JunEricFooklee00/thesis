package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.test.thesis_application.Client_history;
import com.test.thesis_application.R;
import com.test.thesis_application.employee.EmployeeHistoryJobs;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class fragment_Dashboard extends Fragment {
    ImageView details;
    ImageView history;
    ImageView pending;
    ImageView terms;
    ImageView members;
    ImageView contributors;
    ImageView informative;
    String userid;

    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    CardView jobhistory,jobqueue,validatedJobs;
    TextView validatedJobsTitle,numberofvalidatedText,numberofvalidatedholder;
    TextView jobqueutitle,numberofqueueText,numberofqueueholder;
    TextView jobhistoryTitle,numberofcompletedText,numberofcompletedholder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,container,false);


        terms = view.findViewById(R.id.terms);
        members = view.findViewById(R.id.members);
        contributors = view.findViewById(R.id.contributors);
        informative = view.findViewById(R.id.informative);

        validatedJobs = view.findViewById(R.id.validatedJobs);
        jobhistoryTitle = view.findViewById(R.id.jobhistoryTitle);
        numberofcompletedText = view.findViewById(R.id.numberofcompletedText);
        numberofcompletedholder = view.findViewById(R.id.numberofcompletedholder);

        jobhistory = view.findViewById(R.id.jobhistory);
        validatedJobsTitle = view.findViewById(R.id.validatedJobsTitle);
        numberofvalidatedText = view.findViewById(R.id.numberofvalidatedText);
        numberofvalidatedholder = view.findViewById(R.id.numberofvalidatedholder);

        jobqueue = view.findViewById(R.id.jobqueue);
        jobqueutitle = view.findViewById(R.id.jobqueutitle);
        numberofqueueText = view.findViewById(R.id.numberofqueueText);
        numberofqueueholder = view.findViewById(R.id.numberofqueueholder);


        Bundle clientbundle = getArguments();
        if (clientbundle != null) {
            userid = clientbundle.getString("user_ID");
        }
        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("clients");

        loadjobhistory();
        loadjobqueue();
        loadjobvalidated();
        validatedJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        jobhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client_history employeeHistoryJobs =  new Client_history();
                FragmentTransaction empdashTransac = getParentFragmentManager().beginTransaction();
                Bundle ehj = new Bundle();
                ehj.putString("user_ID",userid);
                employeeHistoryJobs.setArguments(ehj);// to pass data
                empdashTransac.replace(R.id.fragment_container,employeeHistoryJobs).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        terms.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            terms fragment = new terms();

            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        informative.setOnClickListener(view13 -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            informative fragment = new informative();

            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        contributors.setOnClickListener(view12 -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            contributors fragment = new contributors();

            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                members fragment = new members();

                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
    public void loadjobhistory(){
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("jobhistories");
        Document filter = new Document("idUser", userid);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                int size = 0;
                while (results.hasNext()) {
                    size++;
                    results.next();
                }
                numberofcompletedholder.setText(String.valueOf(size));
                Log.v("MongoDB", "Results size: " + size);
            } else {
                Log.e("MongoDB", "failed to find documents with: ", task.getError());
            }
        });
    }
    public void loadjobqueue(){
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("joborders");
        Document filter = new Document("idUser", userid);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                int size = 0;
                while (results.hasNext()) {
                    size++;
                    results.next();
                }
                numberofqueueholder.setText(String.valueOf(size));
                Log.v("MongoDB", "ID of user is "+userid+" Results size: " + size);
            } else {
                Log.e("MongoDB", "failed to find documents with: ", task.getError());
            }
        });
    }
    public void loadjobvalidated(){
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("acceptedorders");
        Document filter = new Document("idUser", userid);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                int size = 0;
                while (results.hasNext()) {
                    size++;
                    results.next();
                }
                numberofvalidatedholder.setText(String.valueOf(size));
                Log.v("MongoDB", "Results size: " + size);
            } else {
                Log.e("MongoDB", "failed to find documents with: ", task.getError());
            }
        });
    }
}
