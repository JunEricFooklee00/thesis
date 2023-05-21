package com.test.thesis_application;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.mindrot.jbcrypt.BCrypt;

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

    String hashedPassword;
    //mongodb
    String Appid = "employeems-mcwma";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    //viewBinding requirements
    private Button loginClientAccount;
    private TextInputLayout get_email, get_password;
    private TextView registerButton;

    private SharedPreferences preferences;


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
                    // todo: change the fucking CourseData next time into Users clients and the Users employees too.

                } else {
                    Log.v("user", "Cannot Access Database");
                }
            }
        });

        // Get the shared preferences object
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if email and password are already saved in shared preferences
        String savedEmail = preferences.getString("email", "");
        String savedPassword = preferences.getString("password", "");
        if (!savedEmail.equals("") && !savedPassword.equals("")) {

            // If email and password are saved, automatically log in the user
            get_email.getEditText().setText(savedEmail);
            get_password.getEditText().setText(savedPassword);
            Log.d("SharedPref", "Successfully added credentials");
            login(); // Call the login() method to log in the user
            Log.d("SharedPref", "Hopefully logs in the account");
        }
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
                login();
            }
        });


        requestPermission();
    }// end of onCreate


    private void login() {
        try {
            if (!validateemail() | !validatePassword()) {
                return;
            }
            mongoDatabase = mongoClient.getDatabase("Users");
            mongoCollection = mongoDatabase.getCollection("clients");

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", get_email.getEditText().getText().toString());
            editor.putString("password", get_password.getEditText().getText().toString());
            editor.apply();

            if (sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
                String email = sharedPreferences.getString("email", "");
                String password = sharedPreferences.getString("password", "");
                // Check if the user is a client
                mongoCollection = mongoDatabase.getCollection("clients");
                Document emailQuery = new Document("email", email);
                mongoCollection.findOne(emailQuery).getAsync(result -> {
                    try {
                        Document resultData = result.get();
                        String id = resultData.getObjectId("_id").toString();
                        String hashedPassword = resultData.getString("password");
                        Log.v("resultpassword",hashedPassword);
                        Log.v("resultpassword",password);
                        if (resultData != null && resultData.getString("user").equals("Client")) {

                            boolean passwordMatch = verifyPassword(password, hashedPassword);
                            if (passwordMatch) {
                                Intent homeScreen = new Intent(MainActivity.this, client_home.class);
                                homeScreen.putExtra("user_ID", id);
                                startActivity(homeScreen);
                            } else {
                                Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_LONG).show();
                            }
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Check if the user is an employee
                    mongoCollection = mongoDatabase.getCollection("employees");
                    Document employeeQuery = new Document("email", email);
                    mongoCollection.findOne(employeeQuery).getAsync(result1 -> {
                        try {
                            Document resultData1 = result1.get();
                            String idEmployee = resultData1.getObjectId("_id").toString();
                            Log.v("resultData",idEmployee);
                            String hashedPassword2 = resultData1.getString("password");

                            if (resultData1 != null && resultData1.getString("user").equals("Employee")) {

                                boolean passwordMatch = verifyPassword(password, hashedPassword2);
                                if (passwordMatch) {
                                    Intent homeScreen = new Intent(MainActivity.this, employee_home.class);
                                    homeScreen.putExtra("user_ID", idEmployee);
                                    startActivity(homeScreen);
                                } else {
                                    Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_LONG).show();
                                }

                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // User not found
                        Toast.makeText(MainActivity.this, "Wrong password or username", Toast.LENGTH_LONG).show();
                        Log.v("resultAccount", "Account doesn't exist.");
                    });
                });

            }
        }catch (Exception e){
            Log.v("result","error");
        }
    }
    private boolean verifyPassword(String inputPassword, String hashedPassword) {
        // Check if the input password matches the hashed password
        boolean passwordMatch = BCrypt.checkpw(inputPassword, hashedPassword);
        return passwordMatch;
    }

    private boolean validateemail() {
        String emailinput = get_email.getEditText().getText().toString().trim();
        if (emailinput.isEmpty()) {
            get_email.setError("Field Email is Empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()) {
            get_email.setError("Email is Invalid.");
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
            get_password.setError("Field Password is Empty.");
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