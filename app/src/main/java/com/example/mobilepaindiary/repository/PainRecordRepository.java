package com.example.mobilepaindiary.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.dao.PainRecordDAO;
import com.example.mobilepaindiary.database.PainRecordDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

//provide nice API for data access to the rest of the application and reduce dependencies Room classes
// and manage queries better
public class PainRecordRepository {
    private PainRecordDAO painRecordDAO;
    private LiveData<List<PainRecord>> allPainRecords;

    public PainRecordRepository(Application application) {
//        get a singleton database instance
        PainRecordDatabase painRecordDatabase = PainRecordDatabase.getInstance(application);
        painRecordDAO = painRecordDatabase.painRecordDAO();
        allPainRecords = painRecordDAO.getAll();
    }

//    room executes this query on a separate thread

    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }

    public void insert(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.insert(painRecord);
            }
        });
    }


    public void updatePainRecord(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.updatePainRecord(painRecord);
            }
        });
    }

    public void deleteAll(){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDAO.deleteAll();
            }
        });
    }



//    this method ensures the findByIDFuture is completed before the update operation starts
//    using CompletableFuture, this background job completes and returns the result(a painRecord if it exists in the db)
//    in an asynchronous way so the painRecord object returned won't be null when the update operations is executed later in the
//    PainDataFragment
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByIDFuture(final int painRecordId) {
//     supplyAsync means we want to return some results
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return painRecordDAO.findByID(painRecordId);
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByDate(final String date){
//        want to return some results from a background that runs asynchronously
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return painRecordDAO.findByDate(date);
            }
        },PainRecordDatabase.databaseWriteExecutor);
    }
}
