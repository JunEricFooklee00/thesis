package com.test.thesis_application.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.test.thesis_application.R;

import java.text.DateFormat;
import java.util.Calendar;

public class insertjob extends Fragment {
    //declare variables
    private AutoCompleteTextView ScopeOfWork, TypeOfWork;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private TextInputEditText TietAge;
//    private TextInputLayout  ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insertjob, container, false);

        TietAge= view.findViewById(R.id.TIET_age);

        TietAge.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and show it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateSetListener, mYear, mMonth, mDayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            datePickerDialog.show();
        });

        //materialspinner
        String[] Siteprep = getResources().getStringArray(R.array.SitePreparation);
        ArrayAdapter<String> siteprep = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, Siteprep);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] CarpWork = getResources().getStringArray(R.array.CarpentryWorks);
        ArrayAdapter<String> carpentry = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, CarpWork);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] MechMetalWork = getResources().getStringArray(R.array.MechanicalMetalWorks);
        ArrayAdapter<String> mechanical = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, MechMetalWork);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] ElecWork = getResources().getStringArray(R.array.ElectricalWorks);
        ArrayAdapter<String> electrical = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, ElecWork);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] TherMoistProtection = getResources().getStringArray(R.array.ThermalandMoistureProtectionWorks);
        ArrayAdapter<String> protection = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, TherMoistProtection);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] PaintWork = getResources().getStringArray(R.array.PaintingWorks);
        ArrayAdapter<String> painting = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item_gender, PaintWork);
        TypeOfWork = view.findViewById(R.id.sub_scope_of_work);

        String[] ScopeOfJob = getResources().getStringArray(R.array.scopeofwork);
        ArrayAdapter<String> jobadapter = new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, ScopeOfJob);
        ScopeOfWork = view.findViewById(R.id.filled_exposed);
        ScopeOfWork.setAdapter(jobadapter);
        ScopeOfWork.setOnItemClickListener((parent, view1, position, id) -> {
            if(ScopeOfWork.getText().toString().equals("Site Preparation")){
                TypeOfWork.setAdapter(siteprep);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Carpentry Works")){
                TypeOfWork.setAdapter(carpentry);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Mechanical/Metal Works")){
                TypeOfWork.setAdapter(mechanical);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Electrical Works")){
                TypeOfWork.setAdapter(electrical);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Painting Works")){
                TypeOfWork.setAdapter(painting);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }else if (ScopeOfWork.getText().toString().equals("Thermal and Moisture Protection Works")){

                TypeOfWork.setAdapter(protection);
                Toast.makeText(requireContext(),ScopeOfWork.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });



        //materialspinner




        return view;
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            String datepickerstring = DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());
            TietAge.setText(datepickerstring);
        }
    };
}
