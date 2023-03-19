package com.example.smartalert;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MenuScreen extends AppCompatActivity {
    
    private Button button;
    private EditText editText_email, editText_password;
    boolean passwordVisible;
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
        editText_email = findViewById(R.id.text_email);
        editText_password = findViewById(R.id.text_password);

        //Visible/invisible password
        editText_password.setOnTouchListener((view, motionEvent) -> {
            final int Right = 2;
            if(motionEvent.getAction()== MotionEvent.ACTION_UP){
                if(motionEvent.getRawX()>=editText_password.getRight()-editText_password.getCompoundDrawables()[Right].getBounds().width()){
                    int selection = editText_password.getSelectionEnd();
                    //Hide password
                    if(passwordVisible){
                        editText_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_visibility_off_24,0);
                        editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                        //Show password
                    }else{
                        editText_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_baseline_visibility_24,0);
                        editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    editText_password.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        // Check if user is already logged in
        FirebaseUser currentUser =  firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MenuScreen.this, LoginUser.class);
            startActivity(intent);
            finish();
        }
        button = findViewById(R.id.button_sign_in);
        button.setOnClickListener(view -> {
            String email = editText_email.getText().toString().trim();
            String pass = editText_password.getText().toString().trim();
            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(MenuScreen.this, "To log in please fill the fields",Toast.LENGTH_LONG).show();
            }else{
                signIn();
            }

        });

        //Open Registration
        button = findViewById(R.id.button_sign_up);
        Intent intent2 = new Intent(this, Register.class);

        button.setOnClickListener(view -> startActivity(intent2));

    }

    private void showChangeLanguageDialog() {
        //Array of languages to display in alert dialog
        final String[] listItems = {"English","Ελληνικά"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MenuScreen.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItems, -1, (dialogInterface, i) -> {
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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, check if email ends with "@gov.gr"
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            if (email.endsWith("@gov.gr")) {
                                // Email ends with "@gov.gr", open employer activity
                                Intent intent = new Intent(MenuScreen.this, LoginEmployee.class);
                                startActivity(intent);
                            } else {
                                // Email does not end with "@gov.gr", open user activity
                                Intent intent = new Intent(MenuScreen.this, LoginUser.class);
                                startActivity(intent);
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MenuScreen.this, "Email or password is wrong.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        // Exit the app
        finishAffinity();
    }
}