package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.test.thesis_application.R;
import com.test.thesis_application.ml.B2CarpentryWorks;
import com.test.thesis_application.ml.D2Pipeline;
import com.test.thesis_application.ml.D3DrainagePipeline;
import com.test.thesis_application.ml.G1plainconcrete;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;


public class jobsinfo extends Fragment {

    TextInputLayout output;
    TextView TV_jobTitle, TV_jobid, TV_scope, TV_area, TV_location, TV_startdate, TV_expecteddate, TV_userid;
    String jobtitle, userId, id, ScopeofWork, area, Location, ExpectedFinishDate, Startingdate;
    float outputString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobsinfo, container, false);

        TV_jobTitle = view.findViewById(R.id.textView_jobTitle);
        TV_jobid = view.findViewById(R.id.tv_jobID);
        TV_scope = view.findViewById(R.id.tv_scope);
        TV_area = view.findViewById(R.id.tv_Area);
        TV_location = view.findViewById(R.id.tv_location);
        TV_startdate = view.findViewById(R.id.tv_startingdate);
        TV_expecteddate = view.findViewById(R.id.tv_expecteddate);
        output = view.findViewById(R.id.TIL_forcasted);

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

        Log.v("Datatypenicharles",area.getClass().getSimpleName() + area);
        if (TV_scope.getText().equals("B2 - Carpentry Works for Main Counter")) {
            Toast.makeText(requireContext(),"B2",Toast.LENGTH_LONG).show();
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

                output.getEditText().setText(String.valueOf(outputString) );

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else if (TV_scope.getText().equals("D2 - Pipeline and Fixture Installation")) {
            Toast.makeText(requireContext(),"Pipeline and Fixture Installation",Toast.LENGTH_LONG).show();

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
                output.getEditText().setText(String.valueOf(outputString) );

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else if (TV_scope.getText().equals("D3 - Drainage Pipeline Installation")) {
            Toast.makeText(requireContext(),"D3",Toast.LENGTH_LONG).show();

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
                output.getEditText().setText(String.valueOf(outputString) );

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else if (TV_scope.getText().equals("G1 - Plain Concrete Surfaces (Surface Prep - Primer - Finish Coat)")) {
            Toast.makeText(requireContext(),"G1",Toast.LENGTH_LONG).show();

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
                output.getEditText().setText(String.valueOf(outputString) );

//            Log.v("Model Output", outputString);
                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else {
            Toast.makeText(requireContext(),"tangina ayaw beh",Toast.LENGTH_LONG).show();
        }

//        }else {
//
//        }
        //start of tenserflowlite forecasting.
//        if (TV_scope.equals("")){
//
//        }
//        try {
//            float input1 = 6.75f, input2 = 16f, productivityratio = 6f; // input1 is , input 2 is
//
//            float[] inputValues = new float[]{productivityratio,input1,input2 };
//            B1Works model = B1Works.newInstance(requireContext());
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);
//
//            inputFeature0.loadArray(inputValues);
//
//            // Runs model inference and gets result.
//            B1Works.Outputs outputs = model.process(inputFeature0);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//            float[] outputValues = outputFeature0.getFloatArray();
//
//            float outputString = 0;
//
//            for (float value : outputValues) {
//                outputString = Math.round(value);
//            }
//            output.setText("The recommended amount of workers for this project is :"+ outputString);
//
////            Log.v("Model Output", outputString);
//            // Releases model resources if no longer used.
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }

//        try {
//            float[] inputValues = new float[]{6.75f,16f, 1.125014063f};
//            B1Works ibangmodel = B1Works.newInstance(requireContext());
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature1 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);
//            inputFeature1.loadArray(inputValues);
//            // Runs model inference and gets result.
//            B1Works.Outputs outputs = ibangmodel.process(inputFeature1);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//            float[] outputValues = outputFeature0.getFloatArray();
//            String outputString = "Output values: ";
//            for (float value : outputValues) {
//                outputString += value + ", ";
//            }
//            Log.v("Model Output", outputString);
//            // Releases model resources if no longer used.
//            ibangmodel.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }

//end of tenserflowlite
        return view;
    }

}