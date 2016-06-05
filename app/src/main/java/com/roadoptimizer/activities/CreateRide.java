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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.utils.GooglePlacesAutocomplete;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.LocationDTO;
import com.roadoptimizer.web.dto.RideOfferDTO;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.text.ParseException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRide extends AppCompatActivity implements View.OnClickListener {
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private EditText start;
    private EditText end;

    private LatLng coordsStart;
    private LatLng coordsEnd;
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        start = (EditText) findViewById(R.id.createRide_locationStart);
        end = (EditText) findViewById(R.id.createRide_locationEnd);

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
                    returnIntent.putExtra("createRideResult", "Ride " + newRide.getRideDate() + " successfully created : " + response.code());
                } else {
                    returnIntent.putExtra("createRideResult", "Ride not created : " + response.code());
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
        EditText seats = (EditText) view.findViewById(R.id.createRide_sits);


        //location.getText().toString()
        LocationDTO locationDTO = LocationDTO.builder()
                .latitude(coordsStart.latitude)
                .longitude(coordsStart.longitude)
                .build();

        LocationDTO endlocationDTO = LocationDTO.builder()
                .latitude(coordsEnd.latitude)
                .longitude(coordsEnd.longitude)
                .build();

        RideOfferDTO newRide = RideOfferDTO.builder()
                .rideDate(txtDate.getText().toString() + " " + txtTime.getText().toString())
                .seats(Integer.parseInt(seats.getText().toString()))
                .start(locationDTO)
                .end(endlocationDTO)
                .build();

        return newRide;
    }


    public void onStartLocationButtonClicked(View view) {
        code = 1;
        openAutocompleteActivity();
    }

    public void onEndLocationButtonClicked(View view) {
        code = 2;
        openAutocompleteActivity();
    }


    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("Selector", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Selector", "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.

                if (code == 1) {
                    start.setText(place.getName() + " " + place.getAddress());
                    coordsStart = place.getLatLng();
                }
                else if (code == 2) {
                    end.setText(place.getName() + " " + place.getAddress());
                    coordsEnd = place.getLatLng();
                }

                Toast.makeText(this, place.getLatLng().toString(), Toast.LENGTH_SHORT).show();

                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    mPlaceAttribution.setText("");
//                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("Selector", "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }
}

