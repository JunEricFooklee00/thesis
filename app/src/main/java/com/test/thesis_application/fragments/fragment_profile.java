package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.test.thesis_application.R;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class fragment_profile extends Fragment {
    private static final String arg_name = "argName";
    private static final String arg_Username = "argUserName";

    private String name, email, resume, username, birthday, contactNumber, address, zipcode;
    private Button button;
    private ImageView profile_pic;
    private TextView tv_profilename, tv_profileEmail, tv_username, tv_birthday, tv_contactNumber, tv_address, tv_zipcode;


    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

//
//    public static fragment_profile newInstance(String name, String email) {
//
//        fragment_profile fragProf = new fragment_profile();
//        Bundle args = new Bundle();
//        args.putString(arg_name, name);
//        args.putString(arg_Username, email);
//        fragProf.setArguments(args);
//        return fragProf;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_profilename = view.findViewById(R.id.profile_name);
        tv_profileEmail = view.findViewById(R.id.db_email);
        tv_username = view.findViewById(R.id.profile_username);
        tv_contactNumber = view.findViewById(R.id.db_contact);
        tv_address = view.findViewById(R.id.db_address);
        tv_zipcode = view.findViewById(R.id.db_zipcode);
//        button = view.findViewById(R.id.fragment_profile_button);
        profile_pic = view.findViewById(R.id.profile_picture);

//        assert user != null;
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("CourseData");
//        mongoCollection = mongoDatabase.getCollection("clients");

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "working", Toast.LENGTH_LONG).show();
//            }
//        });
        Bundle data = getArguments();
        if (data != null) {
            name = data.getString("name");
            email = data.getString("email");
            resume = data.getString("resume");
            username = data.getString("username");
//            birthday = data.getString("birthday");
            contactNumber = data.getString("contactNumber");
            address = data.getString("address");
            zipcode = data.getString("zipcode");
        }
        tv_contactNumber.setText("+63" +contactNumber);
        tv_address.setText(address);
        tv_zipcode.setText(zipcode);
        tv_username.setText(username);
        tv_profilename.setText(name);
        tv_profileEmail.setText(email);
        Picasso.get().load(resume).transform(new CropCircleTransformation()).into(profile_pic);

//
//
//        tv_profileEmail.setText(getArguments().getString("argName"));
//        tv_profilename.setText(getArguments().getString("argUserName"));
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getArguments() != null){
////                    name = getArguments().getString(arg_name);
////                    email = getArguments().getString(arg_Username);
////                    Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();
////                }
//
//            }
//        });
        return view;
    }
}
