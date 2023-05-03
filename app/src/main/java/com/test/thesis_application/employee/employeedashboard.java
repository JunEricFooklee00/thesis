package com.test.thesis_application.employee;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.test.thesis_application.R;
import com.test.thesis_application.fragments.contributors;
import com.test.thesis_application.fragments.informative;
import com.test.thesis_application.fragments.members;
import com.test.thesis_application.fragments.terms;

import org.bson.Document;

import java.util.Arrays;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class employeedashboard extends Fragment {

    ImageView members1, contributors1, informative1, terms1, History;
    String userid;
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    CardView jobhistory, jobqueue, validatedJobs;
    TextView validatedJobsTitle, numberofvalidatedText, numberofvalidatedholder;
    TextView jobqueutitle, numberofqueueText, numberofqueueholder;
    TextView jobhistoryTitle, numberofcompletedText, numberofcompletedholder;
    public int historyval1, historyval2, historyval3, historyval4, historyval5, historyvalsum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employeedashboard, container, false);
        members1 = view.findViewById(R.id.members);
        terms1 = view.findViewById(R.id.terms);
        contributors1 = view.findViewById(R.id.contributors);
        informative1 = view.findViewById(R.id.informative);
        History = view.findViewById(R.id.History);

        numberofcompletedholder = view.findViewById(R.id.numberofcompletedholder);
        numberofvalidatedholder = view.findViewById(R.id.numberofvalidatedholder);
        numberofqueueholder = view.findViewById(R.id.numberofqueueholder);

// all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("clients");
        Bundle employeedashbundle = getArguments();
        if (employeedashbundle != null) {
            userid = employeedashbundle.getString("user_ID");
        }


//        loadworkerhistory1();

        loadAllWorkerHistory();
        loadAllWorkerjobs();


//        int sum = historyval1 + historyval2 + historyval3 + historyval4 + historyval5;
//        Log.v("Mongodb", "Value :"+historyval1 + historyval2 + historyval3 + historyval4 + historyval5 );
////        numberofcompletedholder.setText(sum);
////        numberofcompletedholder.setText(String.valueOf(historyval1+historyval2));


        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeHistoryJobs employeeHistoryJobs = new EmployeeHistoryJobs();
                FragmentTransaction empdashTransac = getParentFragmentManager().beginTransaction();
                Bundle ehj = new Bundle();
                ehj.putString("user_ID", userid);
                employeeHistoryJobs.setArguments(ehj);// to pass data
                empdashTransac.replace(R.id.fragment_container, employeeHistoryJobs).setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        terms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                terms fragment = new terms();

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        informative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                informative fragment = new informative();

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        contributors1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                contributors fragment = new contributors();

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        members1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                members fragment = new members();

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

//

    public void loadAllWorkerHistory() {
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("jobhistories");
        Document filter = new Document("$or", Arrays.asList(
                new Document("Worker1", userid),
                new Document("Worker2", userid),
                new Document("Worker3", userid),
                new Document("Worker4", userid),
                new Document("Worker5", userid)));
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                int totalHistory = 0;
                while (results.hasNext()) {
                    results.next();
                    totalHistory++;

                }
                Log.v("MongoDB", "Total job histories for worker " + userid + ": " + totalHistory);
                String sum = String.valueOf(totalHistory);
                numberofcompletedholder.setText(" "+sum);


            } else {
                Log.e("MongoDB", "failed to find documents with: ", task.getError());
            }
        });
    }

    public void loadAllWorkerjobs() {
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
        mongoCollection = mongoDatabase.getCollection("acceptedorders");
        Document filter = new Document("$or", Arrays.asList(
                new Document("Worker1", userid),
                new Document("Worker2", userid),
                new Document("Worker3", userid),
                new Document("Worker4", userid),
                new Document("Worker5", userid)));
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filter).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                int totalHistory = 0;
                while (results.hasNext()) {
                    results.next();
                    totalHistory++;

                }
                Log.v("MongoDB", "Total job validated for worker " + userid + ": " + totalHistory);
                String sum = String.valueOf(totalHistory);
                numberofvalidatedholder.setText(" "+sum);


            } else {
                Log.e("MongoDB", "failed to find documents with: ", task.getError());
            }
        });
    }

    public void loadworkerhistory3() {

    }

    public void loadworkerhistory4() {

    }

    public void loadworkerhistory5() {

    }
}