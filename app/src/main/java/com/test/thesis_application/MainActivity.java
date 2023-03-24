package com.test.thesis_application;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.employee.employee_home;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    //for request permission
    private boolean isReadPermissionGranted = false;
    private boolean isWritePermissionGranted = false;
    private boolean isLocationPermissionGranted = false;
    ActivityResultLauncher<String[]> mPermissionResultLauncher;

    //mongodb
    String Appid = "employeems-mcwma";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    //viewBinding requirements
    private Button loginClientAccount;
    private TextInputLayout get_email, get_password;
    private TextView registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        loginClientAccount = findViewById(R.id.click_login);
        get_email = findViewById(R.id.TIL_Emailinput);
        get_password = findViewById(R.id.TIL_PasswordInput);
        registerButton = findViewById(R.id.btn_register);

        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) != null) {
                    isReadPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE));
                }
                if (result.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != null) {
                    isWritePermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                }
                if (result.get(android.Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                    isLocationPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                }

            }

        });

        App app = new App(new AppConfiguration.Builder(Appid).build());
        app.loginAsync(Credentials.emailPassword("everyoneusingtheapp@gmail.com", "Ems2023"), new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("user", "database now accessible");
                    User user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("CourseData");
                } else {
                    Log.v("user", "Cannot Access Database");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent R = new Intent(MainActivity.this, Register_Form.class);
                startActivity(R);

            }
        });

        loginClientAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!validateemail() | !validatePassword()) {
                        return;
                    }
                    mongoCollection = mongoDatabase.getCollection("clients");
                    Document email = new Document().append("email", get_email.getEditText().getText().toString()).append("password", get_password.getEditText().getText().toString());

                    mongoCollection.findOne(email).getAsync(result -> {
                        try {
                            //Testing Client accounts

                            Toast.makeText(getApplicationContext(), "Testing Clients", Toast.LENGTH_LONG).show();

                            Document resultData = result.get();
                            Log.v("Account", resultData.getString("user"));
                            if (resultData.getString("user").equals("Clients")) {
                                Toast.makeText(getApplicationContext(), "Client Logged in", Toast.LENGTH_LONG).show();
                                Log.v("resultAccount", "Found in Client");
                                Intent home_screen = new Intent(MainActivity.this, client_home.class);
                                home_screen.putExtra("username", resultData.getString("username"))
                                        .putExtra("name", resultData.getString("name"))
                                        .putExtra("email", resultData.getString("email"))
                                        .putExtra("contactNumber", resultData.getString("contactNumber"))
                                        .putExtra("age", resultData.getString("age"))
                                        .putExtra("address", resultData.getString("address"))
                                        .putExtra("zipcode", resultData.getString("zipcode"))
                                        .putExtra("resume", resultData.getString("resume"));


                                startActivity(home_screen);
                            } else {
                                Toast.makeText(getApplicationContext(), "Client Wrong Password or Username", Toast.LENGTH_LONG).show();
                                Log.v("resultAccount", "Client not found in Database");

                            }

                        } catch (Exception client) {
                            //Testing Employee Accounts
                            Toast.makeText(getApplicationContext(), "Testing Employees", Toast.LENGTH_LONG).show();

                            mongoCollection = mongoDatabase.getCollection("employees");
                            mongoCollection.findOne(email).getAsync(result1 -> {
                                try {
                                    Document resultData1 = result1.get();
                                    Toast.makeText(getApplicationContext(), resultData1.toString(), Toast.LENGTH_LONG).show();

                                    if (resultData1.getString("user").equals("Employees")) {
                                        Toast.makeText(getApplicationContext(), "Employee Logged in", Toast.LENGTH_LONG).show();
                                        Log.v("resultAccount", "Found in Employee");
                                        Intent home_screen = new Intent(MainActivity.this, employee_home.class);
                                        home_screen.putExtra("username", resultData1.getString("username"))
                                                .putExtra("name", resultData1.getString("name"))
                                                .putExtra("user_ID",resultData1.getObjectId("_id").toString())
                                                .putExtra("email", resultData1.getString("email"))
                                                .putExtra("contactNumber", resultData1.getString("contactNumber"))
                                                .putExtra("age", resultData1.getString("age"))
                                                .putExtra("address", resultData1.getString("address"))
                                                .putExtra("zipcode", resultData1.getString("zipcode"))
                                                .putExtra("resume", resultData1.getString("resume"));
                                        startActivity(home_screen);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Employee Wrong Password or Username", Toast.LENGTH_LONG).show();
                                        Log.v("resultAccount", "Wala sa Employee");

                                    }
                                } catch (Exception employee) {
                                    employee.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "No user existing.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        requestPermission();
    }// end of onCreate

    private boolean validateemail() {
        String emailinput = get_email.getEditText().getText().toString().trim();
        if (emailinput.isEmpty()) {
            get_email.setError("Field Empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()) {
            get_email.setError("Email Invalid");
            return false;
        } else {
            get_email.setError(null);
            get_email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String PasswordInput = get_password.getEditText().getText().toString().trim();
        if (PasswordInput.isEmpty()) {
            get_password.setError("Field Empty");
            return false;

        } else {
            get_password.setError(null);
            get_password.setErrorEnabled(false);
            return true;
        }
    }

    private void requestPermission() {

        Log.v("Result", "Requesting Permission");

        boolean minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        isWritePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        isWritePermissionGranted = isWritePermissionGranted || minSDK;

        List<String> permissionRequest = new ArrayList<String>();

        if (!isReadPermissionGranted) {

            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        }
        if (!isWritePermissionGranted) {

            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!isLocationPermissionGranted) {

            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionRequest.isEmpty()) {

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }

    }
}