package com.example.aolsvt.myapplication;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static android.util.Log.VERBOSE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(true)
                .withContinueSessionMillis(10)
                .withLogLevel(VERBOSE)
                .build(this, "VQK3K8RYPKXP49YKJ8TD");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Map<String, String>  map = new Map<String, String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(Object o) {
                return false;
            }

            @Override
            public boolean containsValue(Object o) {
                return false;
            }

            @Override
            public String get(Object o) {
                return null;
            }

            @Override
            public String put(String s, String s2) {
                return null;
            }

            @Override
            public String remove(Object o) {
                return null;
            }

            @Override
            public void putAll(@NonNull Map<? extends String, ? extends String> map) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<String> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, String>> entrySet() {
                return null;
            }
        };
        FlurryAgent.logEvent("addMap", map, true);


        LatLng moscow = new LatLng(55.751244, 37.618423);
        mMap.addMarker(new MarkerOptions().position(moscow).title("Marker in Moscow"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moscow));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(37.59507,55.576559),
                        new LatLng(37.493452,55.613885),
                        new LatLng(37.369853,55.767479),
                        new LatLng(37.405561,55.871082),
                        new LatLng(37.553873,55.911209),
                        new LatLng(37.707682,55.898866),
                        new LatLng(37.83677,55.821634),
                        new LatLng(37.834021,55.689979),
                        new LatLng(37.83677,55.646518),
                        new LatLng(37.669234,55.576559)
                )
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

    }
}
