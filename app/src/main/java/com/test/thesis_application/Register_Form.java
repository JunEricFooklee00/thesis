package com.test.thesis_application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import androidx.fragment.app.DialogFragment;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.test.thesis_application.fragments.DatePickerFragment;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class Register_Form extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DrawerLayout drawer_reg;

    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    //    Map config = new HashMap();
    private Uri imagePath, imagePath2;

    private boolean isReadPermissionGranted = false;
    ActivityResultLauncher<String[]> mPermissionResultLauncher;

    private TextInputEditText TietAge;
    private TextInputEditText TietPicture;
    private TextInputEditText Tietresume;
    private TextInputLayout email, username, password, name, confirmpassword, address, level, contactnumber, housenumber, barangay, city, zipcode, province, tilAge, picture, resume;

    private Button register;
    private AutoCompleteTextView autoCompleteTextView, autoCompleteTextViewgender, jobselect;
    String hashedPassword;
    TextInputLayout usertype;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_form);
        app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();

        requestPermission(); // request permission

        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("CourseData");

        jobselect = findViewById(R.id.ACT_Jobtype);
        //nav bar return icon
        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        drawer_reg = findViewById(R.id.drawer_layout_register);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());// end of navbar return icon

        register = findViewById(R.id.btn_register);

        //materialspinner
        String[] job = getResources().getStringArray(R.array.userTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Register_Form.this, R.layout.drop_down_item, job);
        autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        String[] jobtypesz = getResources().getStringArray(R.array.jobs);
        ArrayAdapter<String> jobadapter = new ArrayAdapter<>(Register_Form.this, R.layout.drop_down_item, jobtypesz);
        jobselect.setAdapter(jobadapter);
        //materialspinner
        String[] gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> genderadapter = new ArrayAdapter<>(Register_Form.this, R.layout.drop_down_item_gender, gender);
        autoCompleteTextViewgender = findViewById(R.id.filled_exposedgender);
        autoCompleteTextViewgender.setAdapter(genderadapter);

        usertype = findViewById(R.id.TIL_SPINNER);
        level = findViewById(R.id.TIL_level);
        level.setVisibility(View.INVISIBLE);
