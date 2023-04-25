package com.test.thesis_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private static List<JSONObject> results;


    static QuantityListener quantityListener;
    static ArrayList<String> selected;

    //    public EmployeeAdapter(List<JSONObject> results) {
//        this.results = results;
//    }
    public EmployeeAdapter(List<JSONObject> results) {
        this.results = results;
//        selected = new ArrayList<>();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView;
        public TextView locationTextView;
        public TextView job;
        public CheckBox employee;

        public ViewHolder(View itemView) {
            super(itemView);
//            nameTextView = itemView.findViewById(R.id.tv_employee_name);
            idTextView = itemView.findViewById(R.id.tv_employee_id);
            locationTextView = itemView.findViewById(R.id.tv_employee_location);
            job = itemView.findViewById(R.id.tv_jobtype);
            employee = itemView.findViewById(R.id.checkBox);

//
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject employee = results.get(position);
            holder.employee.setText(employee.getString("first_name") + " " + employee.getString("last_name"));
            holder.job.setText(employee.getString("Profile"));
//            holder.nameTextView.setText(employee.getString("first_name") + " " + employee.getString("last_name"));
            holder.idTextView.setText("ID: " + employee.getString("employeeId"));
            holder.locationTextView.setText("Location: " + employee.getString("address"));


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}