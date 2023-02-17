package com.example.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginUser extends AppCompatActivity {

    public ImageButton imageButton;
    public TextView one, two, three, four, five, six;
    Intent intent = new Intent(this, IncidentUser.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        imageButton = (ImageButton) findViewById(R.id.imageButton_fire);
        imageButton = (ImageButton) findViewById(R.id.imageButton_earthquake);
        imageButton = (ImageButton) findViewById(R.id.imageButton_flood);
        imageButton = (ImageButton) findViewById(R.id.imageButton_hurricane);
        imageButton = (ImageButton) findViewById(R.id.imageButton_weather);
        imageButton = (ImageButton) findViewById(R.id.imageButton_something_else);

        one = (TextView) findViewById(R.id.textView_fire);
        two = (TextView) findViewById(R.id.textView_earthquake);
        three = (TextView) findViewById(R.id.textView_flood);
        four = (TextView) findViewById(R.id.textView_tornado);
        five = (TextView) findViewById(R.id.textView_weather);
        six = (TextView) findViewById(R.id.textView_something_else);

    }
    public class ChooseImageButton implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageButton_fire:
                    //intent.putExtra("fire",one.getText().toString());
                    startActivity(intent);
                    break;
                case R.id.imageButton_earthquake:
                    startActivity(intent);
                    break;
                case R.id.imageButton_flood:
                    startActivity(intent);
                    break;
                case R.id.imageButton_hurricane:
                    startActivity(intent);
                    break;
                case R.id.imageButton_weather:
                    startActivity(intent);
                    break;
                case R.id.imageButton_something_else:
                    startActivity(intent);
                    break;
            }

        }
    }
}