//        String selectedUserType = usertype.getEditText().toString();
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedUserType = autoCompleteTextView.getText().toString();
            Log.d("Register_Form", "AutoCompleteTextView value: " + selectedUserType);

            if (selectedUserType.equals("Clients")) {
                level.setVisibility(View.INVISIBLE);
            } else if (selectedUserType.equals("Employees")) {
                level.setVisibility(View.VISIBLE);
            } else {
                Log.d("Register_Form", "AutoCompleteTextView value: " + selectedUserType);
            }
        });

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
        tilAge = findViewById(R.id.TIL_Age);
        TietAge = findViewById(R.id.TIET_age);
        province = findViewById(R.id.TIL_province);
        picture = findViewById(R.id.TIL_picture);
        TietPicture = findViewById(R.id.TIET_Picture);
        resume = findViewById(R.id.TIL_resume);
        Tietresume = findViewById(R.id.TIET_resume);
        TextInputEditText TIETContactNum = findViewById(R.id.TIET_ContactNum);
        TextInputEditText TIETzipcode = findViewById(R.id.TIET_zipcode);


        //inputfilter
        InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()};
        InputFilter[] zipcodefilter = new InputFilter[]{new InputFilter.LengthFilter(4), new InputFilter.AllCaps()};
        TIETContactNum.setFilters(filters);
        TIETzipcode.setFilters(zipcodefilter);
        TietPicture.setOnClickListener(view -> {
            getImage();
        });
        Tietresume.setOnClickListener(v -> {
            getresume();
        });
        TietAge.setOnClickListener(v -> {
            DialogFragment dp = new DatePickerFragment();
            dp.show(getSupportFragmentManager(), "Date Picker");
        });

        register.setOnClickListener(v -> {
            if (!validateemail() | !validatePassword() | !validateAvatar() | !validatecontactnum() | !validateusername() | !validatefullname() | !validateConfirmPassword() | !validategender() |
                    !validateusertype() | !validatezipcode() | !validateaddress() | !validatehousenumber() | !validatebarangay() | !validatecity() | !validateprovince() | !validateage() | !validateImage() |!validateJobType()) {
                return;
            }
            register.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    register.setEnabled(true);
                }
            }, 10000); // Delay in milliseconds, adjust as needed

            MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {

                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {

                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Toast.makeText(getApplicationContext(), "Success1", Toast.LENGTH_LONG).show();
                    TietPicture.setText(resultData.get("secure_url").toString());

                    MediaManager.get().upload(imagePath2).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {

                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            Tietresume.setText(resultData.get("secure_url").toString());
                            registerAccount();
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {

                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }
                    }).dispatch();


                }

                @Override
                public void onError(String requestId, ErrorInfo error) {

                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();

        });
    }// end of onCreate

    private String hashPassword(String input) {
        // Define the strength of the hashing algorithm
        int strength = 10;

        // Generate a salt value
        String salt = BCrypt.gensalt(strength);

        // Hash the password using the generated salt
        String hashedPassword = BCrypt.hashpw(input, salt);

        return hashedPassword;
    }


    private boolean validateJobType() {
        String sow = Objects.requireNonNull(jobselect.getText()).toString().trim();

        if (sow.isEmpty()) {
            jobselect.setError("this field cannot be empty.");

            return false;
        } else if (sow.equals("Unit")) {
            jobselect.setError("Please Choose a user type.");
            return false;
        } else {
            autoCompleteTextView.setError("Please Select A Job Title.");

            return true;
        }
    }
    private boolean validateusertype() {
        if (autoCompleteTextView.getText().toString().equals("Clients")) {
            autoCompleteTextView.setError(null);

            return true;
        } else if (autoCompleteTextView.getText().toString().equals("Employees")) {
            autoCompleteTextViewgender.setError(null);

            return true;
        } else {
            autoCompleteTextView.setError("Please Choose a user type.");
            return false;
        }
    }

    private boolean validategender() {
        if (autoCompleteTextViewgender.getText().toString().equals("Male")) {
            autoCompleteTextViewgender.setError(null);

            return true;
        } else if (autoCompleteTextViewgender.getText().toString().equals("Female")) {
            autoCompleteTextViewgender.setError(null);

            return true;
        } else {
            autoCompleteTextViewgender.setError("Please Choose a gender");
            return false;
        }
    }

    private boolean validateage() {
        String str_age = Objects.requireNonNull(tilAge.getEditText()).getText().toString().trim();
        if (str_age.isEmpty()) {
            tilAge.setError("Select Birth date.");
            return false;

        } else {
            tilAge.setError(null);
            tilAge.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateemail() {

        String emailinput = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
        if (emailinput.isEmpty()) {
            email.setError("Email cannot be empty.");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()) {
            email.setError("Email Invalid");
            return false;
        } else {

            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateusername() {
        String usernameInput = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
        if (usernameInput.isEmpty()) {
            username.setError("Username cannot be empty.");
            return false;

        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatefullname() {
        String fullnameInput = Objects.requireNonNull(name.getEditText()).getText().toString().trim();
        if (fullnameInput.isEmpty()) {
            name.setError("Name cannot be empty.");
            return false;

        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateaddress() {
        String str_address = Objects.requireNonNull(address.getEditText()).getText().toString();
        if (str_address.isEmpty()) {
            address.setError("Address cannot be empty.");
            return false;

        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatebarangay() {
        String str_barangay = Objects.requireNonNull(barangay.getEditText()).getText().toString();
        if (str_barangay.isEmpty()) {
            barangay.setError("Barangay cannot be empty.");
            return false;

        } else {
            barangay.setError(null);
            barangay.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatehousenumber() {
        String str_housenumber = Objects.requireNonNull(housenumber.getEditText()).getText().toString();
        if (str_housenumber.isEmpty()) {
            housenumber.setError("House and lot number cannot be empty.");
            return false;

        } else {
            housenumber.setError(null);
            housenumber.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatecity() {
        String str_city = Objects.requireNonNull(city.getEditText()).getText().toString();

        if (str_city.isEmpty()) {
            city.setError("City cannot be empty.");
            return false;

        } else {
            city.setError(null);
            city.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatezipcode() {
        String str_zipcode = Objects.requireNonNull(zipcode.getEditText()).getText().toString().trim();
        if (str_zipcode.isEmpty()) {
            zipcode.setError("Contact number cannot be empty");
            return false;

        } else if (str_zipcode.length() > 4) {
            zipcode.setError("Zip Code is Too long");
            return false;

        } else if (str_zipcode.length() <= 3) {
            zipcode.setError("Contact Number is Too short");
            return false;
        } else {
            zipcode.setError(null);
            zipcode.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateprovince() {
        String str_province = Objects.requireNonNull(province.getEditText()).getText().toString();

        if (str_province.isEmpty()) {
            province.setError("Province cannot be empty.");
            return false;

        } else {
            province.setError(null);
            province.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String PasswordInput = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
        String confirmPasswordInput = Objects.requireNonNull(confirmpassword.getEditText()).getText().toString().trim();
        if (PasswordInput.isEmpty()) {
            password.setError("Password cannot be empty");
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
        String PasswordInput = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
        String confirmPasswordInput = Objects.requireNonNull(confirmpassword.getEditText()).getText().toString().trim();
        if (confirmPasswordInput.isEmpty()) {
            confirmpassword.setError("Password cannot be empty");
            return false;

        } else if (confirmPasswordInput.equals(PasswordInput)) {
            confirmpassword.setError(null);
            confirmpassword.setErrorEnabled(false);
            return true;
        } else {
            confirmpassword.setError("Confirm Password Doesn't match");
            return false;
        }
    }

    private boolean validatecontactnum() {
        String ContactNumberInput = Objects.requireNonNull(contactnumber.getEditText()).getText().toString().trim();
        if (ContactNumberInput.isEmpty()) {
            contactnumber.setError("Contact number cannot be empty");
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

    private void getImage() {
        if (isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            selectImage();

        } else {
            requestPermission();
        }
    }

    private void getresume() {
        if (isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            selectResume();

        } else {
            requestPermission();
        }
    }

    private boolean validateImage() {
        String str_picture = Objects.requireNonNull(picture.getEditText()).getText().toString().trim();
        if (str_picture.isEmpty()) {
            picture.setError("Image cannot be empty");
            return false;

        } else {
            picture.setError(null);
            return true;
        }
    }

    private boolean validateAvatar() {
        String strResume = Objects.requireNonNull(picture.getEditText()).getText().toString().trim();
        if (strResume.isEmpty()) {
            resume.setError("Image cannot be empty");
            return false;

        } else {
            resume.setError(null);

            return true;
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
                        ImageView img = findViewById(R.id.AvaterPreview);
                        imagePath = data.getData();
                        String imagetxt = data.getData().toString();
                        TietPicture.setText(imagetxt);
                        Picasso.get().load(imagePath).transform(new CropCircleTransformation()).into(img);
                    }
                }
            });

    private void selectResume() {
        Intent resume = new Intent();
        resume.setType("image/*");
        resume.setAction(Intent.ACTION_GET_CONTENT);
        resumeActivityResultLauncher.launch(resume);
    }

    ActivityResultLauncher<Intent> resumeActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        ImageView img2 = findViewById(R.id.ResumePreview);

                        imagePath2 = data.getData();
                        String imagetxt = data.getData().toString();
                        Tietresume.setText(imagetxt);
                        Picasso.get().load(imagePath2).into(img2);

                    }
                }
            });

    private void requestPermission() {

        Log.v("Result", "Requesting Permission");

        boolean minSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        //for request permission
        boolean isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

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


    private void registerAccount() {

        String input = password.getEditText().getText().toString().trim();

        String CN = contactnumber.getEditText().getText().toString();
        Double contactnum = Double.parseDouble(CN);
        String hash = hashPassword(input);
        // do something with the hash
        String depende = autoCompleteTextView.getText().toString().toLowerCase(Locale.ROOT);
        if (depende.equals("employees")){
            mongoCollection = mongoDatabase.getCollection("employees");
            Document registerAccount = new Document().append("user", autoCompleteTextView.getText().toString())
                    .append("jobType",jobselect.getText().toString())
                    .append("email", Objects.requireNonNull(email.getEditText()).getText().toString().trim())
                    .append("username", Objects.requireNonNull(username.getEditText()).getText().toString().trim())
                    .append("name", Objects.requireNonNull(name.getEditText()).getText().toString().trim())
                    .append("contactNumber", contactnum)
                    .append("password", Objects.requireNonNull(hash))
                    .append("gender", autoCompleteTextViewgender.getText().toString())
                    .append("birthday", Objects.requireNonNull(tilAge.getEditText()).getText().toString())
                    .append("address", Objects.requireNonNull(housenumber.getEditText()).getText().toString() + ", " +
                            Objects.requireNonNull(barangay.getEditText()).getText().toString() + ", " + Objects.requireNonNull(city.getEditText()).getText().toString() + ", " + Objects.requireNonNull(province.getEditText()).getText().toString())
                    .append("zipcode", Objects.requireNonNull(zipcode.getEditText()).getText().toString()).append("avatar", TietPicture.getText().toString()).append("resume", Tietresume.getText().toString()).append("created", new Date());

            mongoCollection.insertOne(registerAccount).getAsync(result -> {
                if (result.isSuccess()) {
                    Toast.makeText(Register_Form.this, result.get().toString(), Toast.LENGTH_LONG).show();
                    Log.v("Data", "Data successfully addedd");
                    Intent intent = new Intent(Register_Form.this, splashScreen.class);
                    startActivity(intent);
                } else {
                    Log.v("Mongodb", "Error:" + result.getError().toString());
                }
            });
        } else if (depende.equals("clients")) {
            mongoCollection = mongoDatabase.getCollection("clients");
            Document registerAccount = new Document().append("user", autoCompleteTextView.getText().toString())
                    .append("email", Objects.requireNonNull(email.getEditText()).getText().toString().trim())
                    .append("username", Objects.requireNonNull(username.getEditText()).getText().toString().trim())
                    .append("name", Objects.requireNonNull(name.getEditText()).getText().toString().trim())
                    .append("contactNumber", contactnum)
                    .append("password", Objects.requireNonNull(hash))
                    .append("gender", autoCompleteTextViewgender.getText().toString())
                    .append("birthday", Objects.requireNonNull(tilAge.getEditText()).getText().toString())
                    .append("address", Objects.requireNonNull(housenumber.getEditText()).getText().toString() + ", " +
                            Objects.requireNonNull(barangay.getEditText()).getText().toString() + ", " + Objects.requireNonNull(city.getEditText()).getText().toString() + ", " + Objects.requireNonNull(province.getEditText()).getText().toString())
                    .append("zipcode", Objects.requireNonNull(zipcode.getEditText()).getText().toString()).append("avatar", TietPicture.getText().toString()).append("resume", Tietresume.getText().toString()).append("created", new Date());

            mongoCollection.insertOne(registerAccount).getAsync(result -> {
                if (result.isSuccess()) {
                    Toast.makeText(Register_Form.this, result.get().toString(), Toast.LENGTH_LONG).show();
                    Log.v("Data", "Data successfully addedd");
                    Intent intent = new Intent(Register_Form.this, splashScreen.class);
                    startActivity(intent);
                } else {
                    Log.v("Mongodb", "Error:" + result.getError().toString());
                }
            });
        }



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String datepickerstring = DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());
        TietAge.setText(datepickerstring);
    }


}