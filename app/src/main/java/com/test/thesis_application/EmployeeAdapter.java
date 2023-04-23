package com.test.thesis_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<JSONObject> results;

    public EmployeeAdapter(List<JSONObject> results) {
        this.results = results;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView idTextView;
        public TextView locationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_employee_name);
            idTextView = itemView.findViewById(R.id.tv_employee_id);
            locationTextView = itemView.findViewById(R.id.tv_employee_location);
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
            holder.nameTextView.setText(employee.getString("first_name") + " " + employee.getString("last_name"));
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