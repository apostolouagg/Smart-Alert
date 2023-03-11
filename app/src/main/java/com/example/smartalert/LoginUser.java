package com.example.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUser extends AppCompatActivity {

    public Button button_logOut;
    public ImageButton b1, b2, b3, b4, b5, b6;
    public TextView one, two, three, four, five, six;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        mAuth = FirebaseAuth.getInstance();

        button_logOut = (Button) findViewById(R.id.button_LogOut);
        button_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogOut();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                TextView welcomeMessage = findViewById(R.id.textView_Welcomeback);
                welcomeMessage.setText("Welcome " + firstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        b1 = (ImageButton) findViewById(R.id.imageButton_fire);
        b2 = (ImageButton) findViewById(R.id.imageButton_flood);
        b3 = (ImageButton) findViewById(R.id.imageButton_earthquake);
        b4 = (ImageButton) findViewById(R.id.imageButton_hurricane);
        b5 = (ImageButton) findViewById(R.id.imageButton_weather);
        b6 = (ImageButton) findViewById(R.id.imageButton_something_else);

        one = (TextView) findViewById(R.id.textView_fire);
        two = (TextView) findViewById(R.id.textView_flood);
        three = (TextView) findViewById(R.id.textView_earthquake);
        four = (TextView) findViewById(R.id.textView_tornado);
        five = (TextView) findViewById(R.id.textView_weather);
        six = (TextView) findViewById(R.id.textView_something_else);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setSelected(!b1.isSelected());
                String incident = one.getText().toString().trim();
                if (b1.isSelected()) {
                    Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                    intent.putExtra("incident", incident);
                    startActivity(intent);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b2.setSelected(!b2.isSelected());
                String incident = two.getText().toString().trim();
                if(b2.isSelected()){
                    Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                    intent.putExtra("incident", incident);
                    startActivity(intent);
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b3.setSelected(!b3.isSelected());
                String incident = three.getText().toString().trim();
                if(b3.isSelected()){
                    Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                    intent.putExtra("incident", incident);
                    startActivity(intent);
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b4.setSelected(!b4.isSelected());
                String incident = four.getText().toString().trim();
                if(b4.isSelected()){
                    Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                    intent.putExtra("incident", incident);
                    startActivity(intent);
                }
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b5.setSelected(!b5.isSelected());
                String incident = five.getText().toString().trim();
                if(b5.isSelected()){
                    Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                    intent.putExtra("incident", incident);
                    startActivity(intent);
                }
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b6.setSelected(!b6.isSelected());
                String incident = six.getText().toString().trim();
                if(b6.isSelected()){
                    Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                    intent.putExtra("incident", incident);
                    startActivity(intent);
                }
            }
        });
    }
    private void confirmLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        Intent intent = new Intent(LoginUser.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        confirmLogOut();
    }



}