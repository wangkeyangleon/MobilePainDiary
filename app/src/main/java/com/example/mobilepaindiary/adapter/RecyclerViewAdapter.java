package com.example.mobilepaindiary.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.databinding.RvLayoutBinding;

import java.util.List;
//provides methods to create ViewHolder objects as needed and bind them with data
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<PainRecord> painRecords;

    public RecyclerViewAdapter(List<PainRecord> painRecords) {
        this.painRecords = painRecords;
    }

//    this method creates a new view holder that is constructed with a new View inflated from a layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvLayoutBinding binding = RvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }


//this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PainRecord painRecord = painRecords.get(position);
        holder.binding.dataId.setText(Integer.toString(painRecord.id));
        holder.binding.email.setText(painRecord.email);
        holder.binding.date.setText(painRecord.dateOfEntry);
        holder.binding.temperature.setText(Float.toString(painRecord.temperature));
        holder.binding.pressure.setText(Float.toString(painRecord.pressure));
        holder.binding.humidity.setText(Float.toString(painRecord.humidity));
        holder.binding.intensityLevel.setText(Integer.toString(painRecord.painIntensityLevel));
        holder.binding.painLocation.setText(painRecord.painLocation);
        holder.binding.mood.setText(painRecord.moodLevel);
        holder.binding.steps.setText(Integer.toString(painRecord.stepsTaken));

    }

    @Override
    public int getItemCount() {
        return painRecords.size();
    }

    // contains the layout for each items in the list
    public class ViewHolder extends RecyclerView.ViewHolder {
        private RvLayoutBinding binding;

        public ViewHolder(RvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
