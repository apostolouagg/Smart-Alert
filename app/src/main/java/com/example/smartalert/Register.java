package com.example.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText editText_username;
    private EditText editText_password;
    private EditText editText_email;
    private EditText editText_phone;
    private EditText editText_postAddress;
    private Button button_confirm;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_username = findViewById(R.id.text_Name);
        editText_password = findViewById(R.id.text_Password);
        editText_email = findViewById(R.id.text_Email);
        editText_phone = findViewById(R.id.text_Phone);
        editText_postAddress = findViewById(R.id.text_PostAddress);
        button_confirm = findViewById(R.id.button_register);

        editText_username.addTextChangedListener(logintextWatcher);
        editText_password.addTextChangedListener(logintextWatcher);
        editText_email.addTextChangedListener(logintextWatcher);
        editText_phone.addTextChangedListener(logintextWatcher);
        editText_postAddress.addTextChangedListener(logintextWatcher);

        editText_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
            }
        });
    }


    private TextWatcher logintextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String usernameInput = editText_username.getText().toString().trim();
            String emailInput = editText_email.getText().toString().trim();
            String passwordInput = editText_password.getText().toString().trim();
            String phoneInput = editText_phone.getText().toString().trim();
            String postAddressInput = editText_postAddress.getText().toString().trim();

            if (usernameInput.length() > 25){
                editText_username.setError("The username is too long!");
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                editText_email.setError("Invalid email!");
            }
            else if(phoneInput.length() != 10){
                editText_phone.setError("The phone number must be 10 digits!");
            }
            else if(postAddressInput.length() != 5){
                editText_postAddress.setError("The post address must be 5 digits!");
            }
            else if(passwordInput.length()<6){
                editText_password.setError("Your password must be greater than 5 digits!");
            }
            else {

            }
            button_confirm.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty()
                    && !emailInput.isEmpty() && !phoneInput.isEmpty() & !postAddressInput.isEmpty());


        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
}