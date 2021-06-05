package com.example.mobilepaindiary.retrofit;

import com.google.gson.annotations.SerializedName;

public class Main {

//    get the temperature
    @SerializedName("temp")
    public float temp;

//    get the pressure
    @SerializedName("pressure")
    public float pressure;

//    get the humidity
    @SerializedName("humidity")
    public float humidity;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
