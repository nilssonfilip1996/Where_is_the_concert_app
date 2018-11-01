package com.example.nilss.whenistheconcert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test
        TicketMasterHandler ticketMasterHandler = new TicketMasterHandler();
        ticketMasterHandler.requestAllEvents("Stockholm", "2018-10-28", "2018-12-31");
        ticketMasterHandler.getEventInfo("Z698xZq2Z17fI-E");
    }
}
