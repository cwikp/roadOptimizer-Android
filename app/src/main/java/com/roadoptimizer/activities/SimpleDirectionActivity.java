package com.roadoptimizer.activities;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.LocationDTO;
import com.roadoptimizer.web.dto.RideDTO;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleDirectionActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback {
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyCgeXpzS42upBtxXawq7nH4rbf8DAK76io";
    private LatLng camera = new LatLng(37.782437, -122.4281893);
    private LatLng origin = new LatLng(37.7849569, -122.4068855);
    private LatLng destination = new LatLng(37.7814432, -122.4460177);
    private List<RideDTO> rides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_map_layout);
        
        loadValuesfromServer();


        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    private void loadValuesfromServer() {
        RideAPI service = ServiceGenerator.createService(RideAPI.class, Constants.TOKEN);
        Call<List<RideDTO>> call = service.getPassengerRides();
        call.enqueue(new Callback<List<RideDTO>>() {

            @Override
            public void onResponse(Call<List<RideDTO>> call, Response<List<RideDTO>> response) {
                if (response.isSuccessful()){
                    requestDirection();
                    rides = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<RideDTO>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 13));
    }


    public void requestDirection() {
        for (RideDTO ride : rides) {

            GoogleDirection.withServerKey(serverKey)
                    .from(new LatLng(ride.getStart().getLatitude(), ride.getStart().getLongitude()))
                    .to(new LatLng(ride.getEnd().getLatitude(), ride.getEnd().getLongitude()))
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        }

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            for (RideDTO ride : rides) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(ride.getStart().getLatitude(), ride.getStart().getLongitude())));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(ride.getEnd().getLatitude(), ride.getEnd().getLongitude())));

                for (LocationDTO checkpoint : ride.getCheckpoints()){
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(checkpoint.getLatitude(), checkpoint.getLongitude())));
                }
            }

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    public void onNavigatorButtonClicked(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
