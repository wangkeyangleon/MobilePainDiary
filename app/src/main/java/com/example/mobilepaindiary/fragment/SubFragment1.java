package com.example.mobilepaindiary.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

//this fragment is for pain location pie chart
public class SubFragment1 extends Fragment {
    private PainRecordViewModel painRecordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PieChart pieChart = new PieChart(getActivity());
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(PainRecordViewModel.class);
//        get all the pain location information
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                List<PieEntry> pieEntries = new ArrayList<>();
                int back = 0;
                int neck = 0;
                int head = 0;
                int knees = 0;
                int hips = 0;
                int abdomen = 0;
                int elbows = 0;
                int shoulders = 0;
                int shins = 0;
                int jaw = 0;
                int facial = 0;
                for (PainRecord pain : painRecords) {
                    switch (pain.painLocation) {
                        case "back":
                            back++;
                            break;
                        case "neck":
                            neck++;
                            break;
                        case "head":
                            head++;
                            break;
                        case "knees":
                            knees++;
                            break;
                        case "hips":
                            hips++;
                            break;
                        case "abdomen":
                            abdomen++;
                            break;
                        case "elbows":
                            elbows++;
                            break;
                        case "shoulders":
                            shoulders++;
                            break;
                        case "shins":
                            shins++;
                            break;
                        case "jaw":
                            jaw++;
                            break;
                        case "facial":
                            facial++;
                            break;
                    }
                }

                //                        calculate the total number of pain location
                int totalNumber = back + neck + head + knees + hips + abdomen + elbows + shoulders + shins + jaw + facial;
                if (totalNumber == 0) {
                    Toast.makeText(getContext(), "Sorry,you haven't add any value", Toast.LENGTH_SHORT).show();
                } else {
                    pieEntries.add(new PieEntry(back, "back"));
                    pieEntries.add(new PieEntry(neck, "neck"));
                    pieEntries.add(new PieEntry(head, "head"));
                    pieEntries.add(new PieEntry(knees, "knees"));
                    pieEntries.add(new PieEntry(hips, "hips"));
                    pieEntries.add(new PieEntry(abdomen, "abdomen"));
                    pieEntries.add(new PieEntry(elbows, "elbows"));
                    pieEntries.add(new PieEntry(shoulders, "shoulders"));
                    pieEntries.add(new PieEntry(shins, "shins"));
                    pieEntries.add(new PieEntry(jaw, "jaw"));
                    pieEntries.add(new PieEntry(facial, "facial"));
                }
                List<PieEntry> pieEntriesNoZero = new ArrayList<>();
                for (PieEntry pie:pieEntries
                ) {
                    if (pie.getValue()!=0){
                        pieEntriesNoZero.add(pie);
                    }
                }
                System.out.println("total number: " + totalNumber);
                //                set the pie category
                PieDataSet pieDataSet = new PieDataSet(pieEntriesNoZero, "Pain Location");
//                set the color for different pie
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(pieDataSet);
                //                        set the text value size

                data.setValueTextSize(10f);
//                show the percentage
                data.setValueFormatter(new PercentFormatter(pieChart));
                pieChart.setUsePercentValues(true);

//                        add description label
                Description description = new Description();
                description.setText("Pain Location Pie Chart");
//                        description.setTextSize(10f);
                pieChart.setDescription(description);

//                        add the data to the pie chart
                pieChart.setData(data);
//                        remove the medium cycle
                pieChart.setHoleRadius(0);
//                        do not display the medium cycle
                pieChart.setTransparentCircleAlpha(0);

//                        add some animation
                pieChart.animateXY(3000, 300);
                pieChart.setVisibility(View.VISIBLE);
                pieChart.invalidate();

            }
        });



        return pieChart;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
