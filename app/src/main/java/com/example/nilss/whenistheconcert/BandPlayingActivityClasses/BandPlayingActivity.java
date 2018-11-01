package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nilss.whenistheconcert.R;

public class BandPlayingActivity extends AppCompatActivity {
    private static final String TAG = "BandPlayingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_playing);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        Log.d(TAG, "onCreate: ID: "+ id);
    }
}
