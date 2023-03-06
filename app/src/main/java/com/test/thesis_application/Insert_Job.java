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
                DialogFragment datepicker = new DialogFragment();
                datepicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
    }

    @Override
    public void onBackPressed(){
        if (drawer_reg.isDrawerOpen(GravityCompat.START)){
            drawer_reg.closeDrawer(GravityCompat.START);

        }else{
            super.onBackPressed();
            this.finish();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}