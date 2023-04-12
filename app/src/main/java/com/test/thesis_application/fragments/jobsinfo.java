package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.test.thesis_application.R;


public class jobsinfo extends Fragment {

   TextView TV_jobTitle;
   String jobtitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobsinfo, container, false);

        TV_jobTitle = view.findViewById(R.id.textView_jobTitle);
        Bundle data = getArguments();
        if (data != null) {
//            uid = data.getString("uid");
//            name = data.getString("name");
//            email = data.getString("email");
//            resume = data.getString("resume");
//            username = data.getString("username");
//            birthday = data.getString("birthday");
            jobtitle = data.getString("jobtitle");
//            address = data.getString("address");
//            zipcode = data.getString("zipcode");
        }
        TV_jobTitle.setText(jobtitle);
        return view;
    }
}