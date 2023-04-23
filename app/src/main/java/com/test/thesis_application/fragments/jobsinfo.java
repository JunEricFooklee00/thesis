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


import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputLayout;
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


public class jobsinfo extends Fragment {

    TextInputLayout output;
    TextView TV_jobTitle, TV_jobid, TV_scope, TV_area, TV_location, TV_startdate, TV_expecteddate, TV_userid, ml;
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
        Float area = new Float(TV_area.getText().toString());
        Float hours = new Float(TV_expecteddate.getText().toString());
//        Log.v("FloatValue", String.valueOf(area));

//        Toast.makeText(requireContext(),hours.getClass().getSimpleName(),Toast.LENGTH_LONG).show();

        Log.v("Datatypenicharles", area.getClass().getSimpleName() + area);
        if (TV_scope.getText().equals("B2 - Carpentry Works for Main Counter")) {
            Toast.makeText(requireContext(), "B2", Toast.LENGTH_LONG).show();
            try {
                hours = hours * 8;
                float input1 = area, input2 = hours, productivityratio = 6f; // input1 is area, input 2 is hours , input 3?

                float[] inputValues = new float[]{productivityratio, input1, input2};
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

                output.getEditText().setText(String.valueOf(outputString));
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
                float input1 = area, input2 = hours, productivityratio = 8f; // input1 is area, input 2 is hours , input 3?

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
                output.getEditText().setText(String.valueOf(outputString));

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
                float input1 = area, input2 = hours, productivityratio = 6f; // input1 is area, input 2 is hours , input 3?

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
                output.getEditText().setText(String.valueOf(outputString));

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
                float input1 = area, input2 = hours, productivityratio = 6f; // input1 is area, input 2 is hours , input 3?

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
                output.getEditText().setText(String.valueOf(outputString));

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
            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            Handler handler = new Handler(Looper.getMainLooper());

            new Thread(() -> {
                // execute the time-consuming function here

                PyObject obj = pyobj.callAttr("find_closest_employee", "Mason", TV_location.getText().toString(), Integer.valueOf(output.getEditText().getText().toString()));

                List<JSONObject> results = new ArrayList<>();
                for (PyObject element : obj.asList()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(element.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    results.add(jsonObject);
                }
//                results.get(0).toString();
//                for (JSONObject result : results) {
//                    System.out.println("Object ID: " + result.getString("Object ID"));
//                    System.out.println("First Name: " + result.getString("first_name"));
//                    System.out.println("Last Name: " + result.getString("last_name"));
//                    System.out.println("Address: " + result.getString("address"));
//                    System.out.println("Profile: " + result.getString("Profile"));
//                    System.out.println("Rating: " + result.getString("Rating"));
//                    System.out.println("Distance: " + result.getString("distance"));
//                }
                String jsonResult = obj.toString();

                handler.post(() -> {
                    // update the UI here
                    try {
                        for (int i = 0; i < results.size(); i++) {
                            JSONObject employee = results.get(i);
                            ml.setText("id: " +employee.getString("employeeId") +"," + "First Name: " + employee.getString("first_name") + ", Last Name: " + employee.getString("last_name"));
                            Log.d("Employee", ""+employee.getString("employeeId") +"," + "First Name: " + employee.getString("first_name") + ", Last Name: " + employee.getString("last_name"));
                        }


//                        ml.setText(results.get(0).getString("employeeId"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    progressBar.setVisibility(View.GONE);

//                    for (JSONObject employee : results) {
//                        try {
//                            String employeeId = employee.getString("employeeId");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        try {
//                            String firstName = employee.getString("first_name");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        try {
//                            String lastName = employee.getString("last_name");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        try {
//                            String address = employee.getString("address");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        try {
//                            String profile = employee.getString("Profile");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        try {
//                            String rating = employee.getString("Rating");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        try {
//                            String distance = employee.getString("distance");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }

                        // do something with the extracted employee details
                        // e.g. display them in a TextView or add them to a list
//                    }
                });
            }).start();

        });//end of onclick listener



        return view;
    }//end of oncreateView

}