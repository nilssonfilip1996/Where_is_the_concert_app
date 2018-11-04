package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nilss.whenistheconcert.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageFragment extends Fragment {

    BandPlayingActivity bandPlayingActivity;

    LinearLayout linearLayout;
    TextView tvName;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image, container, false);

        initComponents();

        return view;
    }

    private void initComponents() {
        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayoutwithImg);
        URL url = null;
        try {
            url = new URL(bandPlayingActivity.detailedEvent.getImageUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapDrawable background = new BitmapDrawable(bmp);
        linearLayout.setBackground(background);

        tvName = (TextView) view.findViewById(R.id.tvName);
    }

}
