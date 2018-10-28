package com.example.nilss.whenistheconcert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nilss.whenistheconcert.Pojos.SimpleEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private ArrayList<SimpleEvent> test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            test = (ArrayList<SimpleEvent>) bundle.getSerializable("key");
        }
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
        for (int i = 0; i < test.size(); i++) {
            addMarker(test.get(i).getLatLng(),test.get(i).getName());
        }
        /*LatLng random = new LatLng(55.60587, 13.00073);
        addMarker(random,  "Malmo");
        random = new LatLng(57.708870, 11.974560);
        addMarker(random, "Gothenburg");
        random = new LatLng(59.334591, 18.063240);
        addMarker(random, "Stockholm");*/
    }

    /**
     * Puts a marker on the map!
     * @param latLng, Latlng object containing latitude and longitude.
     * @param pinDesc, desciptive text when user clicks on pin.
     */
    public void addMarker(LatLng latLng, String pinDesc){
        mMap.addMarker(new MarkerOptions().position(latLng).title(pinDesc));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

}
