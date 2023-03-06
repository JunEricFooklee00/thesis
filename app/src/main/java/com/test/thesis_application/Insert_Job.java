package com.test.thesis_application;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.test.thesis_application.fragments.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class Insert_Job extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DrawerLayout drawer_reg;
    Button start,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_job);

        //nav bar return icon
        Toolbar toolbar =findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        drawer_reg = findViewById(R.id.drawer_insert_Job);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());// end of navbar return icon

        start = findViewById(R.id.startdate);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
    }

    @Override // for back icon in register
    public void onBackPressed(){
        if (drawer_reg.isDrawerOpen(GravityCompat.START)){
            drawer_reg.closeDrawer(GravityCompat.START);

        }else{
            super.onBackPressed();
            this.finish();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int i1, int i2, int i3) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,i1);
        cal.set(Calendar.MONTH,i2);
        cal.set(Calendar.DAY_OF_MONTH,i3);

        String datepickerstring = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        start.setText(datepickerstring);
    }
}