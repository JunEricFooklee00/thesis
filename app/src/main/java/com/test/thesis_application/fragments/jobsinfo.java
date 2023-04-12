package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.tensorflow.lite.DataType;

import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import androidx.fragment.app.Fragment;

import com.test.thesis_application.R;
import com.test.thesis_application.ml.Model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;


public class jobsinfo extends Fragment {

   TextView TV_jobTitle,TV_jobid,TV_scope,TV_area,TV_location,TV_startdate,TV_expecteddate,TV_userid;
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


        //start of tenserflowlite
        try {
            float[] inputValues = new float[]{45.6f,16f, 1.2528f};
            Model model = Model.newInstance(requireContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);

            inputFeature0.loadArray(inputValues);
//            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] outputValues = outputFeature0.getFloatArray();
            String outputString = "Output values: ";
            for (float value : outputValues) {
                outputString += value + ", ";
            }
            Log.v("Model Output", outputString);
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
//end of tenserflowlite
        return view;
    }
//    private int argmax(float[] array) {
//        int maxIndex = 0;
//        float maxVal = array[0];
//        for (int i = 1; i < array.length; i++) {
//            if (array[i] > maxVal) {
//                maxVal = array[i];
//                maxIndex = i;
//            }
//        }
//        return maxIndex;
//    }
}