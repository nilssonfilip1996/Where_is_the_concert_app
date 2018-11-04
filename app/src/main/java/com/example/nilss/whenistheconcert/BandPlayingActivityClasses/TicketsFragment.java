package com.example.nilss.whenistheconcert.BandPlayingActivityClasses;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nilss.whenistheconcert.R;


public class TicketsFragment extends Fragment {

    Button btnToTicketMaster;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tickets, container, false);

        initComponents();
        registerListeners();
        return view;
    }

    private void registerListeners() {
        btnToTicketMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open web browser to ticket master web with url
            }
        });
    }

    private void initComponents() {
        btnToTicketMaster = (Button) view.findViewById(R.id.btnToTickets);
    }


}
