package com.example.nilss.whenistheconcert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nilss.whenistheconcert.Pojos.SimpleEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback{
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private String cityName;
    private LatLng userLocation;
    private String startDate;
    private String endDate;
    private ArrayList<SimpleEvent> eventList;
    private ArrayList<SimpleEvent> foundEvents;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        double latitude = Double.valueOf(intent.getStringExtra("latitude"));
        double longitude = Double.valueOf(intent.getStringExtra("longitude"));
        this.userLocation = new LatLng(latitude,longitude);
        this.cityName = intent.getStringExtra("city");
        this.countryCode = intent.getStringExtra("countryCode");
        this.startDate = intent.getStringExtra("startDate");
        this.endDate = intent.getStringExtra("endDate");
        Log.d(TAG, "onCreate: city: " + cityName);
        Log.d(TAG, "onCreate: startDate: " + startDate);
        Log.d(TAG, "onCreate: endDate: " + endDate);
        initMap();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        TicketMasterHandler tmHandler = new TicketMasterHandler();
        tmHandler.requestAllEvents(MapActivity.this,cityName,countryCode,startDate,endDate);
        mMap.setOnInfoWindowClickListener(this);
    }


    public void updateEventList(ArrayList<SimpleEvent> foundEvents){
        if(foundEvents.size()==0){
            Toast.makeText(this, "No events Found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        this.foundEvents = foundEvents;
        int pinCounter = 0;
        ArrayList<LatLng> coordinateList = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < foundEvents.size(); i++) {
            addMarker(foundEvents.get(i).getLatLng(), foundEvents.get(i).getName());
            builder.include(foundEvents.get(i).getLatLng());
            if(!coordinateList.contains(foundEvents.get(i).getLatLng())){
                coordinateList.add(foundEvents.get(i).getLatLng());
                pinCounter++;
            }
        }
        if(pinCounter>1){
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (height * 0.15); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            mMap.animateCamera(cu);
        }
        else{
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(foundEvents.get(0).getLatLng(),12f));
        }
        Log.d(TAG, "updateEventList: pinCounter: "+ String.valueOf(pinCounter));
    }
    public void addMarker(LatLng latLng, String pinDesc){
        mMap.addMarker(new MarkerOptions().position(latLng).title(pinDesc));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String id="";
        for (int i = 0; i < foundEvents.size(); i++) {
            if(marker.getTitle().equals(foundEvents.get(i).getName())){
                id = foundEvents.get(i).getId();
                break;
            }
        }
        if(id.equals("")){
            Toast.makeText(this, "ERROR, no ID found",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, marker.getTitle() + "\n" + id,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
