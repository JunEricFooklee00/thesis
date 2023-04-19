package com.test.thesis_application.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.thesis_application.Jobinterface;
import com.test.thesis_application.JobsOrderClass;
import com.test.thesis_application.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private final Jobinterface jobinterface;
    List<JobsOrderClass> orders ;

    public OrdersAdapter(List<JobsOrderClass> orders, Jobinterface jobinterface ) {
        this.orders = orders;
        this.jobinterface = jobinterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, jobinterface);

        return  viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_id.setText(orders.get(position).get_id().toString());
        holder.textView_scopeofwork.setText(orders.get(position).getScopeofwork());
        holder.textView_jobTitle.setText(orders.get(position).getJobTitle());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_id;
        public TextView textView_scopeofwork;
        public TextView textView_jobTitle;


        public ViewHolder(View itemView, Jobinterface jobinterface) {
            super(itemView);
//
            textView_jobTitle = itemView.findViewById(R.id.textView_jobTitle); // can be found in jobs orderclass.java
            textView_id = itemView.findViewById(R.id.textview_id);
            textView_scopeofwork = itemView.findViewById(R.id.textView_scopeofwork);
//

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jobinterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                             jobinterface.onItemclick(pos);
                        }
                    }
                }
            });
        }
    }
}
