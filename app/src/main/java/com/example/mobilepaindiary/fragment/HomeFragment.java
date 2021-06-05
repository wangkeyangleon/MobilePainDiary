package com.example.mobilepaindiary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.MainActivity;
import com.example.mobilepaindiary.databinding.HomeFragmentBinding;
import com.example.mobilepaindiary.retrofit.Main;
import com.example.mobilepaindiary.retrofit.RetrofitClient;
import com.example.mobilepaindiary.retrofit.RetrofitInterface;
import com.example.mobilepaindiary.retrofit.SearchResponse;
import com.example.mobilepaindiary.viewmodel.SharedViewModel;
import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding binding;
    //    get the retrofit configuration
    private static final String API_KEY = "572e5daf5d96194c5d1235c0db465190";
    private String keyword;
    private RetrofitInterface retrofitInterface;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        inflate the view for this fragment
//        set the attachRoot to false so it will return the correct view that was created from inflating the layout file
//        it it's true the layout file specified is inflated and attached to the root of parent and will return an incorrect view
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        //retrofit
        //call this method that will create and return an instance of the Retrofit interface
        retrofitInterface = RetrofitClient.getRetrofitService();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();

        //search the weather of melbourne
        keyword = "Melbourne,au";
        Call<SearchResponse> callAsync = retrofitInterface.getWeather(keyword, API_KEY);
//        makes an async request and invokes callback methods when the response returns
        callAsync.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    Main main = response.body().main;
                    String temp = String.valueOf(main.temp);
                    String pressure = String.valueOf(main.pressure);
                    String humidity = String.valueOf(main.humidity);

//                    use the sharedPreference pass the weather data to other fragment
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("temp", temp);
                    editor.putString("pressure", pressure);
                    editor.putString("humidity", humidity);
                    editor.apply();

                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
                    String currentDate = simpleDateFormat.format(date);

                    binding.date.setText("Date:"+currentDate);

//   display the weather to the screen
                    binding.City.setText("Melbourne");

//                    binding.weather.setText(String.format("City:%s\nTemperature:%s\nPressure:%s\nHumidity:%s", "Melbourne"
//                            , temp, pressure, humidity));
                    binding.weather.setText("Temperature: "+temp+"\u2103");
                    binding.homePressure.setText("Pressure: "+pressure);
                    binding.homeHumidity.setText("Humidity: "+humidity);

                } else {
                    Log.i("Error", "Response failed");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT);

            }
        });

        //set the weather output format
        //binding.weather.setText();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
