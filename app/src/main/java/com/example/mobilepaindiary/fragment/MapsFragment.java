package com.example.mobilepaindiary.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mobilepaindiary.MainActivity;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.MapsFragmentBinding;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment {
    private MapsFragmentBinding binding;
    private MapView mapView;


    public MapsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String token = getString(R.string.mapbox_access_token);
        //        different with the activity
        Mapbox.getInstance(getContext(), token);
        binding = MapsFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    //    display the map
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = binding.address.getText().toString();
                List<Address> list = getAddress(address);
                System.out.println(list);
//                get latitude and longitude
                if (!list.isEmpty()) {
                    Address address1 = list.get(0);
                    double latitude = address1.getLatitude();
                    double longitude = address1.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull MapboxMap mapboxMap) {
                            mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    CameraPosition position = new CameraPosition.Builder().target(latLng).zoom(13).build();
                                    mapboxMap.setCameraPosition(position);
                                    mapboxMap.addMarker(new MarkerOptions().position(latLng));
                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Please input the correct address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //    get the longitude and latitude from the address information
    private List<Address> getAddress(String address) {
        System.out.println("address: " + address);
        List<Address> list = null;
        try {
            if (address != null) {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                list = geocoder.getFromLocationName(address, 1);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        mapView.onDestroy();
    }


}
