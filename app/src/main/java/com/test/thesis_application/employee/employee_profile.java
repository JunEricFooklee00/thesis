package com.test.thesis_application.employee;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
import io.realm.mongodb.mongo.options.UpdateOptions;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class employee_profile extends Fragment {

    private String name, email, resume, username, birthday, address,  uid, utype;
    Double contactNumber;
    private Button button;
    private ImageView profile_pic;
    private TextView tv_profilename, tv_birthday, tv_zipcode, tv_uid, userType;
    private EditText tv_username, tv_profileEmail, tv_contactNumber, tv_address;
    private Button editprofile;
    Integer zipcode;
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    boolean isEditing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_profile, container, false);

        tv_profilename = view.findViewById(R.id.profile_name);
        tv_profileEmail = view.findViewById(R.id.db_email);
        tv_username = view.findViewById(R.id.profile_username);
        tv_contactNumber = view.findViewById(R.id.db_contact);
        tv_address = view.findViewById(R.id.db_address);
        profile_pic = view.findViewById(R.id.profile_picture);
        tv_uid = view.findViewById(R.id.profile_ud);
        editprofile = view.findViewById(R.id.Editprofile);
        userType = view.findViewById(R.id.user_position);


        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("employees");

        Bundle data = getArguments();
        if (data != null) {
            uid = data.getString("uid");
        }
        tv_uid.setText(uid);

        ObjectId objectId = new ObjectId(uid);
        Document filter = new Document("_id", objectId);
        mongoCollection.findOne(filter).getAsync(result -> {
            if(result.isSuccess()){
                Document resultdata = result.get();
                name = resultdata.getString("name");
                email = resultdata.getString("email");
                resume = resultdata.getString("avatar");
                username = resultdata.getString("username");
                contactNumber = resultdata.getDouble("contactNumber");
                address = resultdata.getString("address");
                utype = resultdata.getString("jobType");


                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(0);
                userType.setText(utype);
//                tv_contactNumber.setText(""+df.format(contactNumber));
                tv_address.setText(address);
                tv_username.setText(username);
                tv_profilename.setText(name);
                tv_profileEmail.setText(email);
                Picasso.get().load(resume).transform(new CropCircleTransformation()).into(profile_pic);


            }else{
                Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
            }
        });

        tv_username.setEnabled(false);
        tv_profileEmail.setEnabled(false);
        tv_contactNumber.setEnabled(false);
        tv_address.setEnabled(false);

//        editprofile.setEnabled(true);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editprofile.getText().equals("EDIT")) {
                    // save mode
                    editprofile.setText("SAVE");
                    tv_username.setEnabled(true);
                    tv_profileEmail.setEnabled(true);
                    tv_contactNumber.setEnabled(true);
                    tv_address.setEnabled(true);

//                    isEditing = false;
                } else if (editprofile.getText().equals("SAVE")) {
                    editprofile.setText("EDIT");
                    tv_username.setEnabled(false);
                    tv_profileEmail.setEnabled(false);
                    tv_contactNumber.setEnabled(false);
                    tv_address.setEnabled(false);
                    updateOne();
                } else {
                    Log.v("MongoDB","HUH");
//GOLTEB SHEET
                }
            }
        });
        return view;
    }

    private void updateOne() {
        ObjectId objectId = new ObjectId(uid);

        // Create a filter using the objectId
        Document filter = new Document("_id", objectId);

        // Create a document with the location data
        Document update = new Document("$set",
                new Document("username", tv_username.getText().toString())
                        .append("email", tv_profileEmail.getText().toString())
                        .append("contactNumber",Double.valueOf(tv_contactNumber.getText().toString()))
                        .append("address",tv_address.getText().toString()));

        // Use the upsert option to insert a new document if the filter doesn't match any document
        UpdateOptions options = new UpdateOptions().upsert(true);

        // Update the document in the collection
        mongoCollection.updateOne(filter, update, options).getAsync(result -> {
            if (result.isSuccess()) {

                Log.v("MongoDB", "Document updated successfully!");
                // set isEditing to false after the update is done

            } else {
                Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

    }
}