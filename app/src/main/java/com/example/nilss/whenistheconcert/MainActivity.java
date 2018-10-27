package com.example.nilss.whenistheconcert;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nilss.whenistheconcert.Wrapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Controller controller = new Controller(this);
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    //tester

    private CityNameRetriever cityNameRetriever;
    private EditText tvLocation, tvCity;
    private TextView tvStartDate, tvEndDate;
    LocationManager locationManager;
    LocationListener locationListener;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start mapactivity if google play services is ok!
        if (isServiceOk()) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
        initComp();
        initStartDateClickListener();
        initEndDateClickListener();

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);


        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
//                tvLocation.setText("Longitude: " + longitude + "\n" + "Latitude: " + latitude);
                Log.d("Location", location.toString());
                cityNameRetriever = new CityNameRetriever();
                cityNameRetriever.execute(location);
//                Log.d(TAG, "tvLOCATION: " + tvLocation.getText());

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


    /*
        OnCreate ENDS here!
        ------------------
     */


    private void initComp() {
        btn = findViewById(R.id.findButton);
        //  tvLocation = findViewById(R.id.tvLocation);
        tvCity = findViewById(R.id.tvCurrentCity);
        tvStartDate = findViewById(R.id.startDate);
        tvEndDate = findViewById(R.id.endDate);


    }


    private void initStartDateClickListener() {

        this.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth, startDateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog.show();
            }
        });

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String fmonth, fday;
                int intMonth;
                if (month < 10 && day < 10) {
                    fmonth = "0" + month;
                    fday = "0" + day;
                    intMonth = Integer.parseInt(fmonth) + 1;
                    String paddedMonth = String.format("%02d", intMonth);
                    String date = year + "-" + paddedMonth + "-" + fday;
                    tvStartDate.setText(date);
                } else if (day < 10) {

                    fday = "0" + day;
                    month = month + 1;
                    String date = year + "-" + month + "-" + fday;
                    tvStartDate.setText(date);

                } else if (month < 10) {

                    fmonth = "0" + month;
                    intMonth = Integer.parseInt(fmonth) + 1;
                    String paddedMonth = String.format("%02d", intMonth);
                    String date = year + "-" + paddedMonth + "-" + day;
                    tvStartDate.setText(date);
                } else {

                    month = month + 1;
                    String date = year + "-" + month + "-" + day;
                    tvStartDate.setText(date);


                }
            }
        };
    }

    private void initEndDateClickListener() {

        this.tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth, endDateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog.show();
            }
        });

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String fmonth, fday;
                int intMonth;
                if (month < 10 && day < 10) {
                    fmonth = "0" + month;
                    fday = "0" + day;
                    intMonth = Integer.parseInt(fmonth) + 1;
                    String paddedMonth = String.format("%02d", intMonth);
                    String date = year + "-" + paddedMonth + "-" + fday;
                    tvEndDate.setText(date);
                } else if (day < 10) {

                    fday = "0" + day;
                    month = month + 1;
                    String date = year + "-" + month + "-" + fday;
                    tvEndDate.setText(date);

                } else if (month < 10) {

                    fmonth = "0" + month;
                    intMonth = Integer.parseInt(fmonth) + 1;
                    String paddedMonth = String.format("%02d", intMonth);
                    String date = year + "-" + paddedMonth + "-" + day;
                    tvEndDate.setText(date);
                } else {

                    month = month + 1;
                    String date = year + "-" + month + "-" + day;
                    tvEndDate.setText(date);


                }
            }
        };
    }

    private void initBtn(LatLng latLog) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = tvStartDate.getText().toString();
                String endDate = tvEndDate.getText().toString();
                controller.searchForEventsPressed(latLog, startDate, endDate);

            }
        });

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

    private class CityNameRetriever extends AsyncTask<Location, Void, Wrapper> {
        @Override
        protected Wrapper doInBackground(Location... locations) {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            String cityName = "";
            Wrapper wrapper = null;
            try {
                addresses = geocoder.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), 1);
                cityName = addresses.get(0).getLocality();
                Log.d(TAG, "onLocationChanged: full address: " + addresses.get(0).getAddressLine(0));

                ArrayList<LatLng> latlng = new ArrayList<LatLng>(addresses.size());
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        latlng.add(new LatLng(a.getLatitude(), a.getLongitude()));

                    }
                }
                wrapper = new Wrapper();
                wrapper.cityName = cityName;
                wrapper.latlng = latlng;


            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ERROR couldn't retrieve city:");
                e.printStackTrace();
            }
            return wrapper;
        }

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            String cityName = wrapper.cityName;
            LatLng latLog = wrapper.latlng.get(0);

            Log.d(TAG, "WRAPPER: " + latLog);
            MainActivity.this.runOnUiThread(() -> tvCity.setText(cityName));
            Log.d(TAG, "CityName: " + cityName);
            initBtn(latLog);

        }


    }

}
