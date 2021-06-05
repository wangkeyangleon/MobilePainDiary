package com.example.mobilepaindiary.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.databinding.LineChartFragmentBinding;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.R.layout.simple_spinner_item;


public class SubFragment3 extends Fragment {
    private PainRecordViewModel painRecordViewModel;
    private LineChartFragmentBinding binding;
    //    get the range of pain levels
    private HashMap<String, Integer> painLevels = new HashMap<>();
    //    get the range of weather
    private HashMap<String, Float> weathers = new HashMap<>();
    //                get the date and pain level
    HashMap<Date, Integer> painHashMap = new HashMap<>();
    //    get the date and weather
    HashMap<Date, Float> weatherHashMap = new HashMap<>();
    //        init some attribute for line chart
    private LineChart lineChart;
    //        x axis
    private XAxis xAxis;
    //        left y axis
    private YAxis leftYAxis;
    //        right y axis
    private YAxis rightYAxis;
    //        legend
    private Legend legend;
    //        boundary line
    private LimitLine limitLine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()).create(PainRecordViewModel.class);
        binding = LineChartFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        //        set weather spinner
        List<String> weather = new ArrayList<>();
        weather.add("temperature");
        weather.add("humidity");
        weather.add("pressure");
        final ArrayAdapter<String> weatherArray = new ArrayAdapter<String>(getContext(), simple_spinner_item, weather);
        binding.weather.setAdapter(weatherArray);
        Spinner weatherSpinner = binding.weather;


//        generate the line chart
        binding.generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePicker startDate = binding.startDate;
                DatePicker endDate = binding.endDate;
                //        get the user selected
                String userWeather = weatherSpinner.getSelectedItem().toString();
//                        get the user selected start date
                Calendar startCalendar = Calendar.getInstance();
                int startYear = startDate.getYear();
                int startMonth = startDate.getMonth();
                int startDay = startDate.getDayOfMonth();
                startCalendar.set(Calendar.YEAR, startYear);
                startCalendar.set(Calendar.MONTH, startMonth);
                startCalendar.set(Calendar.DAY_OF_MONTH, startDay);


//                        get the user selected end date
                Calendar endCalendar = Calendar.getInstance();
                int endYear = endDate.getYear();
                int endMonth = endDate.getMonth();
                int endDay = endDate.getDayOfMonth();
                endCalendar.set(Calendar.YEAR, endYear);
                endCalendar.set(Calendar.MONTH, endMonth);
                endCalendar.set(Calendar.DAY_OF_MONTH, endDay);


//        get all data from db
                painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
                    @Override
                    public void onChanged(List<PainRecord> painRecords) {
                        System.out.println("user select:" + userWeather);

                        if (startCalendar.getTime().after(endCalendar.getTime())) {
                            Toast.makeText(getContext(), "Start date can not after End Date", Toast.LENGTH_SHORT).show();

                        } else {
                            for (PainRecord painRecord : painRecords) {
                                //                        save the data when between the date
                                if (userWeather.equals("temperature")) {
                                    painLevels.put(painRecord.dateOfEntry, painRecord.painIntensityLevel);
                                    weathers.put(painRecord.dateOfEntry, painRecord.temperature);

                                } else if (userWeather.equals("humidity")) {
                                    painLevels.put(painRecord.dateOfEntry, painRecord.painIntensityLevel);
                                    weathers.put(painRecord.dateOfEntry, painRecord.humidity);
                                } else {
                                    painLevels.put(painRecord.dateOfEntry, painRecord.painIntensityLevel);
                                    weathers.put(painRecord.dateOfEntry, painRecord.pressure);
                                }
                            }
                        }
                    }
                });
                //                        generate line chart
                lineChart = binding.lineChart;
                initChart(lineChart);
                try {
                    painHashMap = getPainLevels(painLevels, startCalendar.getTime(), endCalendar.getTime());
                    weatherHashMap = getWeather(weathers, startCalendar.getTime(), endCalendar.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                showLineChart(painHashMap, weatherHashMap,
                        "pain levels", getResources().getColor(android.R.color.holo_blue_bright),
                        userWeather, getResources().getColor(android.R.color.holo_orange_light));
//                showAnotherLine(weatherHashMap, userWeather, getResources().getColor(android.R.color.holo_orange_light));

            }
        });

//        show the result of correlation
        binding.testCorrelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.showCorrelation.setText(testCorrelation());


            }
        });

        return view;
    }

    //    init chart
    private void initChart(LineChart lineChart) {
//        whether to show the grid
        lineChart.setDrawGridBackground(false);
//        whether to show the border
        lineChart.setDrawBorders(true);
        //whether can be drag
        lineChart.setDragEnabled(false);
        //whether can be touch
        lineChart.setTouchEnabled(true);
        //set xy animation
        lineChart.animateY(2500);
        lineChart.animateX(1500);

//        set xy axis
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        //x axis show at the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //ensure the y value begin with 0
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);

