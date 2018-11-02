package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nilss.whenistheconcert.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {
    TextView tvDate, tvVenue;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_details, container, false);

        initComponents();

        return view;
    }

    private void initComponents() {
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvVenue = (TextView) view.findViewById(R.id.tvVenue);
    }

}
