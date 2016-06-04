package com.roadoptimizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.roadoptimizer.R;
import com.roadoptimizer.utils.NavigationDrawer;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        NavigationDrawer navigationDrawer = new NavigationDrawer(this, toolbar);
        navigationDrawer.setupDrawer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, data.getStringExtra("createRideResult"), Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Ride might not have been created", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void onCreateTourClicked(View view) {
        Intent intent = new Intent(this, CreateRide.class);
        this.startActivityForResult(intent, 1);

    }

    public void onSearchToursClicked(View view) {
        Intent intent = new Intent(this, SearchRide.class);
        this.startActivity(intent);
    }
}
