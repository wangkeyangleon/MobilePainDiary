package com.example.mobilepaindiary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> userEmail;
    private MutableLiveData<String> temperature;
    private MutableLiveData<String> pressure;
    private MutableLiveData<String> humidity;

    public SharedViewModel() {
        userEmail = new MutableLiveData<>();
        temperature = new MutableLiveData<>();
        pressure = new MutableLiveData<>();
        humidity = new MutableLiveData<>();
    }

    public MutableLiveData<String> getPressure(){
        return pressure;
    }

    public void setPressure(String pressure){
        this.pressure.setValue(pressure);
    }

    public MutableLiveData<String> getHumidity(){
        return humidity;
    }

    public void setHumidity(String humidity){
        this.humidity.setValue(humidity);
    }

    public MutableLiveData<String> getTemperature(){
        return temperature;
    }

    public void setTemperature(String temperature){
        this.temperature.setValue(temperature);
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String email) {
        userEmail.setValue(email);
    }
}
