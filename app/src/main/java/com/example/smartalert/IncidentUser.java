package com.example.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class IncidentUser extends AppCompatActivity {

    //FusedLocationProviderClient fusedLocationProviderClient;
    public TextView textView, address, latitude, longitude, date, time;
    public static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_user);

        //Transfer DATA from LoginUser class
        textView = (TextView) findViewById(R.id.textView3);

        Intent incindentUser = getIntent();
        String text = incindentUser.getStringExtra("incident");
        textView.setText(text);

    }
}