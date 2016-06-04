package com.roadoptimizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.utils.GooglePlacesAutocomplete;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.LocationDTO;
import com.roadoptimizer.web.dto.RideOfferDTO;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRide extends AppCompatActivity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.createRide_location);

        if (autoCompView != null) {
            autoCompView.setAdapter(new GooglePlacesAutocomplete.GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
            autoCompView.setOnItemClickListener(this);
        }

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
        Call<Void> call = service.createRideOffer(newRide);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                response.code();
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);

                if (response.isSuccessful()) {
                    returnIntent.putExtra("createRideResult", "Ride " + newRide.getRideDate() + " successfully created");
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

    private RideOfferDTO getValues(View view) throws ParseException {

        EditText date = (EditText) view.findViewById(R.id.createRide_date);
        EditText seats = (EditText) view.findViewById(R.id.createRide_sits);
        EditText start = (EditText) view.findViewById(R.id.createRide_location);


        //location.getText().toString()
        LocationDTO locationDTO = LocationDTO.builder()
                .latitude(1.0)
                .longitude(1.0)
                .build();

        LocationDTO endlocationDTO = LocationDTO.builder()
                .latitude(80.0)
                .longitude(80.0)
                .build();

        RideOfferDTO newRide = RideOfferDTO.builder()
                .rideDate(date.getText().toString())
                .seats(Integer.parseInt(seats.getText().toString()))
                .start(locationDTO)
                .end(endlocationDTO)
                .build();

        return newRide;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}

