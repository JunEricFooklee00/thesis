package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.EmployeeAdapter;
import com.test.thesis_application.R;
import com.test.thesis_application.ml.B2CarpentryWorks;
import com.test.thesis_application.ml.D2Pipeline;
import com.test.thesis_application.ml.D3DrainagePipeline;
import com.test.thesis_application.ml.G1plainconcrete;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class jobsinfo extends Fragment {

    TextInputLayout output, skilled, unskilled;
    TextView TV_jobTitle;
    TextView TV_jobid;
    TextView TV_scope;
    TextView TV_area;
    TextView TV_location;
    TextView TV_startdate;
    TextView TV_expecteddate;
    TextView ml;
    String jobtitle, userId, id, ScopeofWork, area, Location, ExpectedFinishDate, Startingdate;
    Button accept;
    int outputString;

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
        ml = view.findViewById(R.id.outputssz);
        skilled = view.findViewById(R.id.TIL_skilled);
        unskilled = view.findViewById(R.id.TIL_unskilled);

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
        }

        if (!Python.isStarted())
            Python.start(new AndroidPlatform(requireContext()));


        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("myscript");

        accept.setOnClickListener((view1) -> {

            if (!validateskilled() | !validateunskilled() |!validateforcasted()) {
                return;
            }
            ProgressBar progressBar = requireView().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler(Looper.getMainLooper());
            new Thread(() -> {

                // execute the time-consuming function here recommend
                PyObject objskilled = pyobj.callAttr("find_closest_employee", "Mason", TV_location.getText().toString());
                PyObject objunskilled = pyobj.callAttr("find_closest_employee", "Labor", TV_location.getText().toString());


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

                //store JSONObject in list results
                List<JSONObject> resultsunskilled = new ArrayList<>();
                for (PyObject element : objunskilled.asList()) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(element.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    resultsunskilled.add(jsonObject);
                }
//                List<JSONObject> mergedList = new ArrayList<>();
//                mergedList.addAll(results);
//                mergedList.addAll(resultsunskilled);
//
                String jsonResult = objskilled.toString();
                String jsonResult2 = objunskilled.toString();
                Log.d("resultdata", jsonResult + jsonResult2);
                handler.post(() -> {
                    // update the UI here
                    RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
                    RecyclerView recyclerView2 = requireView().findViewById(R.id.recyclerView2);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView2.setLayoutManager(layoutManager2);
                    recyclerView.setAdapter(new EmployeeAdapter(results));
                    recyclerView2.setAdapter(new EmployeeAdapter(resultsunskilled));
//                    layoutManager.getItemCount();



                    progressBar.setVisibility(View.GONE);

                });
            }).start();

        });//end of onclick listener
        return view;
    }//end of oncreateView

    private boolean validateskilled() {
        String strskilled = Objects.requireNonNull(skilled.getEditText()).getText().toString().trim();
        String strunskilled = Objects.requireNonNull(unskilled.getEditText()).getText().toString().trim();
        if (strskilled.isEmpty()) {
            skilled.getEditText().setText("0");
            skilled.setError("Atleast 1 skilled worker is required.");
            return false;
        } else if (strskilled.equals("0")) {
            skilled.setError("Atleast 1 skilled worker is required.");
            return false;
        }
        else {
            skilled.setError(null);
            return true;
        }
    }

    private boolean validateunskilled() {
        String strunskilled = Objects.requireNonNull(unskilled.getEditText()).getText().toString().trim();
        if (strunskilled.isEmpty()) {
            unskilled.getEditText().setText("0");
            return true;
        } else {
            return true;
        }
    }

    private boolean validateforcasted() {
        int forecastedoutput = Integer.valueOf(Objects.requireNonNull(output.getEditText()).getText().toString());

        int givenskilled = Integer.valueOf(Objects.requireNonNull(skilled.getEditText()).getText().toString());
        int givenunskilled = Integer.valueOf(Objects.requireNonNull(unskilled.getEditText()).getText().toString());


        float sum = givenskilled + givenunskilled;
        if (sum == forecastedoutput) {
            unskilled.setError(null);
            skilled.setError(null);
            return true;
        } else if (sum < forecastedoutput) {
            unskilled.setError("The sum is less than the forcasted.");
            skilled.setError("The sum is less than the forcasted.");
            return false;
        } else if (sum > forecastedoutput) {
            unskilled.setError("The sum is greater than the forcasted.");
            skilled.setError("The sum is greater than the forcasted.");
            return false;
        } else {
            unskilled.setError("The sum must equal the forcasted.");
            skilled.setError("The sum must equal the forcasted.");
            return false;
        }
    }

}
