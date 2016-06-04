package com.roadoptimizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.LocationDTO;
import com.roadoptimizer.web.dto.PassengerDTO;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onSearchButtonRideClicked(View view) throws ParseException {
        View correctView = findViewById(R.id.search_ride_id);
        final PassengerDTO newPassenger = getValues(correctView);


        RideAPI service = ServiceGenerator.createService(RideAPI.class, Constants.TOKEN);
        Call<Void> call = service.joinRideOffer(newPassenger);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                response.code();
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);

                if (response.isSuccessful()) {
                    returnIntent.putExtra("createRideResult", "Ride " + newPassenger.getRideTime() + " successfully created");
                } else {
                    returnIntent.putExtra("createRideResult", "Ride not created");
                }
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", t.getMessage(), t);
            }


        });

    }
    private PassengerDTO getValues(View view) throws ParseException {

        EditText date = (EditText) view.findViewById(R.id.searchRide_time);
        EditText start = (EditText) view.findViewById(R.id.searchRide_location);


        //location.getText().toString()
        LocationDTO locationDTO = LocationDTO.builder()
                .latitude(1.0)
                .longitude(1.0)
                .build();

        PassengerDTO newRide = PassengerDTO.builder()
                .address(locationDTO)
                .rideTime(date.getText().toString())
                .build();

        return newRide;
    }
}
