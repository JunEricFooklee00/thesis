package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.thesis_application.R;

public class fragment_project extends Fragment  {
    //declare variables

    ImageView datePickerbtn;
    TextView dateview;
    FloatingActionButton insert;
    private String userid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_projects, container, false);


        insert = view.findViewById(R.id.insert_job);

        Bundle projectuserid = getArguments();
        if (projectuserid != null) {
            userid = projectuserid.getString("user_ID");
        }
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertjobtransaction();
            }
        });
        return view;
    }

    private void insertjobtransaction() {

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        insertjob fragment = new insertjob();

        // Create a Bundle to pass your data
        Bundle bundle = new Bundle();
        bundle.putString("uid", userid); // Example of adding a String to the Bundle

        // Set the Bundle as an argument for your fragment
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(requireContext(),userid,Toast.LENGTH_LONG).show();
    }


}
