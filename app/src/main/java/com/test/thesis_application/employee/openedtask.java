package com.test.thesis_application.employee;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.test.thesis_application.R;
import com.test.thesis_application.fragments.fragment_maps;

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

public class openedtask extends Fragment {
    String userId, id, nameuser, Unitstr, ScopeofWork, area, Location , jobtitle,myid;
    Double contactNumber;
    TextView tv_jobID, tv_name, Tv_contactNumber, tv_scope, tv_Area, Tv_unit, tv_location, tv_startingdate, tv_expecteddate,textView_jobTitle;

    ImageView avatar1;
    TextView clientcontact1,ClientName1;

    Button location1;
    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_openedtask, container, false);

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
        avatar1 = view.findViewById(R.id.avatar1);
        clientcontact1 = view.findViewById(R.id.clientcontact1);
        ClientName1 = view.findViewById(R.id.ClientName1);
        location1 = view.findViewById(R.id.location1);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
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
            jobtitle = data.getString("jobtitle");
             myid = data.getString("employeeid");
        }

        textView_jobTitle.setText(jobtitle);
        tv_jobID.setText(id);
        tv_name.setText(nameuser);
        Tv_contactNumber.setText(df.format(contactNumber));
        tv_scope.setText(ScopeofWork);
        tv_Area.setText(area);
        Tv_unit.setText(Unitstr);
        tv_location.setText(Location);
        LoadClient();


        location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_maps_employee fragmentMaps = new fragment_maps_employee();
                Bundle mapsuid = new Bundle();
                mapsuid.putString("user_ID",userId); // to fragment_maps()
                mapsuid.putString("employeeID",myid);
                fragmentMaps.setArguments(mapsuid);

                FragmentTransaction mapsTransaction = getParentFragmentManager().beginTransaction();
                mapsTransaction.replace(R.id.fragment_container, fragmentMaps)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void LoadClient() {
        mongoDatabase = mongoClient.getDatabase("Users");
        mongoCollection = mongoDatabase.getCollection("clients");

        ObjectId objectId = new ObjectId(userId);
        Document filter = new Document("_id", objectId);
        mongoCollection.findOne(filter).getAsync(result -> {
            if (result.isSuccess()) {
                Document resultdata = result.get();
                String display1 = resultdata.getString("avatar");
                String name1 = resultdata.getString("name");
                Double contact1 = resultdata.getDouble("contactNumber");
                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(0);

                clientcontact1.setText(df.format(contact1));
                ClientName1.setText(name1);
                Picasso.get().load(display1).transform(new CropCircleTransformation()).into(avatar1);
            } else {
                Log.e("MongoDB", "Failed to update document: " + result.getError().getErrorMessage());
            }
        });
    }

}
