package com.test.thesis_application.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.test.thesis_application.R;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class OngoinJobs extends Fragment {
    String userId, id, nameuser, Unitstr, ScopeofWork, area, Location, ExpectedFinishDate, jobtitle, Startingdate, Worker1, Worker2, Worker3, Worker4, Worker5;
    TextView tv_jobID, tv_name, Tv_contactNumber, tv_scope, tv_Area, Tv_unit, tv_location, tv_startingdate, tv_expecteddate, textView_jobTitle;
    TextView WorkerName1, WorkerName2, WorkerName3, WorkerName4, WorkerName5;
    TextView Workerjob1, Workerjob2, Workerjob3, Workerjob4, Workerjob5;
    TextView workercontact1, workercontact2, workercontact3, workercontact4, workercontact5;
    Button location1, location2, location3, location4, location5;
    ImageView avatar1, avatar2, avatar3, avatar4, avatar5;
    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    RecyclerView recyclerView;
    List<pendingemployeesClass> orders = new ArrayList<>();
    Double contact1, contactworker1, contactworker2, contactworker3, contactworker4, contactworker5;
    int forecastednum, suggestednum;
    Button finishjob;
    String areastr;
    Document finished;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoin_jobs, container, false);
        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");


        finishjob = view.findViewById(R.id.finishjob);
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
        avatar1 = view.findViewById(R.id.avatar1);
        location1 = view.findViewById(R.id.location1);//button

        WorkerName2 = view.findViewById(R.id.WorkerName2);
        Workerjob2 = view.findViewById(R.id.workerjob2);
        workercontact2 = view.findViewById(R.id.WorkerContact2);
        avatar2 = view.findViewById(R.id.avatar2);
        location2 = view.findViewById(R.id.location2);//button

        WorkerName3 = view.findViewById(R.id.WorkerName3);
        Workerjob3 = view.findViewById(R.id.workerjob3);
        workercontact3 = view.findViewById(R.id.workercontact3);
        avatar3 = view.findViewById(R.id.avatar3);
        location3 = view.findViewById(R.id.location3);//button

        WorkerName4 = view.findViewById(R.id.WorkerName4);
        Workerjob4 = view.findViewById(R.id.workerjob4);
        workercontact4 = view.findViewById(R.id.workercontact4);
        avatar4 = view.findViewById(R.id.avatar4);
        location4 = view.findViewById(R.id.location4);//button


        WorkerName5 = view.findViewById(R.id.WorkerName5);
        Workerjob5 = view.findViewById(R.id.workerjob5);
        workercontact5 = view.findViewById(R.id.workercontact5);
        avatar5 = view.findViewById(R.id.avatar5);
        location5 = view.findViewById(R.id.location5);//button

        Bundle data = getArguments();
        if (data != null) {//from
            userId = data.getString("idUser");
            id = data.getString("_id");// id ni job
            Log.v("MongoDB", "ID ni job " + id + " " + data);

            mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
            mongoCollection = mongoDatabase.getCollection("acceptedorders");
            ObjectId idjob = new ObjectId(id);
            Document find = new Document("_id", idjob);
            mongoCollection.findOne(find).getAsync(result -> {
                Document resultdataid = result.get();
                if (result.isSuccess()) {
                    Log.v("MongoDB", resultdataid.toString());
                    nameuser = resultdataid.getString("ClientName");
                    Log.v("MongoDb", nameuser);
                    Unitstr = resultdataid.getString("Unit");
                    Log.v("MongoDb", Unitstr);
                    ScopeofWork = resultdataid.getString("TypeOfWork");
                    Log.v("MongoDb", ScopeofWork);
                    area = resultdataid.getString("Area");
                    Log.v("MongoDb", area);
                    Location = resultdataid.getString("Location");
                    Log.v("MongoDb", Location);
                    ExpectedFinishDate = resultdataid.getString("ExpectedFinishDate");
                    Log.v("MongoDb", ExpectedFinishDate);
                    jobtitle = resultdataid.getString("ProjectName");
                    Log.v("MongoDb", jobtitle);
                    Startingdate = resultdataid.getString("StartingDate");
                    Log.v("MongoDb", Startingdate);
                    forecastednum = resultdataid.getInteger("ForecastedNum");
                    Log.v("MongoDb", "" + forecastednum);
                    suggestednum = resultdataid.getInteger("SuggestedNum");
                    Log.v("MongoDb", "" + suggestednum);
                    Object contactNumberObj = resultdataid.get("ContactNumber");
                    if (contactNumberObj instanceof Double) {
                        contact1 = (Double) contactNumberObj;
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(0);
                        Tv_contactNumber.setText(df.format(contact1));
                    } else if (contactNumberObj instanceof Integer) {
                        contact1 = ((Integer) contactNumberObj).doubleValue();
                        Tv_contactNumber.setText(contact1.toString());
                    } else {
                        // handle error case here
                    }


                    Worker1 = resultdataid.getString("Worker1") != null ? resultdataid.getString("Worker1") : "";
                    Worker2 = resultdataid.getString("Worker2") != null ? resultdataid.getString("Worker2") : "";
                    Worker3 = resultdataid.getString("Worker3") != null ? resultdataid.getString("Worker3") : "";
                    Worker4 = resultdataid.getString("Worker4") != null ? resultdataid.getString("Worker4") : "";
                    Worker5 = resultdataid.getString("Worker5") != null ? resultdataid.getString("Worker5") : "";
//                    Log.v("MongoDB", "ID" + id);
//                    Log.v("MongoDB", "Worker1 " + Worker1);
//                    Log.v("MongoDB", "Worker2 " + Worker2);
//                    Log.v("MongoDB", "Worker3 " + Worker3);
//                    Log.v("MongoDB", "Worker4 " + Worker4);
//                    Log.v("MongoDB", "Worker5 " + Worker5);

                    textView_jobTitle.setText(jobtitle);
                    tv_jobID.setText(id);
                    tv_name.setText(nameuser);
                    tv_scope.setText(ScopeofWork);
                    tv_Area.setText(area);
                    Tv_unit.setText(Unitstr);
                    tv_location.setText(Location);
                    tv_startingdate.setText(Startingdate);
                    tv_expecteddate.setText(ExpectedFinishDate);

                    if (Worker1 != null && !Worker1.isEmpty()) {
                        LoadEmployee1();
                        Log.v("Mongodb", "Worker1 id found: " + Worker1);
                    } else {

                    }
                    if (Worker2 != null && !Worker2.isEmpty()) {
                        LoadEmployee2();
                        Log.v("Mongodb", "Worker2 id found: " + Worker2);

                    } else {
                        location2.setEnabled(false);
                    }
                    if (Worker3 != null && !Worker3.isEmpty()) {
                        LoadEmployee3();
                        Log.v("Mongodb", "Worker3 id found: " + Worker3);

                    } else {
                        location3.setEnabled(false);
                    }
                    if (Worker4 != null && !Worker4.isEmpty()) {
                        LoadEmployee4();
                        Log.v("Mongodb", "Worker4 id found: " + Worker4);

                    } else {
                        location4.setEnabled(false);
                    }
                    if (Worker5 != null && !Worker5.isEmpty()) {
                        LoadEmployee5();
                        Log.v("Mongodb", "Worker5 id found: " + Worker5);

                    } else {
                        location5.setEnabled(false);
                    }


                }
            });

        }


        location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("MongoDB",userId);
                Log.v("MongoDB",Worker1);

                fragment_maps fragmentMaps = new fragment_maps();
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID", userId); // to fragment_maps()
                mapsuid.putString("employeeID", Worker1);
                Log.v("MapsClient",userId+" "+Worker1);
                fragmentMaps.setArguments(mapsuid);

                FragmentTransaction mapsTransaction = getParentFragmentManager().beginTransaction();
                mapsTransaction.replace(R.id.fragment_container, fragmentMaps)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_maps fragmentMaps = new fragment_maps();
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID", userId); // to fragment_maps()
                mapsuid.putString("employeeID", Worker2);
                fragmentMaps.setArguments(mapsuid);

                FragmentTransaction mapsTransaction = getParentFragmentManager().beginTransaction();
                mapsTransaction.replace(R.id.fragment_container, fragmentMaps)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        location3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_maps fragmentMaps = new fragment_maps();
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID", userId); // to fragment_maps()
                mapsuid.putString("employeeID", Worker3);
                fragmentMaps.setArguments(mapsuid);

                FragmentTransaction mapsTransaction = getParentFragmentManager().beginTransaction();
                mapsTransaction.replace(R.id.fragment_container, fragmentMaps)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        location4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_maps fragmentMaps = new fragment_maps();
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID", userId); // to fragment_maps()
                mapsuid.putString("employeeID", Worker4);
                fragmentMaps.setArguments(mapsuid);

                FragmentTransaction mapsTransaction = getParentFragmentManager().beginTransaction();
                mapsTransaction.replace(R.id.fragment_container, fragmentMaps)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        location5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_maps fragmentMaps = new fragment_maps();
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID", userId); // to fragment_maps()
                mapsuid.putString("employeeID", Worker5);
                fragmentMaps.setArguments(mapsuid);

                FragmentTransaction mapsTransaction = getParentFragmentManager().beginTransaction();
                mapsTransaction.replace(R.id.fragment_container, fragmentMaps)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        finishjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectId idjobs = new ObjectId(id);

                finished = new Document("_id", idjobs).append("idUser", userId).append("ProjectName", jobtitle).append("ClientName", nameuser).append("ContactNumber", contact1).append("TypeOfWork", ScopeofWork).append("Area", area).append("Unit", Unitstr).append("Location", Location)
                        .append("StartingDate", Startingdate).append("ExpectedFinishDate", ExpectedFinishDate).append("ForecastedNum", Integer.valueOf(forecastednum)).append("SuggestedNum", Integer.valueOf(suggestednum)); //
                if (Worker1 != null && !Worker1.isEmpty()) {
                    Log.v("Mongodb", "Worker1 id: " + Worker1);
                    finished.append("Worker1", Worker1).append("WorkerName1", WorkerName1.getText().toString());
                }
                if (Worker2 != null && !Worker2.isEmpty()) {
                    Log.v("Mongodb", "Worker2 id: " + Worker2);

                    finished.append("Worker2", Worker2).append("WorkerName2", WorkerName2.getText().toString());

                }
                if (Worker3 != null && !Worker3.isEmpty()) {
                    Log.v("Mongodb", "Worker3 id: " + Worker3);

                    finished.append("Worker2", Worker3).append("WorkerName3", WorkerName3.getText().toString());

                }
                if (Worker4 != null && !Worker4.isEmpty()) {
                    Log.v("Mongodb", "Worker4 id: " + Worker4);

                    finished.append("Worker2", Worker4).append("WorkerName4", WorkerName4.getText().toString());

                }
                if (Worker5 != null && !Worker5.isEmpty()) {
                    Log.v("Mongodb", "Worker5 id: " + Worker5);

                    finished.append("Worker2", Worker5).append("WorkerName5", WorkerName5.getText().toString());

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Are you sure that this job is finished?");
                builder.setPositiveButton("Yes, Job is done.", (dialog, which) -> {
                    ObjectId objectId = new ObjectId(id);
                    Document filter = new Document("_id", objectId);
                    mongoCollection.findOneAndDelete(filter).getAsync(result1 -> {
                        if (result1.isSuccess()) {
                            Log.v("MongoDB", " result finished task: " + result1.toString());
                            Log.v("MongoDB", "Finished Document: " + finished.toString());
                            mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
                            mongoCollection = mongoDatabase.getCollection("jobhistories");
                            mongoCollection.insertOne(finished.append("created", new Date())).getAsync(result2 -> {
                                if (result2.isSuccess()) {
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    Toast.makeText(requireContext(), "failed", Toast.LENGTH_LONG).show();
                                }
                            });
                            Toast.makeText(requireContext(), "Deleted successfully.", Toast.LENGTH_LONG).show();
//                            // Close the current fragment and go back to the previous one
//                            requireActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(requireContext(), "Deletion failed.", Toast.LENGTH_LONG).show();
                        }
                    });

                });// end of DialogInterface :D
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public void LoadEmployee1() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");
        if (Worker1 != null) {
            Log.v("MongoDB","Going To find "+ Worker1);
            ObjectId objectId = new ObjectId(Worker1);
            Document filter = new Document("_id", objectId);
//            Log.v("MongoDB", filter.toString());
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    Document resultdata = result.get();
//                    Log.v("MongoDB","Result Data for Worker1"+resultdata.toString());
                    Log.v("MongoDB",resultdata.toString());
                    String display1 = resultdata.getString("avatar");

                    String name1 = resultdata.getString("name");


                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contactworker1 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contactworker1 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contactworker1));

                    Toast.makeText(requireContext(), "name ni worker1: " +name1, Toast.LENGTH_LONG).show();

                    String job1 = resultdata.getString("jobType");
                    WorkerName1.setText( resultdata.getString("name"));
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
                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contactworker2 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contactworker2 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact2.setText(df.format(contactworker2));

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
                    Log.v("MongoDB",resultdata.toString());

                    String display3 = resultdata.getString("avatar");
                    String name3 = resultdata.getString("name");
                    String job3 = resultdata.getString("jobType");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contactworker3 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contactworker3 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contactworker3));

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
                    Log.v("MongoDB",resultdata.toString());

                    String display4 = resultdata.getString("avatar");
                    String name4 = resultdata.getString("name");
                    String job4 = resultdata.getString("jobType");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contactworker4 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contactworker4 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contactworker4));

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
                    Log.v("MongoDB",resultdata.toString());

                    String display5 = resultdata.getString("avatar");
                    String name5 = resultdata.getString("name");
                    String job5 = resultdata.getString("jobType");

                    Object contactNumberObj = resultdata.get("contactNumber");
                    if (contactNumberObj instanceof Double) {
                        contactworker5 = (Double) contactNumberObj;
                    } else if (contactNumberObj instanceof Integer) {
                        contactworker5 = ((Integer) contactNumberObj).doubleValue();
                    } else {
                        // handle error case here
                    }
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(0);
                    workercontact1.setText(df.format(contactworker5));

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