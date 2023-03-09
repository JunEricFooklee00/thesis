package com.test.thesis_application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.fragments.DatePickerFragment;

import org.bson.Document;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class Register_Form extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DrawerLayout drawer_reg;

    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

//    Map config = new HashMap();
    private static final int IMAGE_REQ = 1;
    private static final String TAG = "Upload ###";
    private Uri imagePath;

    //for request permission
    private boolean isReadPermissionGranted = false;
    ActivityResultLauncher<String[]> mPermissionResultLauncher;

    private TextInputEditText TietAge;
    private TextInputLayout email, username, password, name, confirmpassword, contactnumber, address, housenumber, barangay, city, zipcode,province,tilAge;
    private Spinner userType;
    private RadioGroup gender;
    private RadioButton rbgender;
    private Button register;
    private AutoCompleteTextView autoCompleteTextView;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_form);
        App app = new App(new AppConfiguration.Builder(Appid).build());
        User user = app.currentUser();

        requestPermission(); // request permission

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

        //materialspinner
        String[] job = getResources().getStringArray(R.array.userTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Register_Form.this, R.layout.drop_down_item, job);
        autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        //Text Input Layout
        address = findViewById(R.id.TIL_address);
        housenumber = findViewById(R.id.TIL_houseNumber);
        barangay = findViewById(R.id.TIL_Barangay);
        city = findViewById(R.id.TIL_City);
        zipcode = findViewById(R.id.TIL_ZipCode);
        contactnumber = findViewById(R.id.TIL_ContactNumber);
        email = findViewById(R.id.TIL_Email);
        username = findViewById(R.id.TIL_Username);
        password = findViewById(R.id.TIL_Password);
        name = findViewById(R.id.TIL_FullName);
        confirmpassword = findViewById(R.id.TIL_ConfirmPassword);
        tilAge= findViewById(R.id.TIL_Age);
        TietAge= findViewById(R.id.TIET_age);
        province = findViewById(R.id.TIL_province);
        TietAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dp = new DatePickerFragment();
                dp.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validatePassword() | !validatecontactnum() | !validateusername() | !validatefullname() | !validateConfirmPassword() |
                        !validateusertype() |!validatezipcode() |!validateaddress() |!validatehousenumber()|!validatebarangay()|!validatecity()|!validateprovince()|!validateage()) {
                    return;
                }

                Toast.makeText(Register_Form.this, "input", Toast.LENGTH_LONG).show();

                registerAccount();

            }
        });
    }// end of onCreate

    private boolean validateusertype() {
        if (autoCompleteTextView.getText().toString().equals("Clients")) {
            autoCompleteTextView.setError(null);

            return true;
        } else if (autoCompleteTextView.getText().toString().equals("Employees")) {
            return true;
        } else {
            autoCompleteTextView.setError("Please Choose a user type.");
            return false;
        }
    }
    private boolean validateage() {
        String str_age = tilAge.getEditText().getText().toString().trim();
        if (str_age.isEmpty()) {
            tilAge.setError("Field Empty");
            return false;

        } else {
            tilAge.setError(null);
            tilAge.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateemail() {
        String emailinput = email.getEditText().getText().toString().trim();
        if (emailinput.isEmpty()) {
            email.setError("Field Empty");
            return false;

        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()){
            email.setError("Email Invalid");
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateusername() {
        String usernameInput = username.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            username.setError("Field Empty");
            return false;

        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatefullname() {
        String fullnameInput = name.getEditText().getText().toString().trim();
        if (fullnameInput.isEmpty()) {
            name.setError("Field Empty");
            return false;

        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateaddress() {
        String str_address = address.getEditText().getText().toString();
        String str_barangay = barangay.getEditText().getText().toString();
        String str_housenumber = housenumber.getEditText().getText().toString();
        String str_city = city.getEditText().getText().toString();
        String str_zipcode = zipcode.getEditText().getText().toString();

        if (str_address.isEmpty()) {
            address.setError("Field Empty");
            return false;

        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatebarangay() {
        String str_barangay = barangay.getEditText().getText().toString();
        String str_housenumber = housenumber.getEditText().getText().toString();
        String str_city = city.getEditText().getText().toString();
        String str_zipcode = zipcode.getEditText().getText().toString();

        if (str_barangay.isEmpty()) {
            barangay.setError("Field Empty");
            return false;

        } else {
            barangay.setError(null);
            barangay.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatehousenumber() {
        String str_housenumber = housenumber.getEditText().getText().toString();
        String str_city = city.getEditText().getText().toString();
        String str_zipcode = zipcode.getEditText().getText().toString();

        if (str_housenumber.isEmpty()) {
            housenumber.setError("Field Empty");
            return false;

        } else {
            housenumber.setError(null);
            housenumber.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatecity() {
        String str_city = city.getEditText().getText().toString();
        String str_zipcode = zipcode.getEditText().getText().toString();

        if (str_city.isEmpty()) {
            city.setError("Field Empty");
            return false;

        } else {
            city.setError(null);
            city.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatezipcode() {
        String str_zipcode = zipcode.getEditText().getText().toString();

        if (str_zipcode.isEmpty()) {
            zipcode.setError("Field Empty");
            return false;

        } else {
            zipcode.setError(null);
            zipcode.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateprovince() {
        String str_province = province.getEditText().getText().toString();

        if (str_province.isEmpty()) {
            province.setError("Field Empty");
            return false;

        } else {
            province.setError(null);
            province.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String PasswordInput = password.getEditText().getText().toString().trim();
        String confirmPasswordInput = confirmpassword.getEditText().getText().toString().trim();
        if (PasswordInput.isEmpty()) {
            password.setError("Field Empty");
            return false;

        } else if (PasswordInput.equals(confirmPasswordInput)) {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        } else {
            password.setError("Password Doesn't match");
            return false;
        }
    }

    private boolean validateConfirmPassword() {
        String PasswordInput = password.getEditText().getText().toString().trim();
        String confirmPasswordInput = confirmpassword.getEditText().getText().toString().trim();
        if (confirmPasswordInput.isEmpty()) {
            confirmpassword.setError("Field Empty");
            return false;

        } else if (confirmPasswordInput.equals(confirmPasswordInput)) {
            confirmpassword.setError(null);
            confirmpassword.setErrorEnabled(false);
            return true;
        } else {
            confirmpassword.setError("Password Doesn't match");
            return false;
        }
    }

    private boolean validatecontactnum() {
        String ContactNumberInput = contactnumber.getEditText().getText().toString().trim();
        if (ContactNumberInput.isEmpty()) {
            contactnumber.setError("Field Empty");
            return false;

        } else if (ContactNumberInput.length() > 10) {
            contactnumber.setError("Contact Number is Too long");
            return false;

        } else if (ContactNumberInput.length() <= 9) {
            contactnumber.setError("Contact Number is Too short");
            return false;
        } else {
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

//

    private void registerAccount() {
        mongoCollection = mongoDatabase.getCollection(autoCompleteTextView.getText().toString().toLowerCase(Locale.ROOT));
        Document registerAccount = new Document().append("user", autoCompleteTextView.getText().toString())
                .append("email", email.getEditText().getText().toString().trim())
                .append("username", username.getEditText().getText().toString().trim())
                .append("name", name.getEditText().getText().toString().trim())
                .append("contactNumber", contactnumber.getEditText().getText().toString().trim())
                .append("password", password.getEditText().getText().toString().trim())
                .append("age", tilAge.getEditText().getText().toString())
                .append("address", housenumber.getEditText().getText().toString() + "," +
                barangay.getEditText().getText().toString() + "," + city.getEditText().getText().toString() + "," + province.getEditText().getText().toString())
                .append("zipcode", zipcode.getEditText().getText().toString());
//                .append("resume", tv_filepath.getText().toString());
        mongoCollection.insertOne(registerAccount).getAsync(result -> {

            if (result.isSuccess()) {
                Toast.makeText(Register_Form.this, result.get().toString(), Toast.LENGTH_LONG).show();
                Log.v("Data", "Data successfully addedd");
            } else {
                Log.v("Data", "Error:" + result.getError().toString());
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String datepickerstring = DateFormat.getDateInstance(DateFormat.LONG).format(cal.getTime());
        TietAge.setText(datepickerstring);
    }
//

}