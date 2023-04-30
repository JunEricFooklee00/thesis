package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.test.thesis_application.R;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class PendingJobs extends Fragment {

    String userId, id, nameuser, Unitstr, ScopeofWork, area, Location, ExpectedFinishDate, jobtitle, Startingdate, Worker1, Worker2, Worker3, Worker4, Worker5;
    Double contactNumber;
    TextView tv_jobID, tv_name, Tv_contactNumber, tv_scope, tv_Area, Tv_unit, tv_location, tv_startingdate, tv_expecteddate,textView_jobTitle;
    TextView WorkerName1,WorkerName2,WorkerName3,WorkerName4,WorkerName5;
    TextView Workerjob1,Workerjob2,Workerjob3,Workerjob4,Workerjob5;
    TextView workercontact1,workercontact2,workercontact3,workercontact4,workercontact5;
    TextView workeraddress1,workeraddress2,workeraddress3,workeraddress4,workeraddress5;
    ImageView avatar1,avatar2,avatar3,avatar4,avatar5;
    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    RecyclerView recyclerView;
    List<pendingemployeesClass> orders = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_jobs, container, false);
        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");

        textView_jobTitle = view.findViewById(R.id.textView_jobTitle);
        tv_jobID = view.findViewById(R.id.tv_jobID);
        tv_name = view.findViewById(R.id.tv_name);
        Tv_contactNumber = view.findViewById(R.id.Tv_contactNumber);
        tv_scope = view.findViewById(R.id.tv_scope);
        tv_Area = view.findViewById(R.id.tv_Area);
        Tv_unit = view.findViewById(R.id.Tv_unit);
        tv_location = view.findViewById(R.id.tv_location);
        tv_startingdate = view.findViewById(R.id.tv_startingdate);
        tv_expecteddate = view.findViewById(R.id.tv_expecteddate);

        WorkerName1 = view.findViewById(R.id.WorkerName1);
        Workerjob1 = view.findViewById(R.id.workerjob1);
        workercontact1 = view.findViewById(R.id.workercontact1);
        workeraddress1 = view.findViewById(R.id.workeraddress1);
        avatar1 = view.findViewById(R.id.avatar1);

        WorkerName2 = view.findViewById(R.id.WorkerName2);
        Workerjob2 = view.findViewById(R.id.workerjob2);
        workercontact2 = view.findViewById(R.id.workercontact2);
        workeraddress2 = view.findViewById(R.id.workeraddress2);
        avatar2 = view.findViewById(R.id.avatar2);

        WorkerName3 = view.findViewById(R.id.WorkerName3);
        Workerjob3 = view.findViewById(R.id.workerjob3);
        workercontact3 = view.findViewById(R.id.workercontact3);
        workeraddress3 = view.findViewById(R.id.workeraddress3);
        avatar3 = view.findViewById(R.id.avatar3);

        WorkerName4 = view.findViewById(R.id.WorkerName4);
        Workerjob3 = view.findViewById(R.id.workerjob4);
        workercontact4 = view.findViewById(R.id.workercontact4);
        workeraddress4 = view.findViewById(R.id.workeraddress4);
        avatar4 = view.findViewById(R.id.avatar4);

        WorkerName5 = view.findViewById(R.id.WorkerName5);
        Workerjob4 = view.findViewById(R.id.workerjob5);
        workercontact5 = view.findViewById(R.id.workercontact5);
        workeraddress5 = view.findViewById(R.id.workeraddress5);
        avatar5 = view.findViewById(R.id.avatar5);

        Bundle data = getArguments();
        if (data != null) {//from
            userId = data.getString("idUser");
            id = data.getString("_id");
            nameuser = data.getString("Name");
            contactNumber = data.getDouble("contactNumber");
            Unitstr = data.getString("Unit");
            ScopeofWork = data.getString("scopeofwork");
            area = data.getString("area");
            Location = data.getString("location");
            ExpectedFinishDate = data.getString("expectedfinishdate");
            jobtitle = data.getString("jobtitle");
            Startingdate = data.getString("startingdate");
            Worker1 = data.getString("Worker1") != null ? data.getString("Worker1") : "";
            Worker2 = data.getString("Worker2") != null ? data.getString("Worker2") : "";
            Worker3 = data.getString("Worker3") != null ? data.getString("Worker3") : "";
            Worker4 = data.getString("Worker4") != null ? data.getString("Worker4") : "";
            Worker5 = data.getString("Worker5") != null ? data.getString("Worker5") : "";
        }

        if (Worker1 != null && !Worker1.isEmpty()) {
            LoadEmployee1();
        }
        if (Worker2 != null && !Worker2.isEmpty()) {
            LoadEmployee2();
        }
        if (Worker3 != null && !Worker3.isEmpty()) {
            LoadEmployee3();
        }
        if (Worker4 != null && !Worker4.isEmpty()) {
            LoadEmployee4();
        }
        if (Worker5 != null && !Worker5.isEmpty()) {
            LoadEmployee5();
        }

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);

        textView_jobTitle.setText(jobtitle);
        tv_jobID.setText(id);
        tv_name.setText(nameuser);
        Tv_contactNumber.setText(df.format(contactNumber));
        tv_scope.setText(ScopeofWork);
        tv_Area.setText(area);
        Tv_unit.setText(Unitstr);
        tv_location.setText(Location);
        tv_startingdate.setText(Startingdate);
        tv_expecteddate.setText(ExpectedFinishDate);



        return view;
    }

    public void LoadEmployee1() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        if (Worker1 != null) {
            ObjectId objectId = new ObjectId(Worker1);
            Document filter = new Document("_id", objectId);
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    Document resultdata = result.get();
                    String display1 = resultdata.getString("avatar");
                    String name1 = resultdata.getString("name");
                    String address1 = resultdata.getString("address");
                    Double contact1 = resultdata.getDouble("contactNumber");
                    String job1 = resultdata.getString("jobType");

                    WorkerName1.setText(name1);
                    workercontact1.setText(String.valueOf(contact1));
                    workeraddress1.setText(address1);
                    Workerjob1.setText(job1);
                    Picasso.get().load(display1).transform(new CropCircleTransformation()).into(avatar1);
                } else {
                    Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                }
            });
        }

    }

    public void LoadEmployee2() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        if (Worker2 != null) {
            ObjectId objectId = new ObjectId(Worker2);
            Document filter = new Document("_id", objectId);
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    Document resultdata = result.get();
                    String display2 = resultdata.getString("avatar");
                    String name2 = resultdata.getString("name");
                    String address2 = resultdata.getString("address");
                    Double contact2 = resultdata.getDouble("contactNumber");
                    String job2 = resultdata.getString("jobType");

                    WorkerName2.setText(name2);
                    workercontact2.setText(String.valueOf(contact2));
                    workeraddress2.setText(address2);
                    Workerjob2.setText(job2);
                    Picasso.get().load(display2).transform(new CropCircleTransformation()).into(avatar2);

                } else {


                    Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                }
            });
        }
    }

    public void LoadEmployee3() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        if (Worker3 != null) {
            ObjectId objectId = new ObjectId(Worker3);
            Document filter = new Document("_id", objectId);
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    Document resultdata = result.get();
                    String display3 = resultdata.getString("avatar");
                    String name3 = resultdata.getString("name");
                    String address3 = resultdata.getString("address");
                    Double contact3 = resultdata.getDouble("contactNumber");
                    String job3 = resultdata.getString("jobType");

                    WorkerName3.setText(name3);
                    workercontact3.setText(String.valueOf(contact3));
                    workeraddress3.setText(address3);
                    Workerjob3.setText(job3);
                    Picasso.get().load(display3).transform(new CropCircleTransformation()).into(avatar3);
                } else {
                    Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                }
            });
        }
    }

    public void LoadEmployee4() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        if (Worker4 != null) {
            ObjectId objectId = new ObjectId(Worker4);
            Document filter = new Document("_id", objectId);
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    Document resultdata = result.get();
                    String display4 = resultdata.getString("avatar");
                    String name4 = resultdata.getString("name");
                    String address4 = resultdata.getString("address");
                    Double contact4 = resultdata.getDouble("contactNumber");
                    String job4 = resultdata.getString("jobType");

                    WorkerName4.setText(name4);
                    workercontact4.setText(String.valueOf(contact4));
                    workeraddress4.setText(address4);
                    Workerjob4.setText(job4);
                    Picasso.get().load(display4).transform(new CropCircleTransformation()).into(avatar4);
                } else {
                    Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                }
            });
        }
    }

    public void LoadEmployee5() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        if (Worker5 != null) {
            ObjectId objectId = new ObjectId(Worker5);
            Document filter = new Document("_id", objectId);
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    Document resultdata = result.get();
                    String display5 = resultdata.getString("avatar");
                    String name5 = resultdata.getString("name");
                    String address5 = resultdata.getString("address");
                    Double contact5 = resultdata.getDouble("contactNumber");
                    String job5 = resultdata.getString("jobType");

                    WorkerName5.setText(name5);
                    workercontact5.setText(String.valueOf(contact5));
                    workeraddress5.setText(address5);
                    Workerjob5.setText(job5);
                    Picasso.get().load(display5).transform(new CropCircleTransformation()).into(avatar5);
                } else {
                    Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                }
            });
        }
    }


}