package com.test.thesis_application.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Long mMinDate;
    private Long mMaxDate;

    public void setMinDate(Long minDate) {
        mMinDate = minDate;
    }

    public void setMaxDate(Long maxDate) {
        mMaxDate = maxDate;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        if (mMinDate != null) {
            datePickerDialog.getDatePicker().setMinDate(mMinDate);
        }
        if (mMaxDate != null) {
            datePickerDialog.getDatePicker().setMaxDate(mMaxDate);
        } else {
            // Set maximum date to December 31st, 2005
            cal.set(2005, Calendar.DECEMBER, 31);
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        }

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
