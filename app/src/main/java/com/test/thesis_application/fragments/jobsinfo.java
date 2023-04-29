package com.test.thesis_application.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.ArrayList;
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
    TextInputLayout output, skilled, unskilled;
    TextView cv4tv;
    TextView TV_jobTitle;
    TextView TV_jobid;
    TextView TV_scope;
    TextView TV_area;
    TextView TV_location;
    TextView TV_startdate;
    TextView TV_expecteddate;
    String jobtitle, userId, id, ScopeofWork, area, Location, ExpectedFinishDate, Startingdate;
    Button accept;
    int outputString;
    CardView cv4, cv5;
    String jsonResult2;
    String jsonResult;
    TextView stringbuild;

    EmployeeAdapter skilledAdapter;
    laborAdapter unskilledAdapter;
    RecyclerView recyclerView, recyclerView2;
    ImageView del;
    private List<JSONObject> mCheckedItems = new ArrayList<>();
    //    List<JSONObject> checkedItems = skilledAdapter();
    String Appid = "employeems-mcwma";
    User user;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobsinfo, container, false);

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

        stringbuilder = view.findViewById(R.id.accept);
        stringbuild = view.findViewById(R.id.textbuilder);
        cv4tv = view.findViewById(R.id.cv4tv);
        Bundle data = getArguments();
        if (data != null) {
            userId = data.getString("idUser");
            id = data.getString("_id");
            ScopeofWork = data.getString("scopeofwork");
            area = data.getString("area");
            Location = data.getString("location");
            ExpectedFinishDate = data.getString("expectedfinishdate");
            jobtitle = data.getString("jobtitle");
            Startingdate = data.getString("startingdate");
        }

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
//        cv4.setVisibility(View.INVISIBLE);
//        cv5.setVisibility(View.INVISIBLE);
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
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        Log.v("Datatypenicharles", area.getClass().getSimpleName() + area);


        if (TV_scope.getText().equals("B2 - Carpentry Works for Main Counter")) {
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
                output.setEnabled(false);
//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else if (TV_scope.getText().equals("D2 - Pipeline and Fixture Installation")) {
            Toast.makeText(requireContext(), "Pipeline and Fixture Installation", Toast.LENGTH_LONG).show();

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

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else if (TV_scope.getText().equals("D3 - Drainage Pipeline Installation")) {
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

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else if (TV_scope.getText().equals("G1 - Plain Concrete Surfaces (Surface Prep - Primer - Finish Coat)")) {
            Toast.makeText(requireContext(), "G1", Toast.LENGTH_LONG).show();

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
                Objects.requireNonNull(output.getEditText()).setText(String.valueOf(outputString));

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else {
            Toast.makeText(requireContext(), "No need i forcast po", Toast.LENGTH_LONG).show();
        }// end of forcasting.

        //start of machine learning recommendation
        if (!Python.isStarted())
            Python.start(new AndroidPlatform(requireContext()));


        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("myscript");

        accept.setOnClickListener((view1) -> {

            if (!validateforecastsize() ) {
                return;
            }
            ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
            ProgressBar progressBar2 = requireView().findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);

            Handler handler = new Handler(Looper.getMainLooper());
            new Thread(() -> {
                // execute the time-consuming function here recommend
                PyObject objskilled = pyobj.callAttr("find_closest_employee", "Mason", TV_location.getText().toString());

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
            Toast.makeText(requireContext(),totalskilled,Toast.LENGTH_LONG).show();
            Toast.makeText(requireContext(),totalunskilled,Toast.LENGTH_LONG).show();
            List<JSONObject> checkedItems1 = skilledAdapter.getCheckedItems();
            List<JSONObject> checkedItems2 = unskilledAdapter.getCheckedItems();
            Document test = null;
//             check that at least one item is selected in each list
            if (!validateforecast()) {
                return;
            }

            // create a StringBuilder for the selected items
            StringBuilder selectedItemsBuilder = new StringBuilder();

            // add items from the first list to the StringBuilder
            for (JSONObject item : checkedItems1) {
                String objectId = item.optString("name");
                String employeeId = extractEmployeeId(objectId);
                for (int i = 0; i < totalskilled; i++) {
                     test = new Document("tae" + i, employeeId);
                    // Do something with the 'test' Document object here
                }

            }

            // add items from the second list to the StringBuilder
            for (JSONObject item : checkedItems2) {
                String objectId = item.optString("employeeId");
                String employeeId = extractEmployeeId(objectId);
                for (int i = 0; i < totalunskilled; i++) {
                     test = new Document("tae" + i, employeeId);
                    // Do something with the 'test' Document object here
                }
//                selectedItemsBuilder.append(employeeId).append(", ");
            }

//            // convert the StringBuilder to a String and trim trailing commas
//            String selectedItems = selectedItemsBuilder.toString().trim();
//            if (selectedItems.endsWith(",")) {
//                selectedItems = selectedItems.substring(0, selectedItems.length() - 1);
//            }
            String testString = test.toJson();
//            System.out.println(testString);
            // set the TextView with the selected items
            stringbuild.setText(testString);
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
    private boolean validateforecast(){

        int intoutput = Integer.parseInt(Objects.requireNonNull(output.getEditText()).getText().toString());
        int totalunskilled = unskilledAdapter.getCheckedItems().size();
        int totalskilled = skilledAdapter.getCheckedItems().size();
        int totalsum = totalunskilled + totalskilled;
        if (intoutput > totalsum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Selected Number("+totalsum+") of Worker is less than the Required ("+intoutput+").")
                    .setTitle("Error")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        } else if (intoutput < totalsum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Selected Number("+totalsum+") of Worker is greater than the Required ("+intoutput+").")
                    .setTitle("Error")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Total Selected Worker is greater than the given number of employees.
                        }
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

