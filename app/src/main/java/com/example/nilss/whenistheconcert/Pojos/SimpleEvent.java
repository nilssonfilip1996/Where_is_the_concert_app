package com.example.nilss.whenistheconcert.Pojos;

import com.google.android.gms.maps.model.LatLng;

public class SimpleEvent {
    private String name;
    private String id;
    private LatLng latLng;

    public SimpleEvent(String name, String id, LatLng latLng) {
        this.name = name;
        this.id = id;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
