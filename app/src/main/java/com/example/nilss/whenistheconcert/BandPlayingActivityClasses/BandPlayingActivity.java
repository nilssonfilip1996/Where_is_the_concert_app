package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nilss.whenistheconcert.Pojos.DetailedEvent;
import com.example.nilss.whenistheconcert.TicketMasterHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.nilss.whenistheconcert.R;

public class BandPlayingActivity extends AppCompatActivity {

    private static final String TAG = "BandPlayingActivity";

    public DetailedEvent detailedEvent;

    public String artistName, imageURL, ticketURL, eventDate, eventVenue;

    private ImageView imageView;
    private TextView tvName, tvDate, tvVenue;
    private ImageButton btnToTicketMaster;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_playing);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        Log.d(TAG, "onCreate: ID: "+ id);


        TicketMasterHandler tmHandler = new TicketMasterHandler();
        tmHandler.getEventInfo(this, id);

        initComponents();
    }

    private void initComponents() {

        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvName = (TextView) findViewById(R.id.tvName);

        btnToTicketMaster = (ImageButton) findViewById(R.id.btnToTickets);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvVenue = (TextView) findViewById(R.id.tvVenue);
    }

    public void setImageBackground (){
        new LoadBackground(this.imageURL,this.artistName).execute();
    }

    public void setTextViews(String name, String date, String venue) {
        tvName.setText(name);
        tvDate.setText(date);
        tvVenue.setText(venue);
    }

    private void onButtonCLick(String url) {
        btnToTicketMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public void getEventDetails(DetailedEvent detailedEvent){
        this.detailedEvent = detailedEvent;
        Log.d(TAG, "getEventDetails: Name: "+ this.detailedEvent.getName());
        Log.d(TAG, "getEventDetails: Image: "+ this.detailedEvent.getImageUrl());

        this.artistName = this.detailedEvent.getName();
        this.imageURL = this.detailedEvent.getImageUrl();
        this.ticketURL = this.detailedEvent.getTicketUrl();
        this.eventDate = this.detailedEvent.getDate();
        this.eventVenue = this.detailedEvent.getVenue();

        setImageBackground();
        setTextViews(this.artistName, this.eventDate, this.eventVenue);
        onButtonCLick(this.ticketURL);
    }

    public class LoadBackground extends AsyncTask<String, Void, Drawable> {
        private String imageURL;
        private String imageName;

        public LoadBackground (String imageURL, String imageName) {
            this.imageName = imageName;
            this.imageURL = imageURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(String... urls) {
//            while( bandPlayingActivity.imageURL == null) {
//                try {
//                    Log.d("myApp", "Waiting for Task1");
//                    Thread.sleep(1000);
//                }
//                catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            try {
                InputStream is = (InputStream) this.fetch(this.imageURL);
                Drawable d = Drawable.createFromStream(is, this.imageName);
                return d;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private Object fetch(String address) throws MalformedURLException,IOException {
            URL url = new URL(address);
            Object content = url.getContent();
            return content;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            imageView.setImageDrawable(result);
            Log.d(TAG, "Load Image: " + imageURL + " " + result);
        }
    }

}