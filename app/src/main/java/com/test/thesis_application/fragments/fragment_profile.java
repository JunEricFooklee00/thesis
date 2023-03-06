package com.test.thesis_application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.test.thesis_application.R;

public class fragment_profile extends Fragment {
    private static final String arg_name = "argName";
    private static final String arg_Username = "argUserName";

    private  String name, email;
    private Button button;

    public static fragment_profile newInstance(String name, String email){

        fragment_profile fragProf = new fragment_profile();
        Bundle args = new Bundle();
        args.putString(arg_name,name);
        args.putString(arg_Username,email);
        fragProf.setArguments(args);
        return  fragProf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tv_profilename = view.findViewById(R.id.profile_name);
        TextView tv_profileEmail = view.findViewById(R.id.profile_email);
        Bundle data = getArguments();
        if (data != null){
            name = data.getString("name");
            email = data.getString("username");
        }
        tv_profilename.setText(name);
        tv_profileEmail.setText(email);
//
//
//        tv_profileEmail.setText(getArguments().getString("argName"));
//        tv_profilename.setText(getArguments().getString("argUserName"));
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getArguments() != null){
////                    name = getArguments().getString(arg_name);
////                    email = getArguments().getString(arg_Username);
////                    Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();
////                }
//
//            }
//        });
        return view;
    }
}
