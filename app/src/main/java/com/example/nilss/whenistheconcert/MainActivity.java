package com.example.nilss.whenistheconcert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    //tester

    private CityNameRetriever cityNameRetriever;
    private TextView tvLocation, tvCity;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start mapactivity if google play services is ok!
        if(isServiceOk()) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
        tvLocation = findViewById(R.id.tvLocation);
        tvCity = findViewById(R.id.tvCurrentCity);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                tvLocation.setText("Longitude: " + longitude + "\n" + "Latitude: " + latitude);
                Log.d("Location", location.toString());
                cityNameRetriever = new CityNameRetriever();
                cityNameRetriever.execute(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {  // If permission is NOT granted.
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            // Needed for update when application is started again.
        }
    }


    /**
     * Not sure what the deal with google play services is.
     * All I know is that it is mandatory in order to use google maps.
     * This is just a simple check to see if the device can run google maps
     *
     * @return
     */
    private boolean isServiceOk() {
        Log.d(TAG, "isServiceOk: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOk: Google play services are working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceOk: An error occured but we can fix it");
            Dialog diablog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            diablog.show();
        } else {
            Toast.makeText(this, "We can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

            }
        }
    }

    private class CityNameRetriever extends AsyncTask<Location, Void, String> {
        @Override
        protected String doInBackground(Location... locations) {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            String cityName = "";
            try {
                addresses = geocoder.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), 1);
                cityName = addresses.get(0).getLocality();
                Log.d(TAG, "onLocationChanged: full address: " + addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ERROR couldn't retrieve city:");
                e.printStackTrace();
            }
            return cityName;
        }

        @Override
        protected void onPostExecute(String cityName) {
            MainActivity.this.runOnUiThread(() -> tvCity.setText(cityName));
        }
    }

}
