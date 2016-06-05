package com.roadoptimizer.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roadoptimizer.R;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.utils.NavigationDrawer;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.RideDTO;
import com.roadoptimizer.web.services.RideService;
import com.roadoptimizer.web.services.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class YourRides extends AppCompatActivity {
    private List<RideDTO> rides = new ArrayList<>();
    private RideService eventService = new RideService(this);
    private RideDTO rideDTO;
    Call<List<RideDTO>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        setTitle("Events");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(EventsList.this, CreateEvent.class);
//                EventsList.this.startActivityForResult(intent, 1);
//            }
//
//        });

        RideAPI service = ServiceGenerator.createService(RideAPI.class, Constants.TOKEN);
        call = service.getPassengerRides();


        eventService.fillEventsData(call);
        Toast.makeText(YourRides.this, "Started", Toast.LENGTH_LONG).show();
        onEventsListItemClick();

        NavigationDrawer navigationDrawer = new NavigationDrawer(this, toolbar);
        navigationDrawer.setupDrawer();

    }

    public void setEventsData(List<RideDTO> eventsData) {
        this.rides = eventsData;
    }



    public void fillEventsListView() {
        ArrayAdapter<RideDTO> adapter = new EventsListAdapter();
        ListView list = (ListView) findViewById(R.id.eventsListView);
        if (list != null) {
            list.setAdapter(adapter);
        }
    }

    public void onEventsListItemClick() {
        ListView list = (ListView) findViewById(R.id.eventsListView);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RideDTO clickedEvent = rides.get(position);
//                Toast.makeText(YourRides.this, clickedEvent.getOwnerName(), Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(EventsList.this, SingleEvent.class);
//                intent.putExtra("event", clickedEvent);
//                EventsList.this.startActivity(intent);

            }
        });
    }

    private class EventsListAdapter extends ArrayAdapter<RideDTO>{

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE d MMM ''yy");
        Random rnd = new Random();

        public EventsListAdapter() {
            super(YourRides.this, R.layout.item_events_list, rides);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_events_list, parent, false);
            }

            RideDTO currentRide =  rides.get(position);


            TextView time = (TextView) itemView.findViewById(R.id.itemListTime);



            if(currentRide != null) {
                time.setText(currentRide.getRideTime());
            }
            else {
                time.setText("Very soon");

            }

            return itemView;
        }
    }
}
