package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nilss.whenistheconcert.Pojos.DetailedEvent;
import com.example.nilss.whenistheconcert.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageFragment extends Fragment {
    private static final String TAG = "ImageFragment";

    public String artistName, imageURL, ticketURL, eventDate, eventVenue;

    BandPlayingActivity bandPlayingActivity;

    ImageView imageView;
    TextView tvName;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image, container, false);

        initComponents();

        return view;
    }

//    public void onActivityCreated (Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        new LoadBackground().execute();
//    }


    private void initComponents() {
        imageView = (ImageView) view.findViewById(R.id.imageView);
        setImageBackground();

        tvName = (TextView) view.findViewById(R.id.tvName);
    }

    public void setImageBackground (){
        new LoadBackground(this.imageURL,this.artistName).execute();
    }

    public void getData(String artistName, String imageURL) {
        this.artistName = artistName;
        this.imageURL = imageURL;
    }


    public class LoadBackground extends AsyncTask <String, Void, Drawable> {
        public BandPlayingActivity bandPlayingActivity = new BandPlayingActivity();

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
                    Log.d("myApp", "Waiting for Task1");
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
//
//            this.imageURL = bandPlayingActivity.detailedEvent.getImageUrl();
//            this.imageName = bandPlayingActivity.detailedEvent.getName();

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
            imageView.setImageDrawable(result);
            tvName.setText(imageName);
            Log.d(TAG, "Load Image: " + imageURL + " " + result);
        }
    }



}


