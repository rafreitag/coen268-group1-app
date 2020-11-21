package com.example.fitnessmatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment implements LocationListener {
    private Button btn_find_location, btn_next;
    private EditText et_address;

    private static final int REQUEST_LOCATION = 1;
    protected LocationManager locationManager;
    double latitude, longitude;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        btn_find_location = view.findViewById(R.id.btn_find_location);
        btn_next = view.findViewById(R.id.btn_next);
        et_address = view.findViewById(R.id.et_address);

        btn_find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude == 0 && longitude == 0) {
                    Toast.makeText(getActivity(), "Haven't calculated location yet. Wait and try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                findLocation();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude == 0 && longitude == 0) {
                    Toast.makeText(getActivity(), "Haven't calculated location yet. Wait and try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                nextFragment();
            }
        });

        // get permission
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return view;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);

        return view;
    }

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String lat = String.valueOf(latitude);
        String lng = String.valueOf(longitude);
        Log.i("LocationFragment", "Latitude: " + lat + "Longitude: " + lng);
    }

    public void findLocation() {
        // Get address from location
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        String lat = String.valueOf(latitude);
        String lng = String.valueOf(longitude);
        Log.i("LocationFragment", "Latitude: " + lat + "Longitude: " + lng);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        Log.i("LocationFragment", "Address: " + address);
        et_address.setText(address);
    }

    public void nextFragment() {
        Log.i("LocationFragment", "Next fragment");
//        FormOneFragment formOneFragment = new FormOneFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container, formOneFragment);
//        transaction.commit();
    }
}