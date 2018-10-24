package com.example.nilss.whenistheconcert;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

public class Controller {
    private MainActivity mainActivity;
    private MapActivity mapActivity;
    private LatLng userLatlng;
    private String dateIntervalSearch;



    public Controller(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void searchForEventsPressed(LatLng latLng, String dateInterval){
        this.userLatlng = latLng;
        this.dateIntervalSearch = dateInterval;
        Intent intent = new Intent(mainActivity, MapActivity.class);
        mainActivity.startActivity(intent);

    }


}
