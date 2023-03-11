package com.example.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    private Button button;
    private EditText editText_email, editText_password;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        loadLocale();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        //Sign in Account
        editText_email = (EditText) findViewById(R.id.text_email);
        editText_password = (EditText) findViewById(R.id.text_password);

        // Check if user is already logged in
        FirebaseUser currentUser =  firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, LoginUser.class);
            startActivity(intent);
            finish();
        }
        button = (Button) findViewById(R.id.button_sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editText_email.getText().toString().trim();
                String pass = editText_password.getText().toString().trim();
                if(email.isEmpty() || pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "To log in please fill the fields",Toast.LENGTH_LONG).show();
                }else{
                    signIn();
                }
            }
        });

        //Open Registration
        button = (Button) findViewById(R.id.button_sign_up);
        Intent intent2 = new Intent(this, Register.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });
    }

    private void showChangeLanguageDialog() {
        //Array of languages to display in alert dialog
        final String[] listItems = {"English","Ελληνικά"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    //Choose English
                    setlocale("en");
                    recreate();
                }
                else if(i==1){
                    //Choose Greek
                    setlocale("gr");
                    recreate();
                }
                //dismiss alert dialog when language is selected
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
    }

    private void setlocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //Load language saved in share preferences
    public void loadLocale (){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setlocale(language);
    }

    //Sign in
    private void signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, check if email ends with "@gov.gr"
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String email = user.getEmail();
                                if (email.endsWith("@gov.gr")) {
                                    // Email ends with "@gov.gr", open employer activity
                                    Intent intent = new Intent(MainActivity.this, LoginEmployer.class);
                                    startActivity(intent);
                                } else {
                                    // Email does not end with "@gov.gr", open user activity
                                    Intent intent = new Intent(MainActivity.this, LoginUser.class);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Email or password is wrong.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        // Exit the app
        finishAffinity();
    }
}