package com.example.alexLeb.Lab4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GPSTracker gps = new GPSTracker(this);

        TextView llat = (TextView) findViewById(R.id.lat);
        TextView llon = (TextView) findViewById(R.id.lon);
        TextView lenable = (TextView) findViewById(R.id.enable);

        Double lat = gps.getLatitude(); // returns latitude
        Double lon = gps.getLongitude(); // returns longitude
        llat.setText("latitude" + lat.toString());
        llon.setText("longitude" + lon.toString());
        if(gps.canGetLocation()){
            lenable.setText("gps is enabled");
        } else {
            lenable.setText("gps is not enabled");
        }
    }
}
