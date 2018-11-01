package com.example.nilss.whenistheconcert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nilss.whenistheconcert.Pojos.SimpleEvent;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Controller {
    private MainActivity mainActivity;
    private MapActivity mapActivity;
    private LatLng userLatlng;
    private String dateIntervalSearch;
    private static final String TAG = "Controller";



    public Controller(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void searchForEventsPressed(LatLng latLng, String cityName, String countryCode, String startDate, String endDate){
        this.userLatlng = latLng;
       // this.dateIntervalSearch = dateInterval;
        Log.d(TAG, "Wrapper" + latLng + "------" + "StartDate: " + startDate + "-----" + "EndDate: " + endDate);
        //tmHandler.requestAllEvents(cityName,startDate,endDate);
        Intent intent = new Intent(mainActivity, MapActivity.class);
        intent.putExtra("latitude", String.valueOf(latLng.latitude));
        intent.putExtra("longitude", String.valueOf(latLng.longitude));
        intent.putExtra("city", cityName);
        intent.putExtra("countryCode", countryCode);
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        mainActivity.startActivity(intent);



    }


    public void showEventsOnMap(ArrayList<SimpleEvent> foundEvents) {
        if(foundEvents!=null) {
            for (int i = 0; i < foundEvents.size(); i++) {
                Log.d(TAG, "processFinish: name: " + foundEvents.get(i).getName());
                Log.d(TAG, "processFinish: id: " + foundEvents.get(i).getId());
                Log.d(TAG, "processFinish: latlng: " + foundEvents.get(i).getLatLng().latitude + ", " + foundEvents.get(i).getLatLng().longitude);
                Log.d(TAG, "processFinish: ..........................................");
            }
            Log.d(TAG, "processFinish: nbrOfEvents: " + String.valueOf(foundEvents.size()));
        }
    }
}
