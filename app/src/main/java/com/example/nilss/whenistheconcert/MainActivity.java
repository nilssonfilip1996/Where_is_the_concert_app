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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nilss.whenistheconcert.Wrapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Switch switchCity;
    LocationManager locationManager;
    LocationListener locationListener;
    private Button btn;
    private int start = 0;
    private boolean permissionGranted = false;
    private boolean check = false;
    private LatLng userCoordinates = null;
    private String cityName = "";
    private String countryCode= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start mapactivity if google play services is ok!
/*        if (isServiceOk()) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }*/
        initComp();
        initStartDateClickListener();
        initEndDateClickListener();

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        initLocationListener();
        initBtn();
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = switchCity.isChecked();
                Log.d(TAG, "CHECKED " + check);

                if (check) {
                    Toast.makeText(MainActivity.this, "Set new location", Toast.LENGTH_SHORT).show();
                    check = false;
                    Log.d(TAG, "SWICH ON!!");
                    tvCity.setText("");
                    locationManager.removeUpdates(locationListener);

                } else {

                    Toast.makeText(MainActivity.this, "Current Location", Toast.LENGTH_SHORT).show();
                    check = true;
                    Log.d(TAG, "LocationManager start");
                    checkLocationsPermissions();
                }
            }
        });
        checkLocationsPermissions();
    }

    private void checkLocationsPermissions() {
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

    private void initLocationListener() {
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

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
    }


    private void initComp() {
        btn = findViewById(R.id.findButton);
        //  tvLocation = findViewById(R.id.tvLocation);
        tvCity = findViewById(R.id.tvCurrentCity);
        tvStartDate = findViewById(R.id.startDate);
        tvEndDate = findViewById(R.id.endDate);
        switchCity = findViewById(R.id.switchCity);


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

    private void initBtn() {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check) {
                    try {
                        NewCityRetriever();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String startDate = tvStartDate.getText().toString();
                String endDate = tvEndDate.getText().toString();
                String city = cityName;

                if(cityName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "You must enter a city", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((startDate.isEmpty()) || (endDate.isEmpty())){
                    Toast.makeText(getApplicationContext(), "You must enter start and end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");
                Boolean dateChecker=false;
                try {
                    Date sdate = df.parse(startDate);
                    Date edate = df.parse(endDate);

                    Log.d(TAG, "DATE: " + sdate +"------" + edate);

                    if(sdate.before(edate)){
                        dateChecker=true;


                    }
                    else{
                        dateChecker=false;
                        Toast.makeText(getApplicationContext(), "Start date must come before end date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(dateChecker==true){
                   controller.searchForEventsPressed(userCoordinates, city, startDate, endDate);
                }
                else{
                    return;
                }



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
            String countryCode = "";
            Wrapper wrapper = null;
            try {
                addresses = geocoder.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), 1);
                cityName = addresses.get(0).getLocality();
                countryCode = addresses.get(0).getCountryCode();
                Log.d(TAG, "onLocationChanged: full address: " + addresses.get(0).getAddressLine(0));

                ArrayList<LatLng> latlng = new ArrayList<LatLng>(addresses.size());
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        latlng.add(new LatLng(a.getLatitude(), a.getLongitude()));

                    }
                }
                wrapper = new Wrapper();
                wrapper.cityName = cityName;
                wrapper.countryCode = countryCode;
                wrapper.latlng = latlng;


            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ERROR couldn't retrieve city:");
                e.printStackTrace();
            }
            return wrapper;
        }

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            String cityName2 = wrapper.cityName;
            LatLng latLog = wrapper.latlng.get(0);

            Log.d(TAG, "WRAPPER: " + latLog);
            MainActivity.this.runOnUiThread(() -> tvCity.setText(cityName2));
            Log.d(TAG, "CityName: " + cityName2);
            Log.d(TAG, "CountryCode: " + wrapper.countryCode);
            cityName = cityName2;
            userCoordinates = latLog;
            countryCode = wrapper.countryCode;

        }
    }

    public void NewCityRetriever() throws IOException {

        String location = tvCity.getText().toString();
        Geocoder geo = new Geocoder(this);
        List<Address> list = geo.getFromLocationName(location, 1);
        Address add = list.get(0);
        String locality = add.getLocality();
        //  Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        ArrayList<LatLng> latlng = new ArrayList<LatLng>(list.size());
        for (Address a : list) {
            if (a.hasLatitude() && a.hasLongitude()) {
                latlng.add(new LatLng(a.getLatitude(), a.getLongitude()));

            }

        }
        LatLng latLog = latlng.get(0);
        Log.d(TAG, "COORDINATES GEO: " + latLog);
        Log.d(TAG, "CITY RETRIEVED BY TEXTVIEW : " + locality);
        cityName = location;
        userCoordinates = latLog;
    }


}
