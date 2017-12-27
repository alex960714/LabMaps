package com.example.user.labmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button newMarkerBtn;
    private Button delMarkerBtn;
    private ArrayList<LatLng> places;
    private Iterator<LatLng> iter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        places = new ArrayList<>();
        newMarkerBtn = (Button)findViewById(R.id.newMarkerButton);
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                        .title(Double.valueOf(latLng.latitude).toString()
                                + " " + Double.valueOf(latLng.longitude).toString())
                        .draggable(true);
                mMap.addMarker(markerOptions);
                places.add(markerOptions.getPosition());
                iter = places.listIterator();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                deleteMarker(marker);
                delButtonInactive();
                return false;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                places.remove(marker.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setTitle(Double.valueOf(marker.getPosition().latitude).toString()
                        + " "+ Double.valueOf(marker.getPosition().longitude).toString() );
                places.add(marker.getPosition());
                iter=places.listIterator();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        newMarkerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(places.size()!=0) {
                    if (!iter.hasNext()) {
                        iter = places.listIterator();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(iter.next()));
                }
            }
        });


    }

    public void deleteMarker(final Marker marker){
        delMarkerBtn=(Button)findViewById(R.id.delMarkerButton);
        delMarkerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ind = places.indexOf(marker.getPosition());
                places.remove(ind);
                iter = places.listIterator();
                marker.remove();
            }
        });
    }

    public void delButtonInactive(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                delMarkerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
    }

}
