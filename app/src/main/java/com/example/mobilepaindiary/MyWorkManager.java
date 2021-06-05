package com.example.mobilepaindiary;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.viewmodel.PainRecordViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyWorkManager extends Worker {
    private PainRecordViewModel  painRecordViewModel;
    public MyWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //        write the message to the database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("PainRecord");
        PainRecord painRecord = new PainRecord();

//        get the data
        Data data = getInputData();
        String pains[] = data.getStringArray("painData");
        if (pains != null){
        painRecord.id = Integer.parseInt(pains[0]);
        painRecord.email= pains[1];
        painRecord.painIntensityLevel = Integer.parseInt(pains[2]);
        painRecord.moodLevel = pains[3];
        painRecord.painLocation = pains[4];
        painRecord.temperature = Float.parseFloat(pains[5]);
        painRecord.humidity = Float.parseFloat(pains[6]);
        painRecord.pressure = Float.parseFloat(pains[7]);
        painRecord.stepsTaken = Integer.parseInt(pains[8]);
        painRecord.totalSteps = Integer.parseInt(pains[9]);
        painRecord.dateOfEntry=pains[10];
        databaseReference.child(String.valueOf(painRecord.id)).setValue(painRecord);
        }



        Log.d("oneTimeWorkRequest", "add the daily record to firebase");
        return Result.success();
    }
}
