package com.test.thesis_application;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class laborAdapter extends RecyclerView.Adapter<laborAdapter.ViewHolder> {


    private List<JSONObject> results;
    private List<JSONObject> mCheckedItems = new ArrayList<>();

    private CompoundButton.OnCheckedChangeListener mListener;

    public laborAdapter(List<JSONObject> results, List<JSONObject> mCheckedItems, CompoundButton.OnCheckedChangeListener mListener) {
        this.results = results;
        this.mCheckedItems = mCheckedItems;
        this.mListener = mListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public String imgpath;
        public TextView idTextView;
        public TextView locationTextView;
        public TextView job;
        public CheckBox employee;
        public ImageView emp_Avatar;
        public ViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.tv_employee_id);
            locationTextView = itemView.findViewById(R.id.tv_employee_location);
            job = itemView.findViewById(R.id.tv_jobtype);
            employee = itemView.findViewById(R.id.checkBox);
            emp_Avatar = itemView.findViewById(R.id.employee_avatar);

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
            JSONObject employeedata = results.get(position);
            holder.imgpath = employeedata.getString("avatar");
            holder.employee.setText(employeedata.getString("name") );
            holder.job.setText(employeedata.getString("jobType"));
            holder.idTextView.setText("ID: " + employeedata.getString("employeeId"));
            holder.locationTextView.setText("Location: " + employeedata.getString("address"));
            holder.employee.setOnCheckedChangeListener(null); // To prevent triggering the listener when the view is recycled
            holder.employee.setChecked(mCheckedItems.contains(employeedata));

            Picasso.get()
                    .load(holder.imgpath).transform(new CropCircleTransformation())
                    .fit()
                    .centerCrop()
                    .into(holder.emp_Avatar);
            holder.employee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCheckedItems.add(employeedata);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(mCheckedItems.toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String employeeId = null;
                            try {
                                employeeId = jsonObject.getString("employeeId");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String name = null;
                            try {
                                name = jsonObject.getString("name");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String address = null;
                            try {
                                address = jsonObject.getString("address");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String profile = null;
                            try {
                                profile = jsonObject.getString("jobType");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String distance = null;
                            try {
                                distance = jsonObject.getString("distance");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Log.v("mchecked", "Checked Item " + i + ": " + employeeId + ", "  + address + ", " + profile + ", " + distance);
                        }

//                        Log.v("mchecked","dewow "+ mCheckedItems.toString() ); //how do i print the mcheckeditems here
                    } else {
                        mCheckedItems.remove(employeedata);
                    }
                }
            });


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
    public List<JSONObject> getCheckedItems() {
        return mCheckedItems;
    }
}