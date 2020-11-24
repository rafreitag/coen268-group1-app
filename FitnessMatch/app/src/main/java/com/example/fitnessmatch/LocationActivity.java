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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private EditText et_address;

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private Location location; // location
    private double latitude, longitude;

    private Geocoder geocoder;
    private List<Address> addresses;

    private Preferences preferences;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        et_address = findViewById(R.id.et_address);

        preferences = new Preferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
        }
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

    public void findAddressFromLocation(View view) {
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

    public void updateLocation() {
        preferences.setLatitude(latitude);
        preferences.setLongitude(longitude);
    }

    public void nextPage(View view) {
        if(location == null) {
            String address = et_address.getText().toString();
            getLocationFromAddress(address);
            String lat = String.valueOf(latitude);
            String lng = String.valueOf(longitude);
            Log.i("LocationFragment", "Latitude: " + lat + "Longitude: " + lng);
        }

        Log.i("LocationFragment", "Next fragment");
        Intent a = new Intent(this, FindActivity.class);
        // Update preferences object with location
        updateLocation();
        // Sending this preferences object to the find page so that none of the choices are lost
        a.putExtra("preferences", (Serializable) preferences);
        startActivity(a);
    }
}

