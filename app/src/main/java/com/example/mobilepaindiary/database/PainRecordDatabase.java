package com.example.mobilepaindiary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobilepaindiary.Model.PainRecord;
import com.example.mobilepaindiary.dao.PainRecordDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//this is an abstract class and must extend RoomDb
//exportSchema sets false means that does not want to keep a history of versions
@Database(entities = {PainRecord.class}, version = 3, exportSchema = false)
public abstract class PainRecordDatabase extends RoomDatabase {

    //this class must contain an abstract method with no argument, return the DAO
//    ??
    public abstract PainRecordDAO painRecordDAO();

//    means this is a singleton pattern to prevent having multiple instances of the db opened at the same time
    private static PainRecordDatabase INSTANCE;

    //    create an ExecutorService with a fixed thread pool that allows run db operation asynchronously on a background thread
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //    a synchronized method in a multi threaded environment means
    //    that two threads are not allowed to access data at the same time
//    getInstance() returns the singleton
    public static synchronized PainRecordDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PainRecordDatabase.class, "PainRecordDatabase")
                    .fallbackToDestructiveMigration()//when schema or version changed,clear the data and
                    // recreate a new db based on the schema
                    .build();

        }

        return INSTANCE;

    }

}
