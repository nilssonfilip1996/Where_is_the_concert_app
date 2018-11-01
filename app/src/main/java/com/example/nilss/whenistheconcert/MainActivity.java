package com.example.nilss.whenistheconcert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test
        TicketMasterHandler ticketMasterHandler = new TicketMasterHandler();
        ticketMasterHandler.requestAllEvents("Stockholm", "2018-10-28", "2018-12-31");
        ticketMasterHandler.getEventInfo("Z698xZq2Z17fI-E");

        Button button;



        button = (Button) findViewById(R.id.buttonToBandPlaying);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BandPlayingActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }
}
