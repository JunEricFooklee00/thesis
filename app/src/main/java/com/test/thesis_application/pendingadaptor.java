package com.test.thesis_application;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.test.thesis_application.fragments.pendingemployeesClass;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class pendingadaptor extends RecyclerView.Adapter<pendingadaptor.MyViewHolder> {

    private List<pendingemployeesClass> orders;

    public pendingadaptor(List<pendingemployeesClass> employeeList) {
        this.orders = employeeList;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, jobtypeTextView, contactTextView, addressTextView, jobid;
        ImageView avatarImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            //initialize the views in the layout for each item in the RecyclerView
            nameTextView = itemView.findViewById(R.id.Name);
            jobtypeTextView = itemView.findViewById(R.id.tv_employee_id);
            jobid = itemView.findViewById(R.id.tv_jobtype);
            contactTextView = itemView.findViewById(R.id.textView8);
            addressTextView = itemView.findViewById(R.id.tv_employee_location);
            avatarImageView = itemView.findViewById(R.id.employee_avatar);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_employee, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set the data for each item in the RecyclerView
        holder.nameTextView.setText(orders.get(position).getName());
        holder.jobtypeTextView.setText(orders.get(position).getJobtype());
        holder.contactTextView.setText(String.valueOf(orders.get(position).getEcontactNumber()));
        holder.addressTextView.setText(orders.get(position).getAddress());
        Picasso.get().load(orders.get(position).getAvatar()).transform(new CropCircleTransformation()).into(holder.avatarImageView);
        Log.v("EmployeeDetails", holder.nameTextView.getText().toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


}