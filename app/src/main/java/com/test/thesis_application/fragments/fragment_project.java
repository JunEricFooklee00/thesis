package com.test.thesis_application.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.test.thesis_application.Insert_Job;
import com.test.thesis_application.R;

public class fragment_project extends Fragment  {
    //declare variables

    ImageView datePickerbtn;
    TextView dateview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_projects, container, false);
        datePickerbtn = view.findViewById(R.id.imageView2);
        datePickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert_job = new Intent(getActivity().getApplication(), Insert_Job.class);
                startActivity(insert_job);
            }
        });
//        datePickerbtn = view.findViewById(R.id.get_startDate);
//        datePickerbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogFragment datepicker =  new DatePickerFragment();
////                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
//
//                datepicker.show(getActivity().getSupportFragmentManager(), "Date Picker ");
//            }
//        });
        return view;
    }



}
