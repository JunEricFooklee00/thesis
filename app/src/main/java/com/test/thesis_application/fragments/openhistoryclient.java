package com.test.thesis_application.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.thesis_application.R;

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

public class openhistoryclient extends Fragment {
    String userId, id, nameuser, Unitstr, ScopeofWork, area, Location, ExpectedFinishDate, jobtitle, Startingdate, Worker1, Worker2, Worker3, Worker4, Worker5;
    TextView tv_jobID, tv_name, Tv_contactNumber, tv_scope, tv_Area, Tv_unit, tv_location, tv_startingdate, tv_expecteddate, textView_jobTitle;
    TextView WorkerName1, WorkerName2, WorkerName3, WorkerName4, WorkerName5;
    TextView Workerjob1, Workerjob2, Workerjob3, Workerjob4, Workerjob5;
    TextView workercontact1, workercontact2, workercontact3, workercontact4, workercontact5;
    TextView workeraddress1, workeraddress2, workeraddress3, workeraddress4, workeraddress5;
    Button location1, location2, location3, location4, location5;
    ImageView avatar1, avatar2, avatar3, avatar4, avatar5;
    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    Double contact1,contact2,contact3,contact4,contact5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_openhistoryclient, container, false);

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");

        textView_jobTitle = view.findViewById(R.id.textView_jobTitle);
        tv_jobID = view.findViewById(R.id.tv_jobID);
        tv_name = view.findViewById(R.id.tv_name);

        tv_scope = view.findViewById(R.id.tv_scope);
        tv_Area = view.findViewById(R.id.tv_Area);
        Tv_unit = view.findViewById(R.id.Tv_unit);
        tv_location = view.findViewById(R.id.tv_location);
        tv_startingdate = view.findViewById(R.id.tv_startingdate);
        tv_expecteddate = view.findViewById(R.id.tv_expecteddate);

        WorkerName1 = view.findViewById(R.id.WorkerName1);
        Workerjob1 = view.findViewById(R.id.workerjob1);
        workercontact1 = view.findViewById(R.id.workercontact1);
        avatar1 = view.findViewById(R.id.avatar1);


        WorkerName2 = view.findViewById(R.id.WorkerName2);
        Workerjob2 = view.findViewById(R.id.workerjob2);
        workercontact2 = view.findViewById(R.id.workercontact2);
        avatar2 = view.findViewById(R.id.avatar2);


        WorkerName3 = view.findViewById(R.id.WorkerName3);
        Workerjob3 = view.findViewById(R.id.workerjob3);
        workercontact3 = view.findViewById(R.id.workercontact3);
        avatar3 = view.findViewById(R.id.avatar3);


        WorkerName4 = view.findViewById(R.id.WorkerName4);
        Workerjob4 = view.findViewById(R.id.workerjob4);
        workercontact4 = view.findViewById(R.id.workercontact4);
        avatar4 = view.findViewById(R.id.avatar4);


        WorkerName5 = view.findViewById(R.id.WorkerName5);
        Workerjob5 = view.findViewById(R.id.workerjob5);
        workercontact5 = view.findViewById(R.id.workercontact5);
        avatar5 = view.findViewById(R.id.avatar5);

        Bundle data = getArguments();
        if (data != null) {//from
            userId = data.getString("idUser");
            id = data.getString("_id");// id ni job
            Log.v("MongoDB", "ID ni job " + userId + " " + data);
            mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
            mongoCollection = mongoDatabase.getCollection("jobhistories");
            ObjectId jobidfind = new ObjectId(id);
            Document query = new Document("_id",jobidfind);
            mongoCollection.findOne(query).getAsync(result -> {
                if (result.isSuccess()){
                    Document resultdata = result.get();
                    Log.v("MongoDB","DataFound: "+resultdata);

                    nameuser = resultdata.getString("ClientName");
                    Unitstr = resultdata.getString("Unit");
                    ScopeofWork = resultdata.getString("TypeOfWork");
                    area = resultdata.getString("Area");
                    Location = resultdata.getString("Location");
                    ExpectedFinishDate = resultdata.getString("ExpectedFinishDate");
                    jobtitle = resultdata.getString("ProjectName");
                    Startingdate = resultdata.getString("StartingDate");



                    textView_jobTitle.setText(jobtitle);
                    tv_jobID.setText(id);
                    tv_name.setText(nameuser);

                    tv_scope.setText(ScopeofWork);
                    tv_Area.setText(area);
                    Tv_unit.setText(Unitstr);
                    tv_location.setText(Location);
                    tv_startingdate.setText(Startingdate);
                    tv_expecteddate.setText(ExpectedFinishDate);

                    Worker1 = resultdata.getString("Worker1") != null ? resultdata.getString("Worker1") : "";
                    Worker2 = resultdata.getString("Worker2") != null ? resultdata.getString("Worker2") : "";
                    Worker3 = resultdata.getString("Worker3") != null ? resultdata.getString("Worker3") : "";
                    Worker4 = resultdata.getString("Worker4") != null ? resultdata.getString("Worker4") : "";
                    Worker5 = resultdata.getString("Worker5") != null ? resultdata.getString("Worker5") : "";
                    Log.v("MongoDB", "ID" + id);
                    Log.v("MongoDB", "Worker1 " + Worker1);
                    Log.v("MongoDB", "Worker2 " + Worker2);
                    Log.v("MongoDB", "Worker3 " + Worker3);
                    Log.v("MongoDB", "Worker4 " + Worker4);
                    Log.v("MongoDB", "Worker5 " + Worker5);
                    if (Worker1 != null && !Worker1.isEmpty()) {
                        Log.v("Mongodb","Worker1 id: "+ Worker1);
                        LoadEmployee1();
                    }
                    if (Worker2 != null && !Worker2.isEmpty()) {
                        Log.v("Mongodb","Worker2 id: "+ Worker2);
                        LoadEmployee2();

                    }
                    if (Worker3 != null && !Worker3.isEmpty()) {
                        Log.v("Mongodb","Worker3 id: "+ Worker3);

                        LoadEmployee3();
                    }
                    if (Worker4 != null && !Worker4.isEmpty()) {
                        Log.v("Mongodb","Worker4 id: "+ Worker4);
                        LoadEmployee4();
                    }
                    if (Worker5 != null && !Worker5.isEmpty()) {
                        Log.v("Mongodb","Worker5 id: "+ Worker5);
                        LoadEmployee5();

                    }
                }

            });



//            Object contactNumberObj = data.get("ContactNumber");
//            if (contactNumberObj instanceof Double) {
//                contact1 = (Double) contactNumberObj;
//            } else if (contactNumberObj instanceof Integer) {
//                contact1 = ((Integer) contactNumberObj).doubleValue();
//            } else {
//                // handle error case here
//            }
//            DecimalFormat df = new DecimalFormat("#");
//            df.setMaximumFractionDigits(0);
//            Tv_contactNumber.setText(df.format(contact1));
        }
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
                    Log.v("MongoDB",resultdata.toString());
                    String display1 = resultdata.getString("avatar");
                    String name1 = resultdata.getString("name");
                    String address1 = resultdata.getString("address");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact1 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contact1 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contact1));


                    String job1 = resultdata.getString("jobType");
                    WorkerName1.setText(name1);
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
                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact2 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contact2 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact2.setText(df.format(contact2));

                    String job2 = resultdata.getString("jobType");
                    WorkerName2.setText(name2);
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
                    String job3 = resultdata.getString("jobType");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact3 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contact3 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contact3));

                    WorkerName3.setText(name3);
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
                    String job4 = resultdata.getString("jobType");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact4 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contact4 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contact4));

                    WorkerName4.setText(name4);
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
                    String job5 = resultdata.getString("jobType");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact5 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contact5 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contact5));

                    WorkerName5.setText(name5);

                    Workerjob5.setText(job5);
                    Picasso.get().load(display5).transform(new CropCircleTransformation()).into(avatar5);
                } else {
                    Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
                }
            });
        }
    }
}

