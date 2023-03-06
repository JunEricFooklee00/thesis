package com.test.thesis_application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class Register_Form extends AppCompatActivity {
    private DrawerLayout drawer_reg;

    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    Map config = new HashMap();
    //    private static final int IMAGE_REQ = 1;
    private static final String TAG = "Upload ###";
    private Uri imagePath;

    //for request permission
    private boolean isReadPermissionGranted = false;
    ActivityResultLauncher<String[]> mPermissionResultLauncher;

    private EditText emailAdd, Uname, first_Name, contact_Number, Age, pW, check_Password, barangay, house_number, city, province, zipcode;
    private TextView err_email, err_Username, err_firstName, err_contactNumber, err_Age, err_pW, err_checkPw,
            Tv_resume, tv_filepath, err_address;
    private Spinner userType;
    private RadioGroup gender;
    private RadioButton rbgender;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_form);
        App app = new App(new AppConfiguration.Builder(Appid).build());
        User user = app.currentUser();

        //find viewby id's

        requestPermission(); // request permission

        initCongif(); //access cloudinary
        Button registerButton = findViewById(R.id.registerAccount);
        userType = findViewById(R.id.getUserType);

        emailAdd = findViewById(R.id.getEmailAddress);
        err_email = findViewById(R.id.Error_EmailAddress);

        Uname = findViewById(R.id.getUsername);
        err_Username = findViewById(R.id.Error_Username);

        first_Name = findViewById(R.id.getFirstName);
        err_firstName = findViewById(R.id.Error_FirstName);

        contact_Number = findViewById(R.id.get_contactNumber);
        err_contactNumber = findViewById(R.id.Error_LastName);

        Age = findViewById(R.id.getAge);
        err_Age = findViewById(R.id.Error_Age);

        pW = findViewById(R.id.getPassWord);
        err_pW = findViewById(R.id.Error_password);

        check_Password = findViewById(R.id.CheckPassWord);
        err_checkPw = findViewById(R.id.Error_CheckPassword);

        gender = findViewById(R.id.radioGroup);

        Tv_resume = findViewById(R.id.textView2);
        tv_filepath = findViewById(R.id.show_filepath);
        Button btn_uploadresume = findViewById(R.id.upload_resume);

        barangay = findViewById(R.id.get_barangay);
        house_number = findViewById(R.id.get_houseNumber);
        city = findViewById(R.id.get_city);
        province = findViewById(R.id.get_province);
        zipcode = findViewById(R.id.get_zipCode);

        err_address = findViewById(R.id.error_address);

        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("CourseData");

        //nav bar return icon
        Toolbar toolbar =findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        drawer_reg = findViewById(R.id.drawer_layout_register);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());// end of navbar return icon


        btn_uploadresume.setOnClickListener(view -> getImage());

        barangay.setOnClickListener(v -> barangay.setBackground(getDrawable(R.drawable.border)));
        house_number.setOnClickListener(v -> {
            house_number.setBackground(getDrawable(R.drawable.border));
            err_address.setVisibility(View.INVISIBLE);
        });
        city.setOnClickListener(v -> {
            city.setBackground(getDrawable(R.drawable.border));
            err_address.setVisibility(View.INVISIBLE);
        });
        province.setOnClickListener(v -> {
            province.setBackground(getDrawable(R.drawable.border));
            err_address.setVisibility(View.INVISIBLE);
        });
        zipcode.setOnClickListener(v -> {
            zipcode.setBackground(getDrawable(R.drawable.border));
            err_address.setVisibility(View.INVISIBLE);
        });

        emailAdd.setOnClickListener(v -> {
            emailAdd.setBackground(getDrawable(R.drawable.border));
            err_email.setVisibility(View.INVISIBLE);
        });

        Uname.setOnClickListener(v -> {
            Uname.setBackground(getDrawable(R.drawable.border));
            err_Username.setVisibility(View.INVISIBLE);
        });
        first_Name.setOnClickListener(v -> {
            first_Name.setBackground(getDrawable(R.drawable.border));
            err_firstName.setVisibility(View.INVISIBLE);
        });
        contact_Number.setOnClickListener(v -> {
            contact_Number.setBackground(getDrawable(R.drawable.border));
            err_contactNumber.setVisibility(View.INVISIBLE);
        });
        Age.setOnClickListener(v -> {
            Age.setBackground(getDrawable(R.drawable.border));
            err_Age.setVisibility(View.INVISIBLE);
        });
        pW.setOnClickListener(v -> {
            pW.setBackground(getDrawable(R.drawable.border));
            err_pW.setVisibility(View.INVISIBLE);
        });
        check_Password.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });
        barangay.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });
        house_number.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });
        city.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });
        province.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });
        zipcode.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });
        err_address.setOnClickListener(v -> {
            check_Password.setBackground(getDrawable(R.drawable.border));
            err_checkPw.setVisibility(View.INVISIBLE);
        });

        //spinner
        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (userType.getSelectedItem().toString()) {
                    case "Clients":
                        Tv_resume.setText("Curriculum Vitae:");
                        break;
                    case "Employees":
                        Tv_resume.setText("Government ID:");
                        break;
                    case "Choose type of user.":
                        Log.v("user", "Choose type of user");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });// end of spinner

        registerButton.setOnClickListener(v -> {

            Log.d(TAG, ": " + " button clicked");

            MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d(TAG, "onStart: " + "started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d(TAG, "onStart: " + "uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Log.d(TAG, "onStart: " + "success");
                    tv_filepath.setText(Objects.requireNonNull(resultData.get("secure_url")).toString());
                    String inputEmail = emailAdd.getText().toString();
                    if (tv_filepath.getText().toString().equals(Objects.requireNonNull(resultData.get("secure_url")).toString())) {
                        // checking files
                        int selectedID = gender.getCheckedRadioButtonId();
                        rbgender = findViewById(selectedID);
                        if (selectedID == -1) {
                            Toast.makeText(getApplicationContext(), "No gender is selected", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), rbgender.getText(), Toast.LENGTH_LONG).show();
                        }
                        if (emailAdd.getText().toString().equals("")) {
                            err_email.setVisibility(View.VISIBLE);
                            emailAdd.setBackground(getDrawable(R.drawable.err_border));

                        }
                        if (Uname.getText().toString().equals("")) {
                            err_Username.setVisibility(View.VISIBLE);
                            Uname.setBackground(getDrawable(R.drawable.err_border));
                        }

                        if (first_Name.getText().toString().equals("")) {
                            err_firstName.setVisibility(View.VISIBLE);
                            first_Name.setBackground(getDrawable(R.drawable.err_border));
                        }
                        if (contact_Number.getText().toString().equals("")) {
                            err_contactNumber.setVisibility(View.VISIBLE);
                            contact_Number.setBackground(getDrawable(R.drawable.err_border));
                        }
                        if (Age.getText().toString().equals("")) {
                            err_Age.setVisibility(View.VISIBLE);
                            Age.setBackground(getDrawable(R.drawable.err_border));
                        }
                        if (pW.getText().toString().equals("")) {
                            err_pW.setVisibility(View.VISIBLE);
                            pW.setBackground(getDrawable(R.drawable.err_border));
                        }
                        if (check_Password.getText().toString().equals("")) {
                            err_checkPw.setVisibility(View.VISIBLE);
                            check_Password.setBackground(getDrawable(R.drawable.err_border));
                        }
                        if (barangay.getText().toString().equals("")) {
                            barangay.setBackground(getDrawable(R.drawable.err_border));
                            err_address.setVisibility(View.VISIBLE);
                        }
                        if (city.getText().toString().equals("")) {
                            city.setBackground(getDrawable(R.drawable.err_border));
                            err_address.setVisibility(View.VISIBLE);

                        }
                        if (house_number.getText().toString().equals("")) {
                            house_number.setBackground(getDrawable(R.drawable.err_border));
                            err_address.setVisibility(View.VISIBLE);

                        }
                        if (province.getText().toString().equals("")) {
                            province.setBackground(getDrawable(R.drawable.err_border));
                            err_address.setVisibility(View.VISIBLE);

                        }
                        if (zipcode.getText().toString().equals("")) {
                            zipcode.setBackground(getDrawable(R.drawable.err_border));
                            err_address.setVisibility(View.VISIBLE);
                        } else if (check_Password.getText().toString().contentEquals(pW.getText().toString())) {

                            if (userType.getSelectedItem().toString().equals("Choose type of user.") || emailAdd.getText().toString().equals("") || Uname.getText().toString().equals("") || first_Name.getText().toString().equals("") || contact_Number.getText().toString().equals("") ||
                                    Age.getText().toString().equals("") || pW.getText().toString().equals("") || check_Password.getText().toString().equals("") || zipcode.getText().toString().equals("") || province.getText().toString().equals("") || city.getText().toString().equals("")
                                    || house_number.getText().toString().equals("") || barangay.getText().toString().equals("")) {

                                Toast.makeText(getApplicationContext(), "Fill up all data required.", Toast.LENGTH_LONG).show();
                            } else {
                                if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
                                    err_email.setVisibility(View.VISIBLE);
                                    err_email.setText("Email is not valid.");
                                    emailAdd.setBackground(getDrawable(R.drawable.err_border));
                                } else {

                                    if (userType.getSelectedItem().toString().equals("Clients")) {
                                        findOneAccount();
                                        Intent login = new Intent(Register_Form.this, MainActivity.class);
                                        startActivity(login);
                                        finish();
                                    } else if (userType.getSelectedItem().toString().equals("Employees")) {
                                        findEmployee();
                                        Intent login = new Intent(Register_Form.this, MainActivity.class);
                                        startActivity(login);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Select user type", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        }// end of else if
                    } else {
                        Toast.makeText(getApplicationContext(), "may mali :(", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }
            }).dispatch();

        });

    }// end of onCreate


    @Override
    public void onBackPressed(){
        if (drawer_reg.isDrawerOpen(GravityCompat.START)){
            drawer_reg.closeDrawer(GravityCompat.START);

        }else{
            super.onBackPressed();
            this.finish();
        }

    }

    private void requestPermission() {

        Log.v("Result", "Requesting Permission");

        boolean minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        isReadPermissionGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        boolean isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean isWritePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        isWritePermissionGranted = isWritePermissionGranted || minSDK;

        List<String> permissionRequest = new ArrayList<String>();

        if (!isReadPermissionGranted) {

            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

        }
        if (!isWritePermissionGranted) {

            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!isLocationPermissionGranted) {

            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionRequest.isEmpty()) {

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }

    }

    private void initCongif() {

        config.put("cloud_name", "dsuuylodx");
        config.put("api_key", "643975753735143");
        config.put("api_secret", "8lmznwULEA4x8DwBk5LHU-Hg0xw");
//        config.put("secure", true);

        MediaManager.init(this, config);
    }

    private void getImage() {
        if (isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            selectImage();

        } else {
            requestPermission();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath = data.getData();
                    }
                }
            });

    private void findEmployee() {
        mongoCollection = mongoDatabase.getCollection(userType.getSelectedItem().toString().toLowerCase(Locale.ROOT));
        Document registerAccount = new Document().append("user", userType.getSelectedItem().toString()).append("email", emailAdd.getText().toString()).append(
                "username", Uname.getText().toString()).append("name", first_Name.getText().toString()).append("contactNumber", contact_Number.getText().toString()).append(
                "age", Integer.parseInt(Age.getText().toString())).append("password", pW.getText().toString()).append("address", house_number.getText().toString() + "," +
                barangay.getText().toString() + "," + city.getText().toString() + "," + province.getText().toString()).append("zipcode", zipcode.getText().toString()).append("resume", tv_filepath.getText().toString());
        mongoCollection.insertOne(registerAccount).getAsync(result -> {

            if (result.isSuccess()) {

                Log.v("Data", "Data successfully addedd");
            } else {
                Log.v("Data", "Error:" + result.getError().toString());
            }
        });
    }

    private void findOneAccount() {
        mongoCollection = mongoDatabase.getCollection(userType.getSelectedItem().toString().toLowerCase(Locale.ROOT));
        Document registerAccount = new Document().append("user", userType.getSelectedItem().toString()).append("email", emailAdd.getText().toString()).append(
                "username", Uname.getText().toString()).append("name", first_Name.getText().toString()).append("contactNumber", contact_Number.getText().toString()).append(
                "age", Integer.parseInt(Age.getText().toString())).append("password", pW.getText().toString()).append("address", house_number.getText().toString() + "," +
                barangay.getText().toString() + "," + city.getText().toString() + "," + province.getText().toString()).append("zipcode", zipcode.getText().toString());
        mongoCollection.insertOne(registerAccount).getAsync(result -> {
            if (result.isSuccess()) {
                Log.v("Data", "Data successfully addedd");
            } else {
                Log.v("Data", "Error:" + result.getError().toString());
            }
        });
    }

}