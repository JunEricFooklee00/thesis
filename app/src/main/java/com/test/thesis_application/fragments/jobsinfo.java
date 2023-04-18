package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.test.thesis_application.R;


public class jobsinfo extends Fragment {

   TextView TV_jobTitle,TV_jobid,TV_scope,TV_area,TV_location,TV_startdate,TV_expecteddate,TV_userid,output;
   String jobtitle,userId,id,ScopeofWork,area,Location,ExpectedFinishDate,Startingdate;

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
        TV_startdate= view.findViewById(R.id.tv_startingdate);
        TV_expecteddate = view.findViewById(R.id.tv_expecteddate);
        output = view.findViewById(R.id.TIL_forcasted);
        Bundle data = getArguments();
        if (data != null) {
            userId = data.getString("userId");
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