package com.roadoptimizer.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.LocationDTO;
import com.roadoptimizer.web.dto.PassengerDTO;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.text.ParseException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRide extends AppCompatActivity implements View.OnClickListener{
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

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
        txtTime = (EditText)findViewById(R.id.createRide_time);
        txtDate = (EditText)findViewById(R.id.createRide_date);

        txtTime.setOnClickListener(this);
        txtDate.setOnClickListener(this);
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
                .rideTime(txtDate.getText().toString() + " " + txtTime.getText().toString())
                .build();

        return newRide;
    }

    @Override
    public void onClick(View v) {
        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String y = Integer.toString(year);
                            String m = Integer.toString(monthOfYear + 1);
                            String d = Integer.toString(dayOfMonth);
                            if (monthOfYear < 10) {
                                m = "0" + m;
                            }
                            if (dayOfMonth < 10) {
                                d = "0" + d;
                            }
                            txtDate.setText(y + "/" + m + "/" + d);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            String hour = Integer.toString(hourOfDay);
                            String min = Integer.toString(minute);
                            if (hourOfDay < 10) {
                                hour = "0" + hour;
                            }
                            if (minute < 10) {
                                min = "0" + min;
                            }
                            txtTime.setText(hour + ":" + min + ":" + "00");
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
