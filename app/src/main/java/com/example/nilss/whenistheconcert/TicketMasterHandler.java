package com.example.nilss.whenistheconcert;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TicketMasterHandler {
    private static final String TAG = "TicketMasterHandler";
    private final String rootURL = "https://app.ticketmaster.com/discovery/v2/";
    private final String tmAPIKey = "XztmbuZ4nUUAx2Ki3rG7gDVZzHfGAAbw";


    /**
     * Filip and Jesper will be responsible for this
     * @param cityName
     * @return Enough info to place pins on map
     */
    public JSONObject getAllEvents(String cityName){
        //String URL = rootURL + "events.json?" + "classificationName=music"+ "&city=" + cityName + "&apikey="+ tmAPIKey;
        String URL = rootURL + "events.json?" + "city=" + cityName + "&apikey="+ tmAPIKey;
        TMRequester tmRequester = new TMRequester();
        tmRequester.execute(URL);
        return null;
    }

    /**
     * Arnes and Zorica will be responsible for this.
     * @param eventID
     * @return Up to you guys what info you will need in your activity.
     */
    public JSONObject getEventInfo(String eventID){
        //Do something
        return new JSONObject();
    }

    private class TMRequester extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                Log.d(TAG, "doInBackground: URL"+ url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
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
            Log.d(TAG, "onPostExecute: " + s);
        }
    }
}