//        set legend
        legend = lineChart.getLegend();
        //set show type line circle square empty
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //display position at the left bottom
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //whether show in the line chart
        legend.setDrawInside(false);
    }

    //    init lineDataSet
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //set the line is solid and hollow
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //set the line
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //Set the curve to be displayed as a smooth curve (if not set, the default polyline)
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    //show line chart
    public void showLineChart(HashMap<Date, Integer> painLevels, HashMap<Date, Float> weatherHashMap, String name, int color, String name1, int color1) {
        List<Entry> entries = new ArrayList<>();
        int i = 0;
        List<Date> xValues = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd-MM-yyyy");
//        iterating pain level hashMap
        for (Map.Entry<Date, Integer> entry : painLevels.entrySet()) {
            Entry entry1 = new Entry(i, entry.getValue());
            i++;
            entries.add(entry1);
            xValues.add(entry.getKey());
        }

//    every lineDateSet represents a line
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
//        LineData lineData = new LineData(lineDataSet);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        List<String> xStringDate = new ArrayList<>();
        SimpleDateFormat sf1 = new SimpleDateFormat("ddMMyyyy");
//                change the date to string type
        for (Date dates : xValues) {
            xStringDate.add(sf1.format(dates));
        }
//        lineChart.setData(lineData);

//        add another line
        int a = 0;
        List<Entry> weatherEntry = new ArrayList<>();
        //        iterating weather hashMap
        for (Map.Entry<Date, Float> entry : weatherHashMap.entrySet()) {
            Entry entry1 = new Entry(a, entry.getValue());
            a++;
            weatherEntry.add(entry1);
        }
//        create weather line chart
        LineDataSet lineDataSet1 = new LineDataSet(weatherEntry, name1);
        initLineDataSet(lineDataSet1, color1, LineDataSet.Mode.LINEAR);
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        lineDataSetList.add(lineDataSet);
        lineDataSetList.add(lineDataSet1);
        LineData lineData = new LineData(lineDataSetList);
        lineChart.setData(lineData);

        //        change the x axis to date
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xStringDate));
        lineChart.invalidate();
    }


    //    get the pain level and date with the user select range
    private HashMap<Date, Integer> getPainLevels(HashMap<String, Integer> hashMap, Date startDate, Date endDate) throws ParseException {
        HashMap<Date, Integer> painLevels = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd-MM-yyyy");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            Date date;
            date = simpleDateFormat.parse(entry.getKey());
            if (date.before(endDate) && date.after(startDate)) {
                painLevels.put(date, entry.getValue());
            }

        }
        return painLevels;

    }

    //    get the weather and date with the user select range
    private HashMap<Date, Float> getWeather(HashMap<String, Float> hashMap, Date startDate, Date endDate) {
        HashMap<Date, Float> weatherDates = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//        pay attention to this format
        simpleDateFormat.applyPattern("dd-MM-yyyy");
        for (Map.Entry<String, Float> entry : hashMap.entrySet()) {
            Date date = null;
            try {
                date = simpleDateFormat.parse(entry.getKey());
            } catch (ParseException e) {
                System.out.println(e.getErrorOffset());
            }
            if (date.before(endDate) && date.after(startDate)) {
                weatherDates.put(date, entry.getValue());
            }

        }
        return weatherDates;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //    test correlation
    public String testCorrelation() {
//        two column array: 1st column = first array, second column = second array
        double data[][] = new double[painHashMap.size()][2];
        List<Double> dataList = new ArrayList<>();
//        add pain and weather data to dataList
        List<Double> painList = new ArrayList<>();
        for (Map.Entry<Date,Integer> entry:painHashMap.entrySet()){
            painList.add(Double.valueOf(entry.getValue()));
        }

        List<Double> weatherList = new ArrayList<>();
        for (Map.Entry<Date,Float> entry:weatherHashMap.entrySet()){
            weatherList.add(Double.valueOf(entry.getValue()));
        }
        int q = 0;
        int w = 0;
        System.out.println("pain:"+painHashMap.size());
        for (int i = 0; i < painHashMap.size()*2; i++) {
            if (i%2==0){
                dataList.add(painList.get(q));
                q++;
            }else {
                dataList.add(weatherList.get(w));
                w++;
            }

        }

        int a = 0;
//        insert data data
        for (int i = 0;i<painHashMap.size();i++){
            for (int k = 0; k <2 ; k++) {
                data[i][k] = dataList.get(a);
                a++;
            }
        }

//        create a realMatrix
        RealMatrix m = MatrixUtils.createRealMatrix(data);

//        measure all correlation test: x-x,x-y,y-x,y-x
        for (int i = 0; i < m.getColumnDimension(); i++)
            for (int j = 0; j < m.getColumnDimension(); j++) {
                PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
                double cor = pearsonsCorrelation.correlation(m.getColumn(i), m.getColumn(j));
                System.out.println(i + "," + j + "=[" + String.format(".%2f",
                        cor) + "," + "]");
            }

//        correlation test (another method):x-y
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(m);
        RealMatrix corM = pearsonsCorrelation.getCorrelationMatrix();

//        significant test of the correlation coefficient (p-value)
        RealMatrix pM = pearsonsCorrelation.getCorrelationPValues();
        return ("p value:" + pM.getEntry(0, 1) + "\n" + " correlation: " + corM.getEntry(0, 1));

    }


}

