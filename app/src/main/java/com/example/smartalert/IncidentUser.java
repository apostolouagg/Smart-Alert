package com.example.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class IncidentUser extends AppCompatActivity {

    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_user);

        textView = (TextView) findViewById(R.id.textView_Report);

        //String text = getIntent().getExtras().toString("fire");

        //text.setText(text);
    }
}