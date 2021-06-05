package com.example.mobilepaindiary.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.repository.PainRecordRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//use LiveData and ViewModel to achieve the observer design pattern(observed the live data)
//holds all of the data needed for the UI
//UI is notified of changes using observation
public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository painRecordRepository;
    private LiveData<List<PainRecord>> allPainRecords;

    public PainRecordViewModel(Application application) {
        super(application);
        painRecordRepository = new PainRecordRepository(application);
        allPainRecords = painRecordRepository.getAllPainRecords();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByIDFuture(final int painRecordId) {
        return painRecordRepository.findByIDFuture(painRecordId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByDate(final String date){
        return painRecordRepository.findByDate(date);
    }

    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }

    public void insert(PainRecord painRecord) {
        painRecordRepository.insert(painRecord);
    }

    public void update(PainRecord painRecord) {
        painRecordRepository.updatePainRecord(painRecord);
    }

    public void deleteAll(){
        painRecordRepository.deleteAll();
    }


}
