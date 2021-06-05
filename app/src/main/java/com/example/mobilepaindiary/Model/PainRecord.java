package com.example.mobilepaindiary.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "date_of_entry",unique = true)})
public class PainRecord {

    //    table id will be generated automatically
    @PrimaryKey(autoGenerate = true)
    public int id;

    //    user email when user log in
    @ColumnInfo(name = "email")
    @NonNull
    public String email;

    //    pain intensity from 0-10
    @ColumnInfo(name = "pain_Intensity_level")
    public int painIntensityLevel;
    //  pain location (back,neck,head,knees,hips,abdomen,elbows,shoulders,shins,jaw and facial)
    @ColumnInfo(name = "pain_location")
    public String painLocation;

    //  mood level (very low,low,average,good and very good)
    @ColumnInfo(name = "mood_level")
    public String moodLevel;

    //    steps taken per day
    @ColumnInfo(name = "steps_taken")
    public int stepsTaken;

    @ColumnInfo(name = "total_steps")
    public int totalSteps;

    //    date
    @ColumnInfo(name = "date_of_entry")
    public String dateOfEntry;

    //    temperature
    @ColumnInfo(name = "temperature")
    public float temperature;

    //    humidity
    @ColumnInfo(name = "humidity")
    public float humidity;

    //    pressure
    @ColumnInfo(name = "pressure")
    public float pressure;

    public PainRecord(int id, @NonNull String email, int painIntensityLevel, String painLocation,
                      String moodLevel, int stepsTaken, String dateOfEntry, float temperature,
                      float humidity, float pressure) {
        this.id = id;
        this.email = email;
        this.painIntensityLevel = painIntensityLevel;
        this.painLocation = painLocation;
        this.moodLevel = moodLevel;
        this.stepsTaken = stepsTaken;
        this.dateOfEntry = dateOfEntry;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public PainRecord() {
    }

    @Override
    public String toString() {
        return "PainRecord{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", painIntensityLevel=" + painIntensityLevel +
                ", painLocation='" + painLocation + '\'' +
                ", moodLevel='" + moodLevel + '\'' +
                ", stepsTaken=" + stepsTaken +
                ", dateOfEntry='" + dateOfEntry + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                '}';
    }
}
