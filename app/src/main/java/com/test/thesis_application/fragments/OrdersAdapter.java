package com.test.thesis_application.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.thesis_application.JobsOrderClass;
import com.test.thesis_application.R;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    List<JobsOrderClass> orders ;
//     List<JobsOrderClass> orders;
//     Context context;


    public OrdersAdapter(List<JobsOrderClass> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_id.setText(orders.get(position).get_id().toString());
        holder.textView_scopeofwork.setText(orders.get(position).getScopeofwork());
        holder.textView_jobTitle.setText(orders.get(position).getJobTitle());
        holder.textView_area.setText(orders.get(position).getArea());
        holder.textView_location.setText(orders.get(position).getLocation());
        holder.textView_startingDate.setText(orders.get(position).getStartingDate());
        holder.textView_expectedFinishDate.setText(orders.get(position).getExpectedFinishDate());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_id;
        public TextView textView_scopeofwork;
        public TextView textView_jobTitle;

        public TextView textView_area;
        public TextView textView_location;
        public TextView textView_startingDate;
        public TextView textView_expectedFinishDate;

        public ViewHolder(View itemView) {
            super(itemView);
//
            textView_id = itemView.findViewById(R.id.textview_id);
            textView_scopeofwork = itemView.findViewById(R.id.textView_scopeofwork);
            textView_area =itemView.findViewById(R.id.textview_area);
            textView_jobTitle = itemView.findViewById(R.id.textView_jobTitle);
            textView_location = itemView.findViewById(R.id.textView_location);
            textView_startingDate = itemView.findViewById(R.id.textView_startingDate);
            textView_expectedFinishDate = itemView.findViewById(R.id.textView_expectedFinishDate);
        }
    }
}
