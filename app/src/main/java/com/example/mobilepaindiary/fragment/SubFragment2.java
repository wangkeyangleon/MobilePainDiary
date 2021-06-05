package com.example.mobilepaindiary.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.databinding.StepFragmentBinding;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//this fragment is for steps taken pie chart
public class SubFragment2 extends Fragment {
    private PainRecordViewModel painRecordViewModel;
    private int goal;
    private int totalSteps;
    private StepFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = StepFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(PainRecordViewModel.class);
        PieChart pieChart = binding.pieChart;
        //get date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd-MM-YYYY");
        Date date = new Date();
        String userDate = simpleDateFormat.format(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CompletableFuture<PainRecord> painRecordCompletableFuture = painRecordViewModel.findByDate(userDate);
            painRecordCompletableFuture.thenApply(painRecord -> {
                if (painRecord != null) {
                    goal = painRecord.stepsTaken;
                    totalSteps = painRecord.totalSteps;
                } else {
                    Toast.makeText(getContext(), "Sorry you haven't wrote record today", Toast.LENGTH_SHORT).show();
                }
                return painRecord;
            });

        }
//                get the steps that user set

//                create the pie chart to shows the steps entered for the current date and the remaining steps
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(totalSteps, "Steps"));
        int remainingSteps = goal - totalSteps;
        if (remainingSteps < 0) {
            binding.stepMessage.setText("you already finish today's goal!");

        } else {
            pieEntries.add(new PieEntry(remainingSteps, "Remaining steps"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Steps");
//                set the color for different pie
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(pieDataSet);
            //                        set the text value size

            data.setValueTextSize(10f);
            //                        show the percentage
            data.setValueFormatter(new PercentFormatter(pieChart));
            pieChart.setUsePercentValues(true);

//                        add description label
            Description description = new Description();
            description.setText("Steps taken Pie Chart");
            //                        add the data to the pie chart
            pieChart.setData(data);

//                        add some animation
            pieChart.animateXY(3000, 300);
            pieChart.setVisibility(View.VISIBLE);
            pieChart.invalidate();
        }

        return view;
    }
}
