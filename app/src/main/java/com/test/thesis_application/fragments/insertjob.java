package com.test.thesis_application.fragments;

import android.app.DatePickerDialog;
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
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.R;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class insertjob extends Fragment {
    //mongodb
    String Appid = "employeems-mcwma";
    private App app;
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    //declare variables
    private AutoCompleteTextView ScopeOfWork, TypeOfWork,actv_unit;
    private TextInputLayout title, area, unit, location, Sdate, expected;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private TextInputEditText TietAge;
    //    private TextInputLayout  ;
    private String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insertjob, container, false);
        // TODO: to pass client id as the primary key of each job
        TietAge= view.findViewById(R.id.TIET_age);
        title = view.findViewById(R.id.TIL_title);
        area = view.findViewById(R.id.TIL_Area);
        unit = view.findViewById(R.id.TIL_unit);
        location = view.findViewById(R.id.TIL_location);
        Sdate = view.findViewById(R.id.TIL_Age);
        expected = view.findViewById(R.id.TIL_expected);

        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("uid");
        }

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
        String[] Siteprep = getResources().getStringArray(R.array.SitePreparation);
        ArrayAdapter<String> siteprep = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, Siteprep);

        String[] CarpWork = getResources().getStringArray(R.array.CarpentryWorks);
        ArrayAdapter<String> carpentry = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, CarpWork);

        String[] MechMetalWork = getResources().getStringArray(R.array.MechanicalMetalWorks);
        ArrayAdapter<String> mechanical = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, MechMetalWork);

        String[] ElecWork = getResources().getStringArray(R.array.ElectricalWorks);
        ArrayAdapter<String> electrical = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, ElecWork);

        String[] TherMoistProtection = getResources().getStringArray(R.array.ThermalandMoistureProtectionWorks);
        ArrayAdapter<String> protection = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, TherMoistProtection);

        String[] PaintWork = getResources().getStringArray(R.array.PaintingWorks);
        ArrayAdapter<String> painting = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, PaintWork);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] ScopeOfJob = getResources().getStringArray(R.array.scopeofwork);
        ArrayAdapter<String> jobadapter = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, ScopeOfJob);
        ScopeOfWork = view.findViewById(R.id.filled_exposed);
        ScopeOfWork.setAdapter(jobadapter);
        ScopeOfWork.setOnItemClickListener((parent, view1, position, id) -> {
            if(ScopeOfWork.getText().toString().equals("Site Preparation")){
                TypeOfWork.setAdapter(siteprep);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Carpentry Works")){
                TypeOfWork.setAdapter(carpentry);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Mechanical/Metal Works")){
                TypeOfWork.setAdapter(mechanical);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Electrical Works")){
                TypeOfWork.setAdapter(electrical);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Painting Works")){
                TypeOfWork.setAdapter(painting);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Thermal and Moisture Protection Works")){

                TypeOfWork.setAdapter(protection);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
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
            if (!validateScopeofWork() | !validatetitle() |!validateexpectedfinishdate() | !validateArea() |!validatesubdiv() |!validateunit() |!validatelocation()
            |! validatestartingdate() |!validateexpectedfinishdate()) {
                return;
            }
            // TODO: add code po here idol
            registerAccount();
        });

        return view;
    }

    private boolean validatetitle(){
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

    private boolean validateScopeofWork() {
        String sow = Objects.requireNonNull(ScopeOfWork.getText()).toString().trim();

        if (sow.isEmpty() ){
            ScopeOfWork.setError("this field cannot be empty.");

            return false;
        } else if( sow.equals("Unit")){
            ScopeOfWork.setError("Please Choose a user type.");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validatesubdiv() {
        String subdivi = Objects.requireNonNull(TypeOfWork.getText()).toString().trim();

        if (subdivi.isEmpty() ){
            TypeOfWork.setError("Please Choose a Sub-division.");

            return false;
        } else if( subdivi.equals("Sub-division.")){
            TypeOfWork.setError("Please Choose a Sub-division.");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validateunit() {
        String strunit = Objects.requireNonNull(actv_unit.getText()).toString().trim();

        if (strunit.isEmpty() ){
            actv_unit.setError("Field cannot be empty.");

            return false;
        } else if( strunit.equals("Unit")){
            actv_unit.setError("Please Choose Unit");
            return false;
        }
        else{
            actv_unit.setError(null);
            return true;
        }
    }
    private boolean validateArea(){
        String strarea = Objects.requireNonNull(area.getEditText()).getText().toString().trim();
        if (strarea.isEmpty()) {
            area.setError("Name cannot be empty.");
            return false;

        } else {
            area.setError(null);
            area.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validatelocation(){
        String strlocation = Objects.requireNonNull(location.getEditText()).getText().toString().trim();
        if (strlocation.isEmpty()) {
            location.setError("Name cannot be empty.");
            return false;

        } else {
            location.setError(null);
            location.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatestartingdate(){
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
    private boolean validateexpectedfinishdate(){
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


    private void registerAccount() {
        ObjectId objectId = new ObjectId(userid);

        // Create a new order document
        Document orderDocument = new Document().append("scopeofwork", ScopeOfWork.getText().toString()).append("jobTitle", Objects.requireNonNull(title.getEditText()).getText().toString().trim())
                .append("Area", Objects.requireNonNull(area.getEditText()).getText().toString().trim() + Objects.requireNonNull(unit.getEditText()).getText().toString().trim()).append("Location", Objects.requireNonNull(location.getEditText()).getText().toString().trim())
                .append("StartingDate", Objects.requireNonNull(Sdate.getEditText()).getText().toString().trim()).append("ExpectedFinishDate", Objects.requireNonNull(expected.getEditText()).getText().toString().trim());
        // Create an array of product documents for the order
//        List<Document> jobs = new ArrayList<Document>();

        orderDocument.put("userId", objectId);
        mongoCollection.insertOne(orderDocument).getAsync(result -> {

            if (result.isSuccess()) {
                Toast.makeText(requireContext(), result.get().toString(), Toast.LENGTH_LONG).show();
                Log.v("Data", "Data successfully addedd");
                getActivity().onBackPressed();
            } else {
                Toast.makeText(requireContext(),"di ko alam",Toast.LENGTH_LONG).show();
                Log.v("Data", "Error:" + result.getError().toString());
            }
        });
    }


    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            String datepickerstring = DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());
            TietAge.setText(datepickerstring);
        }
    };
}
