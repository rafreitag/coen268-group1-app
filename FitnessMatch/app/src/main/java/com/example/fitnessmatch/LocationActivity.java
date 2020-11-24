package com.example.fitnessmatch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private Button btn_find_location, btn_next;
    private EditText et_address;

//    //From -> the first coordinate from where we need to calculate the distance
//    private double fromLatitude = 37.341;
//    private double fromLongitude = -121.94;
//
//    //To -> the second coordinate to where we need to calculate the distance
//    private double toLatitude = 37.335;
//    private double toLongitude = -121.881;

    private static final int REQUEST_LOCATION = 1;
    protected LocationManager locationManager;
    Location location; // location
    double latitude, longitude;

    Geocoder geocoder;
    List<Address> addresses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        btn_find_location = findViewById(R.id.btn_find_location);
        btn_next = findViewById(R.id.btn_next);
        et_address = findViewById(R.id.et_address);

        btn_find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAddressFromLocation();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment();
            }
        });

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

    public void getLocation() {
        // get permission
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) this);

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String lat = String.valueOf(latitude);
        String lng = String.valueOf(longitude);
        Log.i("LocationFragment", "Latitude: " + lat + "Longitude: " + lng);
    }

    public void findAddressFromLocation() {
        getLocation();

        // Get address from location
        geocoder = new Geocoder(this, Locale.getDefault());

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

//    public double getMiles(float i) {
//        return i * 0.000621371192;
//    }
//
//    public void calculateDistance() {
//        Location loc1 = new Location("");
//        loc1.setLatitude(fromLatitude);
//        loc1.setLongitude(fromLongitude);
//        Location loc2 = new Location("");
//        loc2.setLatitude(toLatitude);
//        loc2.setLongitude(toLongitude);
//        float distanceInMeters = loc1.distanceTo(loc2);
//        double distanceInMiles = getMiles(distanceInMeters);
//
//        //Displaying the distance
//        Toast.makeText(this, String.valueOf(distanceInMiles+" Miles"), Toast.LENGTH_SHORT).show();
//    }

    public void getLocationFromAddress(String strAddress){
        getLocation();
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address == null) {
                return;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextFragment() {
        if(location == null) {
            String address = et_address.getText().toString();
            getLocationFromAddress(address);
            String lat = String.valueOf(latitude);
            String lng = String.valueOf(longitude);
            Log.i("LocationFragment", "Latitude: " + lat + "Longitude: " + lng);
        }

        Log.i("LocationFragment", "Next fragment");
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
    }
}

