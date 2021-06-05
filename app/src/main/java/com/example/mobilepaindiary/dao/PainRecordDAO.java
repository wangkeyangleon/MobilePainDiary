package com.example.mobilepaindiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobilepaindiary.Model.PainRecord;

import java.util.List;

//is an interface with abstract methods and include CRUD operations
@Dao
public interface PainRecordDAO {

    //    get all the data from db
//    observed LiveData will automatically notify the observer on the main thread each time the data changes
    @Query("SELECT * FROM painrecord order by id asc")
    LiveData<List<PainRecord>> getAll();

    @Query("select * from painrecord where id = :painRecordId LIMIT 1")
    PainRecord findByID(int painRecordId);

    @Query("select * from painrecord where date_of_entry = :date")
    PainRecord findByDate(String date);

    @Insert
    void insert(PainRecord painRecord);

    @Update
    void updatePainRecord(PainRecord painRecord);

    @Query("delete from painrecord")
    void deleteAll();




}
