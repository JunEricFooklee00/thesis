package com.test.thesis_application.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.EmployeeAdapter;
import com.test.thesis_application.R;
import com.test.thesis_application.laborAdapter;
import com.test.thesis_application.ml.B2CarpentryWorks;
import com.test.thesis_application.ml.D2Pipeline;
import com.test.thesis_application.ml.D3DrainagePipeline;
import com.test.thesis_application.ml.G1plainconcrete;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;


public class jobsinfo extends Fragment implements CompoundButton.OnCheckedChangeListener {
    Button stringbuilder;
    String workertype;
    TextInputLayout output, skilled, unskilled, suggested;
    TextView cv4tv;
    TextView TV_jobTitle;
    TextView TV_jobid;
    TextView TV_scope;
    TextView TV_area;
    TextView TV_location;
    TextView TV_startdate;
    TextView titleskilled;
    TextView titleUnskilled;
    TextView TV_expecteddate;
    TextView Tv_contactNumber;
    String jobtitle, userId, id, ScopeofWork, area, Location, ExpectedFinishDate, Startingdate;
    Double contactNumber;
    Button accept;
    int outputString;
    CardView cv4, cv5;
    String jsonResult2;
    String jsonResult;
    String nameuser;

    TextView pangalan;
    EmployeeAdapter skilledAdapter;
    laborAdapter unskilledAdapter;
    RecyclerView recyclerView, recyclerView2;
    ImageView del;
    private List<JSONObject> mCheckedItems = new ArrayList<>();
    String Appid = "employeems-mcwma";
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    TextView Unitval ;
    String Unitstr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobsinfo, container, false);

        suggested = view.findViewById(R.id.TIL_Suggested);
        accept = view.findViewById(R.id.Accept);
        TV_jobTitle = view.findViewById(R.id.textView_jobTitle);
        TV_jobid = view.findViewById(R.id.tv_jobID);
        TV_scope = view.findViewById(R.id.tv_scope);
        TV_area = view.findViewById(R.id.tv_Area);
        TV_location = view.findViewById(R.id.tv_location);
        TV_startdate = view.findViewById(R.id.tv_startingdate);
        TV_expecteddate = view.findViewById(R.id.tv_expecteddate);
        output = view.findViewById(R.id.TIL_forcasted);
        cv4 = view.findViewById(R.id.cardview4);
        cv5 = view.findViewById(R.id.cardView5);
        del = view.findViewById(R.id.delete);
        skilled = view.findViewById(R.id.TIL_skilled);
        unskilled = view.findViewById(R.id.TIL_unskilled);
        pangalan = view.findViewById(R.id.tv_name);
        stringbuilder = view.findViewById(R.id.accept);
        titleskilled = view.findViewById(R.id.skilledTitle);
        titleUnskilled = view.findViewById(R.id.unskilledtitle);
        Tv_contactNumber = view.findViewById(R.id.Tv_contactNumber);
        Unitval = view.findViewById(R.id.Tv_unit);

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
            ExpectedFinishDate = data.getString("expectedfinishdate");
            jobtitle = data.getString("jobtitle");
            Startingdate = data.getString("startingdate");
        }

        Log.v("doublevaluedapat",contactNumber.toString());
        DecimalFormat contactformat = new DecimalFormat("#");
        contactformat.setMaximumFractionDigits(0);
        String tae = contactformat.format(contactNumber);

        Tv_contactNumber.setText(tae);
        Unitval.setText(Unitstr);
        pangalan.setText(nameuser);
        TV_jobTitle.setText(jobtitle);
        TV_jobid.setText(id);
        TV_scope.setText(ScopeofWork);
        TV_area.setText(area);
        TV_location.setText(Location);
        TV_startdate.setText(Startingdate);
        TV_expecteddate.setText(ExpectedFinishDate);
        Float area = Float.valueOf(TV_area.getText().toString());
        float hours = Float.parseFloat(TV_expecteddate.getText().toString());
        float result;
        Double stringdouble = Double.valueOf(Tv_contactNumber.getText().toString());

        List<String> checkedItems = new ArrayList<>();


        // all required for mongodb
        App app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        assert user != null;
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("JobOrder");
        mongoCollection = mongoDatabase.getCollection("joborders");

        del.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Are you sure you want to delete this job order?" + TV_jobid.getText().toString());
            builder.setPositiveButton("Yes", (dialog, which) -> {
                ObjectId objectId = new ObjectId(id);
                Document filter = new Document("_id", objectId);
                mongoCollection.findOneAndDelete(filter).getAsync(result1 -> {
                    if (result1.isSuccess()) {
                        Toast.makeText(requireContext(), "Deleted successfully.", Toast.LENGTH_LONG).show();
                        // Close the current fragment and go back to the previous one
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Deletion failed.", Toast.LENGTH_LONG).show();
                    }
                });

            });// end of DialogInterface :D
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        Log.v("Datatypenicharles", area.getClass().getSimpleName() + area);


        String scopetv = TV_scope.getText().toString();
        String firstLetter = scopetv.substring(0, 1); // extract the first two letters
        String firstTwoLetters = scopetv.substring(0, 2); // extract the first two letters

        if (firstLetter.equals("G")) { // use .equals() to compare strings
            workertype = "Painter";
            titleskilled.setText("Recommended "+workertype  );

            if(firstTwoLetters.equals("G1")){
                try {
                    hours = hours * 8;
                    Float RateOfWork = 0.54f;
                    result = (RateOfWork * area) / hours;
                    float input1 = area, input2 = hours, productivityratio = result; // input1 is area, input 2 is hours , input 3?

                    float[] inputValues = new float[]{productivityratio, input1, input2};
                    G1plainconcrete model = G1plainconcrete.newInstance(requireContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);

                    inputFeature0.loadArray(inputValues);

                    // Runs model inference and gets result.
                    G1plainconcrete.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] outputValues = outputFeature0.getFloatArray();

                    outputString = 0;

                    for (float value : outputValues) {
                        outputString = Math.round(value);
                    }
                    suggested.getEditText().setText("0");
                    output.setVisibility(View.VISIBLE);
                    suggested.setVisibility(View.INVISIBLE);
                    Objects.requireNonNull(output.getEditText()).setText(String.valueOf(outputString));

//            Log.v("Model Output", outputString);
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }else{
                output.getEditText().setText("0");
                output.setVisibility(View.INVISIBLE);
                suggested.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();
            }
        } else if (firstLetter.equals("D")) {
            workertype = "Plumber";
            titleskilled.setText("Recommended "+workertype  );
            if (firstTwoLetters.equals("D2")){
                Toast.makeText(requireContext(), "D2", Toast.LENGTH_LONG).show();
                try {
                    hours = hours * 8;
                    Float RateOfWork = 1f;
                    result = (RateOfWork * area) / hours;
                    float input1 = area, input2 = hours, productivityratio = result; // input1 is area, input 2 is hours , input 3?

                    float[] inputValues = new float[]{productivityratio, input1, input2};
                    D2Pipeline model = D2Pipeline.newInstance(requireContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);

                    inputFeature0.loadArray(inputValues);

                    // Runs model inference and gets result.
                    D2Pipeline.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] outputValues = outputFeature0.getFloatArray();

                    outputString = 0;

                    for (float value : outputValues) {
                        outputString = Math.round(value);
                    }
                    Objects.requireNonNull(output.getEditText()).setText(String.valueOf(outputString));

                    suggested.getEditText().setText("0");
                    output.setVisibility(View.VISIBLE);
                    suggested.setVisibility(View.INVISIBLE);
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            } else if (firstTwoLetters.equals("D3")) {//TV_scope.getText().equals("D3 - Drainage Pipeline Installation")
                Toast.makeText(requireContext(), "D3", Toast.LENGTH_LONG).show();
                try {
                    hours = hours * 8;
                    Float RateOfWork = 0.8f;
                    result = (RateOfWork * area) / hours;
                    float input1 = area, input2 = hours, productivityratio = result; // input1 is area, input 2 is hours , input 3?

                    float[] inputValues = new float[]{productivityratio, input1, input2};
                    D3DrainagePipeline model = D3DrainagePipeline.newInstance(requireContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);

                    inputFeature0.loadArray(inputValues);

                    // Runs model inference and gets result.
                    D3DrainagePipeline.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] outputValues = outputFeature0.getFloatArray();

                    outputString = 0;

                    for (float value : outputValues) {
                        outputString = Math.round(value);
                    }
                    Objects.requireNonNull(output.getEditText()).setText(String.valueOf(outputString));
                    suggested.getEditText().setText("0");
                    output.setVisibility(View.VISIBLE);
                    suggested.setVisibility(View.INVISIBLE);
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            } else {
                //if it doesnt have any model to forecast
                output.getEditText().setText("0");
                output.setVisibility(View.INVISIBLE);
                suggested.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();
            }


        } else if (firstLetter.equals("B")) {
            workertype = "Carpenter";
            titleskilled.setText("Recommended "+workertype  );

            if (firstTwoLetters.equals("B2")){
                Toast.makeText(requireContext(), "B2", Toast.LENGTH_LONG).show();
                try {
                    hours = hours * 8;
                    Float RateOfWork = 2.6667f;
                    result = (RateOfWork * area) / hours;
                    float input2 = area, input3 = hours, productivityratio = result; // input1 is area, input 2 is hours , input 3?

                    float[] inputValues = new float[]{productivityratio, input2, input3};
                    B2CarpentryWorks model = B2CarpentryWorks.newInstance(requireContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);

                    inputFeature0.loadArray(inputValues);

                    // Runs model inference and gets result.
                    B2CarpentryWorks.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] outputValues = outputFeature0.getFloatArray();

                    outputString = 0;

                    for (float value : outputValues) {
                        outputString = Math.round(value);
                    }

                    Objects.requireNonNull(output.getEditText()).setText(String.valueOf(outputString));
                    suggested.getEditText().setText("0");
                    output.setVisibility(View.VISIBLE);
                    suggested.setVisibility(View.INVISIBLE);
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }else{
                //if it doesnt have any model to forecast
                output.getEditText().setText("0");
                output.setVisibility(View.INVISIBLE);
                suggested.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();
            }

        } else if (firstLetter.equals("L")) {
            workertype = "Labor";
            titleskilled.setText("Recommended "+workertype  );

            output.getEditText().setText("0");
            output.setVisibility(View.INVISIBLE);
            suggested.setVisibility(View.VISIBLE);
            Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();


        } else if (firstLetter.equals("C")) { //Metal Works
            workertype = "Welder";
            titleskilled.setText("Recommended "+workertype);
            output.getEditText().setText("0");
            output.setVisibility(View.INVISIBLE);
            suggested.setVisibility(View.VISIBLE);
            Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();


        }else if (firstLetter.equals("E")) { // Electrical Works
            workertype = "Electrician";
            titleskilled.setText("Recommended "+workertype  );

            output.getEditText().setText("0");
            output.setVisibility(View.INVISIBLE);
            suggested.setVisibility(View.VISIBLE);
            Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(requireContext(), "Selected Type of Work Doesnt have a forecasting model yet.", Toast.LENGTH_LONG).show();
        }// end of forcasting.

        //start of machine learning recommendation
        if (!Python.isStarted())
            Python.start(new AndroidPlatform(requireContext()));
        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("myscript");

        accept.setOnClickListener((view1) -> {

            if (!validateforecastsize() |!validateSuggested()) {
                return;
            }
            ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
            ProgressBar progressBar2 = requireView().findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);

            Handler handler = new Handler(Looper.getMainLooper());
            new Thread(() -> {
                // execute the time-consuming function here recommend
                PyObject objskilled = pyobj.callAttr("find_closest_employee", workertype, TV_location.getText().toString());

                //store JSONObject in list results
                List<JSONObject> results = new ArrayList<>();
                for (PyObject element : objskilled.asList()) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(element.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    results.add(jsonObject);
                }

                jsonResult = objskilled.toString();

                Log.d("resultdata", jsonResult);
                handler.post(() -> {
//                     update the UI here
                    recyclerView = requireView().findViewById(R.id.recyclerView);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);

                    skilledAdapter = new EmployeeAdapter(results, mCheckedItems, null);
                    recyclerView.setAdapter(skilledAdapter);
                    cv4.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                });
            }).start();

            Handler handler2 = new Handler(Looper.getMainLooper());
            new Thread(() -> {
                PyObject objunskilled = pyobj.callAttr("find_closest_employee", "Labor", TV_location.getText().toString());
                //store JSONObject in list results
                List<JSONObject> resultsunskilled = new ArrayList<>();
                for (PyObject element : objunskilled.asList()) {
                    JSONObject jsonObject2;
                    try {
                        jsonObject2 = new JSONObject(element.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    resultsunskilled.add(jsonObject2);
                }


                jsonResult2 = objunskilled.toString();
                Log.d("resultdata", jsonResult2);
                handler2.post(() -> {
                    recyclerView2 = requireView().findViewById(R.id.recyclerView2);
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView2.setLayoutManager(layoutManager2);
                    // Create separate adapters for each RecyclerView
                    Log.d("EmployeeAdapter", "Resultsunskilled size: " + resultsunskilled.size());
                    unskilledAdapter = new laborAdapter(resultsunskilled, new ArrayList<>(), null);
                    recyclerView2.setAdapter(unskilledAdapter);
                    progressBar2.setVisibility(View.GONE);
                    cv5.setVisibility(View.VISIBLE);

//                    for (int i = 0; i < unskilledAdapter.getItemCount(); i++) {
//                        if (unskilledAdapter.getItem(i).isChecked()) {
//                            checkedItems.add(unskilledAdapter.getItem(i).getEmployeeName());
//                        }
//                    }
                    // you can now access the checked items list here
                    Log.d("checkedItems", checkedItems.toString());
                });
            }).start();

        });//end of onclick listener

        stringbuilder.setOnClickListener(v -> {

            // get the checked items from the adapter
            int totalunskilled = unskilledAdapter.getCheckedItems().size();
            int totalskilled = skilledAdapter.getCheckedItems().size();
            Toast.makeText(requireContext(), String.valueOf(totalskilled),Toast.LENGTH_LONG).show();
            Toast.makeText(requireContext(),String.valueOf(totalunskilled),Toast.LENGTH_LONG).show();
            List<JSONObject> checkedItems1 = skilledAdapter.getCheckedItems();
            List<JSONObject> checkedItems2 = unskilledAdapter.getCheckedItems();
            Document test = new Document();
            Log.v("charleskendrickmagalued",checkedItems1.toString());
//  check that at least one item is selected in each list
            if (!validateforecast()) {
                return;
            }
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0);
            ObjectId jobid = new ObjectId(id);
            test.append("_id",jobid).append("idUser",userId).append("ProjectName",jobtitle).append("ClientName",nameuser).append("ContactNumber",stringdouble).append("TypeOfWork", ScopeofWork).append("Area",df.format(area)).append("Unit",Unitstr).append("Location",Location)
                    .append("StartingDate",Startingdate).append("ExpectedFinishDate",ExpectedFinishDate).append("ForecastedNum",Integer.valueOf(output.getEditText().getText().toString())).append("SuggestedNum",Integer.valueOf(suggested.getEditText().getText().toString())); //


            int n = 0;
            int z = 0;
            // add items from the first list to the StringBuilder
            for (JSONObject item : checkedItems1) {
                String objectId = item.optString("employeeId");
                String name = item.optString("name");
                String employeeId = extractEmployeeId(objectId);
                for (int i = 0; i < totalskilled; i++) {
                      n = i +1;
                     test.append("Worker" + n, employeeId).append("WorkerName"+n, name);
                    // Do something with the 'test' Document object here
                }
            }

            // add items from the second list to the StringBuilder
            for (JSONObject item : checkedItems2) {
                String objectId = item.optString("employeeId");
                String employeeId = extractEmployeeId(objectId);
                String name = item.optString("name");

                for (int i = 0; i < totalunskilled; i++) {
                    z = n +i+1;
                    test.append("Worker" + z, employeeId).append("WorkerName"+z, name);
                }
            }

            String testString = test.toJson();

            ObjectId objectId = new ObjectId(id);
            Document filter = new Document("_id", objectId);
            mongoCollection.findOneAndDelete(filter).getAsync(result1 -> {
                if (result1.isSuccess()) {
                    Toast.makeText(requireContext(), "Deleted successfully.", Toast.LENGTH_LONG).show();
                    mongoDatabase = mongoClient.getDatabase("ReviewJobOrder");
                    mongoCollection = mongoDatabase.getCollection("joborders");

                    mongoCollection.insertOne(test.append("created", new Date())).getAsync(result2 -> {
                        if (result2.isSuccess()){
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }else{
                            Toast.makeText(requireContext(),"failed",Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Deletion failed.", Toast.LENGTH_LONG).show();
                }
            });
        });


        return view;
    }//end of oncreateView

    private String extractEmployeeId(String objectId) {
        String regex = "^ObjectId\\('(.+)'\\)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(objectId);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return objectId;
        }
    }
    private boolean validateforecastsize(){
        String stroutput = Objects.requireNonNull(output.getEditText().getText().toString().trim());
        if (stroutput.isEmpty()){
            output.setError("Cannot be Blank.");
            return false;
        } else  {
            output.setError(null);
            return true;
        }
    }
    private boolean validateSuggested(){
        String stroutput = Objects.requireNonNull(suggested.getEditText().getText().toString().trim());
        if (stroutput.isEmpty()){
            suggested.setError("Cannot be Blank.");
            return false;
        } else  {
            suggested.setError(null);
            return true;
        }
    }
    private boolean validateforecast(){

        int intoutput = Integer.parseInt(Objects.requireNonNull(output.getEditText()).getText().toString());
        int intsuggested = Integer.parseInt(Objects.requireNonNull(suggested.getEditText()).getText().toString());
        int sum = intoutput + intsuggested;
        int totalunskilled = unskilledAdapter.getCheckedItems().size();
        int totalskilled = skilledAdapter.getCheckedItems().size();
        int totalsum = totalunskilled + totalskilled;
        if (sum > totalsum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Selected Number("+totalsum+") of Worker is less than the Required ("+intoutput+").")
                    .setTitle("Error")
                    .setPositiveButton("OK", (dialog, id) -> {
                        //
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        } else if (sum < totalsum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Selected Number("+totalsum+") of Worker is greater than the Required ("+intoutput+").")
                    .setTitle("Error")
                    .setPositiveButton("OK", (dialog, id) -> {
                        // Total Selected Worker is greater than the given number of employees.
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        } else {
            output.setError(null);
            return true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mCheckedItems.add((JSONObject) buttonView.getTag()); // Add the checked item to the list
        } else {
            mCheckedItems.remove(buttonView.getTag()); // Remove the unchecked item from the list
        }

        // Perform any necessary actions with the updated checked items list
        // For example, you can print the list to the logcat
        for (JSONObject item : mCheckedItems) {
            Log.d("MyFragment", "Checked item: " + item.toString());
        }
    }
}

