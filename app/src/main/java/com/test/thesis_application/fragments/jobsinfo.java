package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.test.thesis_application.R;


public class jobsinfo extends Fragment {

   TextView TV_jobTitle,TV_jobid,TV_scope;
   String jobtitle,userId,id,ScopeofWork,area,Location,ExpectedFinishDate,Startingdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobsinfo, container, false);

        TV_jobTitle = view.findViewById(R.id.textView_jobTitle);
        TV_jobid = view.findViewById(R.id.tv_jobID);
        TV_scope = view.findViewById(R.id.tv_scope);
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
        return view;
    }
}