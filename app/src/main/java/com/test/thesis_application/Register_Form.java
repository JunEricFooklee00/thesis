package com.test.thesis_application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.textfield.TextInputLayout;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private TextInputLayout email, username, password, name, confirmpassword, contactnumber;
    private Spinner userType;
    private RadioGroup gender;
    private RadioButton rbgender;
    private Button register;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_form);
        App app = new App(new AppConfiguration.Builder(Appid).build());
        User user = app.currentUser();

        requestPermission(); // request permission
//        initCongif(); //access cloudinary

        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("CourseData");

        //nav bar return icon
        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        drawer_reg = findViewById(R.id.drawer_layout_register);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());// end of navbar return icon

//
//        btn_uploadresume.setOnClickListener(view -> getImage());

        //button
        register = findViewById(R.id.btn_register);


        //Text Input Layout
        contactnumber = findViewById(R.id.TIL_ContactNumber);
        email = findViewById(R.id.TIL_Email);
        username = findViewById(R.id.TIL_Username);
        password = findViewById(R.id.TIL_Password);
        name = findViewById(R.id.TIL_FullName);
        confirmpassword = findViewById(R.id.TIL_ConfirmPassword);

//        //spinner
        userType = findViewById(R.id.getUserType);
//        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (userType.getSelectedItem().toString()) {
//                    case "Clients":
//
//
//                        break;
//                    case "Employees":
//
//                        break;
//                    case "Choose type of user.":
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });//end of spinner

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validatePassword()| !validatecontactnum() | !validateusername() | !validatefullname()| !validateConfirmPassword() | !validateusertype()){
                    return;
                }

                Toast.makeText(Register_Form.this,"input",Toast.LENGTH_LONG).show();

                registerAccount();
//                String input = "Email: " + email.getEditText().getText().toString();
//                input += "\n";
//                input += "Password: " +password.getEditText().getText().toString();
//                input += "\n";
//                input += "Contact Number: "+contactnumber.getEditText().getText().toString();
//                Toast.makeText(Register_Form.this,input,Toast.LENGTH_LONG).show();

            }
        });
    }// end of onCreate
    private boolean validateusertype(){
        if (userType.getSelectedItem().toString().equals("Clients")){
            return true;
        }else if (userType.getSelectedItem().toString().equals("Employees")){
            return true;
        } else {
            Toast.makeText(Register_Form.this,"Please Select Valid UserType.",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean validateemail() {
        String emailinput = email.getEditText().getText().toString().trim();
        if (emailinput.isEmpty()){
            email.setError("Field Empty");
            return false;

        }else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateusername() {
        String usernameInput = username.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()){
            username.setError("Field Empty");
            return false;

        }else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatefullname() {
        String fullnameInput = name.getEditText().getText().toString().trim();
        if (fullnameInput.isEmpty()){
            name.setError("Field Empty");
            return false;

        }else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String PasswordInput = password.getEditText().getText().toString().trim();
        String confirmPasswordInput = confirmpassword.getEditText().getText().toString().trim();
        if (PasswordInput.isEmpty()){
            password.setError("Field Empty");
            return false;

        }else if (PasswordInput.equals(confirmPasswordInput)){
            password.setError(null);
            password.setErrorEnabled(false);
            return  true;
        }else {
            password.setError("Password Doesn't match");
            return false;
        }
    }
    private boolean validateConfirmPassword() {
        String PasswordInput = password.getEditText().getText().toString().trim();
        String confirmPasswordInput = confirmpassword.getEditText().getText().toString().trim();
        if (confirmPasswordInput.isEmpty()){
            confirmpassword.setError("Field Empty");
            return false;

        }else if (confirmPasswordInput.equals(confirmPasswordInput)){
            confirmpassword.setError(null);
            confirmpassword.setErrorEnabled(false);
            return  true;
        }else {
            confirmpassword.setError("Password Doesn't match");
            return false;
        }
    }
    private boolean validatecontactnum() {
        String ContactNumberInput = contactnumber.getEditText().getText().toString().trim();
        if (ContactNumberInput.isEmpty()){
            contactnumber.setError("Field Empty");
            return false;

        }else if (ContactNumberInput.length() >10){
            contactnumber.setError("Contact Number is Too long");
            return false;

        }else if (ContactNumberInput.length()<=9){
            contactnumber.setError("Contact Number is Too short");
            return false;
        }
        else {
            contactnumber.setError(null);
            contactnumber.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer_reg.isDrawerOpen(GravityCompat.START)) {
            drawer_reg.closeDrawer(GravityCompat.START);

        } else {
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

//    private void initCongif() {
//
//        config.put("cloud_name", "dsuuylodx");
//        config.put("api_key", "643975753735143");
//        config.put("api_secret", "8lmznwULEA4x8DwBk5LHU-Hg0xw");
////        config.put("secure", true);
//
//        MediaManager.init(this, config);
//    }
//
////    private void getImage() {
////        if (isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////
////            selectImage();
////
////        } else {
////            requestPermission();
////        }
////    }
////
////    private void selectImage() {
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        someActivityResultLauncher.launch(intent);
////    }
////
////    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
////            new ActivityResultContracts.StartActivityForResult(),
////            new ActivityResultCallback<ActivityResult>() {
////                @Override
////                public void onActivityResult(ActivityResult result) {
////                    if (result.getResultCode() == Activity.RESULT_OK) {
////                        // There are no request codes
////                        Intent data = result.getData();
////                        imagePath = data.getData();
////                    }
////                }
////            });

    private void registerAccount() {
        mongoCollection = mongoDatabase.getCollection(userType.getSelectedItem().toString().toLowerCase(Locale.ROOT));
        Document registerAccount = new Document().append("user", userType.getSelectedItem().toString())
                .append("email", email.getEditText().getText().toString().trim())
                .append("username",username.getEditText().getText().toString().trim())
                .append("name", name.getEditText().getText().toString().trim())
                .append("contactNumber", contactnumber.getEditText().getText().toString().trim())
                .append("password", password.getEditText().getText().toString().trim());
//                .append("age", Integer.parseInt(Age.getText().toString()))
//                .append("address", house_number.getText().toString() + "," +
//                barangay.getText().toString() + "," + city.getText().toString() + "," + province.getText().toString())
//                .append("zipcode", zipcode.getText().toString()).append("resume", tv_filepath.getText().toString());
        mongoCollection.insertOne(registerAccount).getAsync(result -> {

            if (result.isSuccess()) {
                Toast.makeText(Register_Form.this,result.get().toString(),Toast.LENGTH_LONG).show();
                Log.v("Data", "Data successfully addedd");
            } else {
                Log.v("Data", "Error:" + result.getError().toString());
            }
        });
    }
//

}