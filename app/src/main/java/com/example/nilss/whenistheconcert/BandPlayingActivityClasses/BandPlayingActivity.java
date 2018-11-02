package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nilss.whenistheconcert.Pojos.DetailedEvent;
import com.example.nilss.whenistheconcert.Pojos.SimpleEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import com.example.nilss.whenistheconcert.R;

public class BandPlayingActivity extends AppCompatActivity {
    private static final String TAG = "BandPlayingActivity";
    public DetailedEvent detailedEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_playing);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        Log.d(TAG, "onCreate: ID: "+ id);


        TicketMasterHandler tmHandler = new TicketMasterHandler();
        tmHandler.getEventInfo(this,id);


    }

    public void getEventDetails(DetailedEvent detailedEvent){


        this.detailedEvent = detailedEvent;
        Log.d(TAG, "getEventDetails: Name: "+ this.detailedEvent.getName());

    }
}
