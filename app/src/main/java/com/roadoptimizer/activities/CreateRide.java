package com.roadoptimizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.LocationDTO;
import com.roadoptimizer.web.dto.RideOfferDTO;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onCreateButtonRideClicked(View view) throws ParseException {
        View correctView = findViewById(R.id.create_ride_id);
        final RideOfferDTO newRide = getValues(correctView);

        RideAPI service = ServiceGenerator.createService(RideAPI.class, Constants.TOKEN);
        Call<RideOfferDTO> call = service.createRide(newRide);
        call.enqueue(new Callback<RideOfferDTO>() {

            @Override
            public void onResponse(Call<RideOfferDTO> call, Response<RideOfferDTO> response) {
                response.code();
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);

                if (response.isSuccessful()) {
                    returnIntent.putExtra("createRideResult", "Ride " + newRide.getDate() + " successfully created");
                } else {
                    returnIntent.putExtra("createRideResult", "Ride not created");
                }
                finish();
            }

            @Override
            public void onFailure(Call<RideOfferDTO> call, Throwable t) {
                Log.e("MainActivity", t.getMessage(), t);
            }


        });
    }

    private RideOfferDTO getValues(View view) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        EditText date = (EditText) view.findViewById(R.id.createRide_date);
        EditText sits = (EditText) view.findViewById(R.id.createRide_sits);
        EditText location = (EditText) view.findViewById(R.id.createRide_location);

        Date dateObject = dateFormatter.parse(date.getText().toString());

        //location.getText().toString()
        LocationDTO locationDTO = LocationDTO.builder()
                .latitude(1.0)
                .longitude(1.0)
                .build();

        RideOfferDTO newRide = RideOfferDTO.builder()
                .date(dateObject.getTime())
                .sits(Integer.parseInt(sits.getText().toString()))
                .location(locationDTO)
                .build();

        return newRide;
    }

}

