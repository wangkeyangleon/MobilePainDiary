package com.example.mobilepaindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.adapter.RecyclerViewAdapter;
import com.example.mobilepaindiary.databinding.ActivityMainBinding;
import com.example.mobilepaindiary.retrofit.RetrofitClient;
import com.example.mobilepaindiary.retrofit.RetrofitInterface;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.example.mobilepaindiary.viewmodel.SharedViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private PainRecordViewModel painRecordViewModel;
    private PainRecord painRecordWorkManager;
    private String[] pains;
    //        set work Manager to upload data
    Constraints constraints = new Constraints.Builder().setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.UNMETERED).build();
    private OneTimeWorkRequest oneTimeWorkRequest;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBar.toolbar);

        //        set the 10pm to update data
        Calendar phoneTime = Calendar.getInstance();
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY,22);
        alarmTime.set(Calendar.MINUTE,0);
        alarmTime.set(Calendar.SECOND,0);
        if(alarmTime.before(phoneTime))

        {
            alarmTime.add(Calendar.HOUR_OF_DAY, 24);
        }

        long timeGap = alarmTime.getTimeInMillis() - phoneTime.getTimeInMillis();

        //        get the data from room db
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance((Application) getApplicationContext())
                .create(PainRecordViewModel.class);

        painRecordViewModel.getAllPainRecords().observe((LifecycleOwner) this, new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
//                assign the data to painRecordWorkManager
                painRecordWorkManager = painRecords.get(painRecords.size() - 1);
                pains = new String[]{String.valueOf(painRecordWorkManager.id), painRecordWorkManager.email,
                        String.valueOf(painRecordWorkManager.painIntensityLevel), painRecordWorkManager.moodLevel
                        , painRecordWorkManager.painLocation, String.valueOf(painRecordWorkManager.temperature),
                        String.valueOf(painRecordWorkManager.humidity), String.valueOf(painRecordWorkManager.pressure),
                        String.valueOf(painRecordWorkManager.stepsTaken),
                        String.valueOf(painRecordWorkManager.totalSteps), painRecordWorkManager.dateOfEntry};
                Data data = new Data.Builder().putStringArray("painData", pains).build();
                System.out.println(pains[1]);
                oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorkManager.class).setInputData(data)
                        .setConstraints(constraints).setInitialDelay(timeGap, TimeUnit.MILLISECONDS).build();
                //       get work manager instance
                //        enqueue the oneTimeRequest to work manager
                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }

        });





//        workManager.enqueue(oneTimeWorkRequest);


//        controls how the Navigation button is displayed and what action is triggered when it is tapped
        //setOpenableLayout(binding.drawerLayout) decide the icon
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_pain_data_entry,
                R.id.nav_daily_record, R.id.nav_reports, R.id.nav_maps).setOpenableLayout(binding.drawerLayout).build();

//        is responsible for attaching fragments to their host activity and detaching them when the fragment when the fragment
//        is no longer in use
        FragmentManager fragmentManager = getSupportFragmentManager();
//        NacHost is an important part of the navigation component that provides a container for navigation to occur
//        NavHostFragment implements the NavHost interface
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
//retrieve the NavController directly from the NavHostFragment by calling getNavController()
        NavController navController = navHostFragment.getNavController();

//        sets up a NavigationView for use with a NavController
//        NavigationUI class is used with the Navigation component
        NavigationUI.setupWithNavController(binding.navView, navController);
//        sets up a Toolbar for use with a NavController
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController, appBarConfiguration);


    }

    public class MyWorkManager1 extends Worker {

        public MyWorkManager1(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            //        write the message to the database
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("PainRecord");

//        get the data from room db
            painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                    .create(PainRecordViewModel.class);

            painRecordViewModel.getAllPainRecords().observe((LifecycleOwner) this, new Observer<List<PainRecord>>() {
                @Override
                public void onChanged(List<PainRecord> painRecords) {
                    databaseReference.child(String.valueOf(painRecords.get(painRecords.size() - 1).id))
                            .setValue(painRecords.get(painRecords.size() - 1));
                }
            });


            Log.d("oneTimeWorkRequest", "add the daily record to firebase");
            return Result.success();

        }
    }
}