package com.example.mobilepaindiary.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//this is the Retrofit model class and should match the Retrofit interface type
public class SearchResponse {

    @SerializedName("main")
    public Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
