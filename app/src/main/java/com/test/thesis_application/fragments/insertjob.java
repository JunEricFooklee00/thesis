package com.test.thesis_application.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.R;

import org.bson.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class insertjob extends Fragment {
    private FusedLocationProviderClient fusedLocationClient;

    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    //declare variables
    private AutoCompleteTextView ScopeOfWork, TypeOfWork, actv_unit;
    private TextInputLayout title, area, unit, TILlocation, Sdate, expected, name;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private TextInputEditText TietAge;
    //    private TextInputLayout  ;
    private String userid, nameuser, geocode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insertjob, container, false);
        // TODO: to pass client id as the primary key of each job
        TietAge = view.findViewById(R.id.TIET_age);
        title = view.findViewById(R.id.TIL_title);
        area = view.findViewById(R.id.TIL_Area);
        unit = view.findViewById(R.id.TIL_unit);
        TILlocation = view.findViewById(R.id.TIL_location);

        Sdate = view.findViewById(R.id.TIL_Age);
        expected = view.findViewById(R.id.TIL_expected);
        name = view.findViewById(R.id.TIL_name);
        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("uid");
            nameuser = projectuserid.getString("name");
        }
        name.getEditText().setText(nameuser);
        TietAge.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and show it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateSetListener, mYear, mMonth, mDayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            datePickerDialog.show();
        });

        //materialspinner
        String[] unit = getResources().getStringArray(R.array.unit);
        ArrayAdapter<String> Unit = new ArrayAdapter<>(requireContext(), R.layout.dropdown_unit, unit);
        actv_unit = view.findViewById(R.id.actv_unit);

        actv_unit.setAdapter(Unit);

        String[] CarpWork = getResources().getStringArray(R.array.CarpentryWorks);
        ArrayAdapter<String> carpentry = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, CarpWork);

        String[] MechMetalWork = getResources().getStringArray(R.array.MechanicalMetalWorks);
        ArrayAdapter<String> mechanical = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, MechMetalWork);

        String[] Plumbing = getResources().getStringArray(R.array.PlumbingWorks);
        ArrayAdapter<String> plumbing = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, Plumbing);

        String[] Painting = getResources().getStringArray(R.array.PaintingWorks);
        ArrayAdapter<String> painting = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, Painting);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] ScopeOfJob = getResources().getStringArray(R.array.scopeofwork);
        ArrayAdapter<String> jobadapter = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, ScopeOfJob);
        ScopeOfWork = view.findViewById(R.id.filled_exposed);
        ScopeOfWork.setAdapter(jobadapter);
        ScopeOfWork.setOnItemClickListener((parent, view1, position, id) -> {
            if (ScopeOfWork.getText().toString().equals("Carpentry Works")) {
                TypeOfWork.setAdapter(carpentry);
                actv_unit.setText("Square Meter");

                TypeOfWork.setText("");
//                if(TypeOfWork.getText().equals("Carpentry Works for Main Counter")){
//                }
                Toast.makeText(requireContext(), ScopeOfWork.getText().toString(), Toast.LENGTH_LONG).show();
            } else if (ScopeOfWork.getText().toString().equals("Mechanical/Metal Works")) {
                TypeOfWork.setAdapter(mechanical);
                TypeOfWork.setText("");
                actv_unit.setText("");
                actv_unit.setAdapter(Unit);

                Toast.makeText(requireContext(), ScopeOfWork.getText().toString(), Toast.LENGTH_LONG).show();
            } else if (ScopeOfWork.getText().toString().equals("Plumbing Works")) {
                TypeOfWork.setAdapter(plumbing);
                TypeOfWork.setText("");
                actv_unit.setText("Linear Meter");


                Toast.makeText(requireContext(), ScopeOfWork.getText().toString(), Toast.LENGTH_LONG).show();
            } else if (ScopeOfWork.getText().toString().equals("Painting Works")) {
                TypeOfWork.setAdapter(painting);
                TypeOfWork.setText("");
                actv_unit.setText("Square Meter");
                Toast.makeText(requireContext(), ScopeOfWork.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        TILlocation.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation();
                TILlocation.getEditText().setText(geocode);
            }
        });

        Button submit = view.findViewById(R.id.btn_add_job);

        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("JobOrder");
        mongoCollection = mongoDatabase.getCollection("joborders");

        submit.setOnClickListener(v -> {
            if (!validateScopeofWork() | !validatetitle() | !validateexpectedfinishdate() | !validateArea() | !validatesubdiv() | !validateunit() | !validatelocation()
                    | !validatestartingdate() | !validateexpectedfinishdate() | !validatename() | !validateInputArea() |!validateinputDate()) {
                return;
            }
            // TODO: add code po here idol
            insertjob();
        });
        return view;
    }

    private void getlocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    getAddress(location);
                }
            });
        }
    }

    private void getAddress(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                    sb.append(address.getAddressLine(i)).append("\n");
//                }
//                tvAddress.setText(sb.toString());
//                 geocode = sb.toString();
                geocode = address.getAddressLine(0);
            } else {
                geocode = "Cannot get location";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validatetitle() {
        String strtitle = Objects.requireNonNull(title.getEditText()).getText().toString().trim();
        if (strtitle.isEmpty()) {
            title.setError("Username cannot be empty.");
            return false;

        } else {
            title.setError(null);
            title.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatename() {
        String strname = Objects.requireNonNull(name.getEditText()).getText().toString().trim();
        if (strname.isEmpty()) {
            name.setError("Username cannot be empty.");
            return false;

        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateScopeofWork() {
        String sow = Objects.requireNonNull(ScopeOfWork.getText()).toString().trim();

        if (sow.isEmpty()) {
            ScopeOfWork.setError("this field cannot be empty.");

            return false;
        } else if (sow.equals("Unit")) {
            ScopeOfWork.setError("Please Choose a user type.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatesubdiv() {
        String subdivi = Objects.requireNonNull(TypeOfWork.getText()).toString().trim();

        if (subdivi.isEmpty()) {
            TypeOfWork.setError("Please Choose a Sub-division.");

            return false;
        } else if (subdivi.equals("Sub-division.")) {
            TypeOfWork.setError("Please Choose a Sub-division.");
            return false;
        } else {
            TypeOfWork.setError(null);

            return true;
        }
    }

    private boolean validateunit() {
        String strunit = Objects.requireNonNull(actv_unit.getText()).toString().trim();

        if (strunit.isEmpty()) {
            actv_unit.setError("Field cannot be empty.");

            return false;
        } else if (strunit.equals("Unit")) {
            actv_unit.setError("Please Choose Unit");
            return false;
        } else {
            actv_unit.setError(null);
            return true;
        }
    }

    private boolean validateArea() {
        String strarea = Objects.requireNonNull(area.getEditText()).getText().toString().trim();
        String strunit = Objects.requireNonNull(actv_unit.getText()).toString();

        if (strarea.isEmpty()) {
            area.setError("Name cannot be empty.");
            return false;
        } else {
            area.setError(null);
            area.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateInputArea() {
        String strArea = Objects.requireNonNull(area.getEditText()).getText().toString().trim();
//        String strUnit = Objects.requireNonNull(actv_unit.getText()).toString().trim();

        if (TypeOfWork.getText().toString().equals("B2 - Carpentry Works for Main Counter")) {
            try {
                double value = Double.parseDouble(strArea);
                if (value >= 4.84 && value <= 9.61) {
                    area.setError(null);
                    return true;
                } else {
                    area.setError("Value must be between 4.84 and 9.61");
                    return false;
                }
            } catch (NumberFormatException e) {
                area.setError("Invalid value");
                return false;
            }
        } else if (TypeOfWork.getText().toString().equals("D2 - Pipeline and Fixture Installation")) {
            try {
                double value = Double.parseDouble(strArea);
                if (value >= 1 && value <= 12) {
                    area.setError(null);
                    return true;
                } else {
                    area.setError("Value must be between 1 and 12");
                    return false;
                }
            } catch (NumberFormatException e) {
                area.setError("Invalid value");
                return true;
            }
        } else if (TypeOfWork.getText().toString().equals("D3 - Drainage Pipeline Installation")) {
            try {
                double value = Double.parseDouble(strArea);
                if (value >= 4 && value <= 12.2) {
                    area.setError(null);
                    return true;
                } else {
                    area.setError("Value must be between 4 and 12.2");
                    return false;
                }
            } catch (NumberFormatException e) {
                area.setError("Invalid value");
                return true;
            }
        } else if (TypeOfWork.getText().toString().equals("G1 - Plain Concrete Surfaces (Surface Prep - Primer - Finish Coat)")) {
            try {
                double value = Double.parseDouble(strArea);
                if (value >= 9.95 && value <= 152.06) {
                    area.setError(null);
                    return true;

                } else {
                    area.setError("Value must be between 9.95 and 152.06");
                    return false;
                }
            } catch (NumberFormatException e) {
                area.setError("Invalid value");
                return false;
            }
        } else {
            // handle case where unit is not recognized
            return true;
        }
    }

    private boolean validateinputDate() {
        String strfinishdate = Objects.requireNonNull(expected.getEditText()).getText().toString().trim();
//        String strUnit = Objects.requireNonNull(actv_unit.getText()).toString().trim();

        if (TypeOfWork.getText().toString().equals("B2 - Carpentry Works for Main Counter")) {
            try {
                double value = Double.parseDouble(strfinishdate);
                if (value >= 1 && value <= 3) {
                    expected.setError(null);
                    return true;
                } else {
                    expected.setError("Value must be between 1 and 3");
                    return false;
                }
            } catch (NumberFormatException e) {
                expected.setError("Invalid value");
                return false;
            }
        } else if (TypeOfWork.getText().toString().equals("D2 - Pipeline and Fixture Installation")) {
            try {
                double value = Double.parseDouble(strfinishdate);
                if (value >= 1 && value <= 2) {
                    expected.setError(null);
                    return true;
                } else {
                    expected.setError("Value must be between 1 and 2");
                    return false;
                }
            } catch (NumberFormatException e) {
                area.setError("Invalid value");
                return true;
            }
        } else if (TypeOfWork.getText().toString().equals("D3 - Drainage Pipeline Installation")) {
            try {
                double value = Double.parseDouble(strfinishdate);
                if (value >= 4 && value <= 12.2) {
                    expected.setError(null);
                    return true;
                } else {
                    expected.setError("Value must be between 1 and 2");
                    return false;
                }
            } catch (NumberFormatException e) {
                expected.setError("Invalid value");
                return true;
            }
        } else if (TypeOfWork.getText().toString().equals("G1 - Plain Concrete Surfaces (Surface Prep - Primer - Finish Coat)")) {
            try {
                double value = Double.parseDouble(strfinishdate);
                if (value >=1  && value <= 4) {
                    expected.setError(null);
                    return true;

                } else {
                    expected.setError("Value must be between 9.95 and 152.06");
                    return false;
                }
            } catch (NumberFormatException e) {
                expected.setError("Invalid value");
                return false;
            }
        } else {
            // handle case where unit is not recognized
            return true;
        }
    }

    public boolean validatelocation() {
        String strlocation = Objects.requireNonNull(TILlocation.getEditText()).getText().toString().trim();
        if (strlocation.isEmpty()) {
            TILlocation.setError("Name cannot be empty.");
            return false;

        } else {
            TILlocation.setError(null);
            TILlocation.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatestartingdate() {
        String strSdate = Objects.requireNonNull(Sdate.getEditText()).getText().toString().trim();
        if (strSdate.isEmpty()) {
            Sdate.setError("Please choose a starting date.");
            return false;

        } else {
            Sdate.setError(null);
            Sdate.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateexpectedfinishdate() {
        String strfinishdate = Objects.requireNonNull(expected.getEditText()).getText().toString().trim();
        if (strfinishdate.isEmpty()) {
            expected.setError("Please choose a starting date.");
            return false;

        } else {
            expected.setError(null);
            expected.setErrorEnabled(false);
            return true;
        }
    }

    private void insertjob() {
        //Create a new order document
        Document orderDocument = new Document()
                .append("idUser", userid)
                .append("name", name.getEditText().getText().toString().trim())
                .append("TypeOfWork", TypeOfWork.getText().toString())
                .append("jobTitle", Objects.requireNonNull(title.getEditText()).getText().toString().trim())
                .append("Area", Objects.requireNonNull(area.getEditText()).getText().toString().trim())
                .append("Unit", Objects.requireNonNull(unit.getEditText()).getText().toString().trim())
                .append("Location", Objects.requireNonNull(TILlocation.getEditText()).getText().toString().trim())
                .append("StartingDate", Objects.requireNonNull(Sdate.getEditText()).getText().toString().trim())
                .append("ExpectedFinishDate", Objects.requireNonNull(expected.getEditText()).getText().toString().trim())
                .append("created", new Date());
        //Create an array of product documents for the order
        //List<Document> jobs = new ArrayList<Document>();

        //orderDocument.put("idUser", objectId);
        mongoCollection.insertOne(orderDocument).getAsync(result -> {

            if (result.isSuccess()) {
                Toast.makeText(requireContext(), result.get().toString(), Toast.LENGTH_LONG).show();
                Log.v("Data", "Data successfully addedd");
                getActivity().onBackPressed();
            } else {
                Toast.makeText(requireContext(), "di ko alam", Toast.LENGTH_LONG).show();
                Log.v("Data", "Error:" + result.getError().toString());
            }
        });


    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String datepickerstring = DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());
            TietAge.setText(datepickerstring);
        }
    };
}
