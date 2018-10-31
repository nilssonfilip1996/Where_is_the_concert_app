package com.example.nilss.whenistheconcert;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nilss.whenistheconcert.Pojos.SimpleEvent;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TicketMasterHandler {
    private static final String TAG = "TicketMasterHandler";
    private final String rootURL = "https://app.ticketmaster.com/discovery/v2/";
    private final String tmAPIKey = "XztmbuZ4nUUAx2Ki3rG7gDVZzHfGAAbw";

    private static final String REQUEST_TYPE_GET_EVENTS = "0";
    private static final String REQUEST_TYPE_GET_EVENT_INFO = "1";

    private static final String NAME_KEY = "name";
    private static final String ID_KEY = "id";
    private static final String LOCATION_KEY = "location";
    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";


    /**
     * Filip and Jesper will be responsible for this
     * @param cityName, where to search
     * @param dateFrom, from?
     * @param dateTo, to?
     */
    public void requestAllEvents(MapActivity mapActivity, String cityName, String dateFrom, String dateTo){
        //String URL = rootURL + "events.json?" + "classificationName=music"+ "&city=" + cityName + "&apikey="+ tmAPIKey;
        String dateCriteria = "startDateTime="+dateFrom+"T00:00:00Z&endDateTime=" + dateTo + "T00:00:00Z";
        String URL = rootURL + "events.json?" + dateCriteria + "&city=" + cityName + "&size=199" + "&apikey="+ tmAPIKey;
        TMRequester tmRequester = new TMRequester(new AsyncResponse() {
            @Override
            public void processFinish(JSONArray result) {
                ArrayList<SimpleEvent> foundEvents = new ArrayList<>();
                for (int i = 0; i < result.length(); i++) {
                    JSONObject temp = null;
                    try {
                        temp = result.getJSONObject(i);
                        String name = temp.getString(NAME_KEY);
                        String id = temp.getString(ID_KEY);
                        JSONObject JSONlocation = temp.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject(LOCATION_KEY);
                        LatLng latLng = new LatLng(JSONlocation.getDouble(LATITUDE_KEY), JSONlocation.getDouble(LONGITUDE_KEY));
                        foundEvents.add(new SimpleEvent(name, id, latLng));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mapActivity.updateEventList(foundEvents);
                //Run on ui thread here.
/*                for (int i = 0; i < foundEvents.size(); i++) {
                    mapActivity.addMarker(foundEvents.get(i).getLatLng(), foundEvents.get(i).getName());
                    Log.d(TAG, "processFinish: name: "+ foundEvents.get(i).getName());
                    Log.d(TAG, "processFinish: id: " + foundEvents.get(i).getId());
                    Log.d(TAG, "processFinish: latlng: " + foundEvents.get(i).getLatLng().latitude + ", " + foundEvents.get(i).getLatLng().longitude);
                    Log.d(TAG, "processFinish: ..........................................");
                }*/
                Log.d(TAG, "processFinish: nbrOfEvents: " + String.valueOf(foundEvents.size()));
            }
        });
        tmRequester.execute(URL, REQUEST_TYPE_GET_EVENTS);
    }

    /**
     * Arnes and Zorica will be responsible for this.
     * @param eventID
     * @return Up to you guys what info you will need in your activity.
     */
    public void getEventInfo(String eventID){
        //Do something
    }

    /**
     * Inner class that extends AsyncTask.
     * Used to download JSON content from a given URL.
     * When calling .execute you'll have to pass a URL as a string aswell as what type of request it is.
     */
    public class TMRequester extends AsyncTask<String,Void,String>{
        private AsyncResponse delegate = null;
        private String requestType;


        public TMRequester(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                requestType = params[1];
                Log.d(TAG, "doInBackground: URL"+ url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                //Request all events(Filip And Jesper).
                if(requestType.equals(REQUEST_TYPE_GET_EVENTS)) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONObject("_embedded").getJSONArray("events");
                    if (jsonArray != null) {
                        delegate.processFinish(jsonArray);
                    }
                    /*for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject test = new JSONObject(jsonArray.get(i).toString());
                        Log.d(TAG, "onPostExecute: event" + i +": " + test.getString("name") + "--" + test.getJSONObject("dates").getJSONObject("start").getString("localDate"));
                    }*/
                }
                //Otherwise it is a eventInfo request!(Zorica and Arnes).
                else{

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
