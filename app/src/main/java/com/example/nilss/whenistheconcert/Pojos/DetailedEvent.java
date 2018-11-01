package com.example.nilss.whenistheconcert.Pojos;

import com.google.android.gms.maps.model.LatLng;

public class DetailedEvent {

    private String name;
    private String venue;
    private String date;
    private String ticketUrl;
    private String imageUrl;
    //private LatLng latLng;

    public DetailedEvent(String name, String venue, String date, String ticketUrl, String imageUrl/*, LatLng latLng*/){
        this.name=name;
        this.venue=venue;
        this.date=date;
        this.ticketUrl=ticketUrl;
        this.imageUrl=imageUrl;
       // this.latLng=latLng;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name) {
        this.name=name;
    }

    public String getVenue(){
        return this.venue;
    }
    public void setVenue(String venue) {
        this.venue=venue;
    }
    public String getDate(){
        return this.date;
    }
    public void setDate(String date) {
        this.date=date;
    }
    public String getTicketUrl(){ return this.ticketUrl; }
    public void setTicketUrl(String ticketUrl){
        this.ticketUrl=ticketUrl;
    }
    public String getImageUrl(){ return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl=imageUrl; }
//    public LatLng getLatLng() {
//        return latLng;
//    }
//
//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }

}
