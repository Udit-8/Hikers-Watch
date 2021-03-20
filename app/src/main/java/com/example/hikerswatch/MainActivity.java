package com.example.hikerswatch;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView lattitudeText,longitudeText,accuracyText,altitudeText,addressText;
    public void getDetails(Location hikerLocation)
    {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List <Address> addressList = geocoder.getFromLocation(hikerLocation.getLatitude(),hikerLocation.getLongitude(),1);
            if(addressList != null && addressList.size() > 0)
            {
                lattitudeText.setText(String.valueOf(hikerLocation.getLatitude()));
                longitudeText.setText(String.valueOf(hikerLocation.getLongitude()));
                altitudeText.setText(String.valueOf(hikerLocation.getAltitude()));
                accuracyText.setText(String.valueOf(hikerLocation.getAccuracy()));
                String currAddress = "";
                if(addressList.get(0).getSubThoroughfare()!=null)
                    currAddress += addressList.get(0).getSubThoroughfare()+"\n";
                if(addressList.get(0).getThoroughfare()!=null)
                    currAddress += addressList.get(0).getThoroughfare()+"\n";
                if(addressList.get(0).getLocality()!=null)
                    currAddress += addressList.get(0).getLocality()+"\n";
                if(addressList.get(0).getSubLocality()!=null)
                    currAddress += addressList.get(0).getSubLocality();
                addressText.setText(currAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults != null && grantResults.length > 0)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lattitudeText = findViewById(R.id.lattitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        altitudeText = findViewById(R.id.altitudeText);
        addressText = findViewById(R.id.addressText);
        accuracyText = findViewById(R.id.accuracyText);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                getDetails(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            Location curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            getDetails(curLocation);
        }
    }
}
