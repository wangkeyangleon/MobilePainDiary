package com.example.mobilepaindiary.fragment;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.MyReceiver;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.MoodImageBinding;
import com.example.mobilepaindiary.databinding.PainDataEntryFragmentBinding;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.example.mobilepaindiary.viewmodel.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static android.R.layout.simple_spinner_item;

//display data and forward on UI events
public class PainDataFragment extends Fragment {
    private PainDataEntryFragmentBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private FirebaseAuth firebaseAuth;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private Integer painLevel = 0;
    private List<Map<String,Object>> moodList = new ArrayList<>();
    private MoodImageBinding imageBinding;
    private String moodLevel = "average";

    public PainDataFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PainDataEntryFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(PainRecordViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();

        timePicker = binding.alarm;
//        set the timePicker is 24 hours
        timePicker.setIs24HourView(true);
        //        set alarm
        binding.alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyReceiver.class);
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute-2);

               //        init alarm manager
                alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,intent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                Toast.makeText(getActivity().getApplicationContext(),"Successfully set the alarm",Toast.LENGTH_SHORT).show();
//                binding.message.setText("Successfully set the alarm");

            }
        });


//        get the user email
        String email = firebaseAuth.getCurrentUser().getEmail();

//        get the weather data from the home fragment
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        String temp = sharedPreferences.getString("temp", null);
        String pressure = sharedPreferences.getString("pressure", null);
        String humidity = sharedPreferences.getString("humidity", null);

        binding.painLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.seekBarText.setText("Level:"+Integer.toString(progress));
                painLevel = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        set location_pain spinner
        List<String> painLocations = new ArrayList<String>();
        painLocations.add("back");
        painLocations.add("neck");
        painLocations.add("head");
        painLocations.add("knees");
        painLocations.add("hips");
        painLocations.add("abdomen");
        painLocations.add("elbows");
        painLocations.add("shoulders");
        painLocations.add("shins");
        painLocations.add("jaw");
        painLocations.add("facial");
        final ArrayAdapter<String> locationArray = new ArrayAdapter<String>(getContext(), simple_spinner_item, painLocations);
        binding.locationPain.setAdapter(locationArray);
        Spinner locationSpinner = binding.locationPain;

//mood rating
        binding.moodRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating==1){
                    moodLevel = "very low";
                    binding.moodValue.setText(moodLevel);
                }else if (rating ==2){
                    moodLevel = "low";
                    binding.moodValue.setText(moodLevel);
                }else if (rating ==3){
                    moodLevel = "average";
                    binding.moodValue.setText(moodLevel);
                }else if (rating ==4){
                    moodLevel = "good";
                    binding.moodValue.setText(moodLevel);
                }else {
                    moodLevel = "very good";
                    binding.moodValue.setText(moodLevel);
                }
            }
        });


//        this is just for test


//        add PainData
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //        get the pain location that user selected
                String painLocation = locationSpinner.getSelectedItem().toString();


//                get the user's goal
                String stepsTaken = binding.goal.getText().toString();
                String totalSteps = binding.totalSteps.getText().toString();
                if (!painLocation.isEmpty() && !stepsTaken.isEmpty() && !totalSteps.isEmpty()) {
                    //                use sharedPreferences to save the user


                    PainRecord painRecord = new PainRecord();
                    painRecord.email = email;
                    painRecord.temperature = Float.parseFloat(temp);
                    painRecord.pressure = Float.parseFloat(pressure);
                    painRecord.humidity = Float.parseFloat(humidity);
                    painRecord.painIntensityLevel = painLevel;
                    painRecord.painLocation = painLocation;
                    painRecord.moodLevel = moodLevel;
                    painRecord.stepsTaken = Integer.parseInt(stepsTaken);
                    painRecord.totalSteps = Integer.parseInt(totalSteps);
//                    get the time when user add the data to the db
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                    simpleDateFormat.applyPattern("dd-MM-YYYY");
                    Date date = new Date();
                    String userDate = simpleDateFormat.format(date);
                    painRecord.dateOfEntry = userDate;
//                    check whether the user already add the data today
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        CompletableFuture<PainRecord> painRecordCompletableFuture = painRecordViewModel.findByDate(userDate);
//                        get the painRecord object return from CompletableFuture
                        painRecordCompletableFuture.thenApply(painRecord1 -> {
                            if (painRecord1 != null) {
                                binding.message.setText("Sorry,you already add the data today");
                                Toast.makeText(getContext(), "Sorry,you already add the data today", Toast.LENGTH_SHORT).show();
                            } else {
                                //add the painRecord to db
                                painRecordViewModel.insert(painRecord);
                                binding.message.setText("Successfully add the data");
                                Toast.makeText(getContext(), "Successfully add the data", Toast.LENGTH_SHORT).show();
                            }
                            return painRecord1;
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Please input the required data", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        update today's data
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                simpleDateFormat.applyPattern("dd-MM-YYYY");
                Date date = new Date();
                String userDate = simpleDateFormat.format(date);

//                get PainIntensityLevel,painLocation,mood and steps from spinner

                String painLocation = locationSpinner.getSelectedItem().toString();


                String stepsTaken = binding.goal.getText().toString();

                String totalSteps = binding.totalSteps.getText().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    CompletableFuture<PainRecord> painRecordCompletableFuture = painRecordViewModel.findByDate(userDate);
                    painRecordCompletableFuture.thenApply(painRecord -> {
                        if (painRecord != null) {
                            painRecord.painIntensityLevel = painLevel;
                            painRecord.painLocation = painLocation;
                            painRecord.moodLevel = moodLevel;
                            if (!stepsTaken.isEmpty()) {
                                painRecord.stepsTaken = Integer.parseInt(stepsTaken);
                            }
                            if (!totalSteps.isEmpty()){
                                painRecord.totalSteps = Integer.parseInt(totalSteps);
                            }
                            painRecordViewModel.update(painRecord);
                            binding.message.setText("Successfully update the data");
                            Toast.makeText(getContext(), "Successfully update the data", Toast.LENGTH_SHORT).show();

                        } else {
                            binding.message.setText("Sorry,you did not add data today");
                            Toast.makeText(getContext(), "Sorry,you did not add data today", Toast.LENGTH_SHORT).show();

                        }
                        return painRecord;
                    });

                }


            }


        });


        return view;
    }

//    manually

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
