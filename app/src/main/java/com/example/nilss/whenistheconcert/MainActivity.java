package com.example.nilss.whenistheconcert;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start mapactivity if google play services is ok!
        if(isServiceOk()) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Not sure what the deal with google play services is.
     * All I know is that it is mandatory in order to use google maps.
     * This is just a simple check to see if the device can run google maps
     * @return
     */
    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOk: Google play services are working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOk: An error occured but we can fix it");
            Dialog diablog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            diablog.show();
        }
        else{
            Toast.makeText(this,"We can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
