package com.example.smartalert;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Register extends AppCompatActivity {

    private EditText editText_username, editText_password, editText_email, editText_phone, editText_postAddress;
    private Button button_confirm;
    boolean passwordVisible;

    FirebaseAuth mAuth;
    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    UserInfo userInfo;

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


        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Users");

        // initializing our object
        // class variable.
        userInfo = new UserInfo();

        mAuth = FirebaseAuth.getInstance();

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
    void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
    //Check the all the fields
    private TextWatcher logintextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
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
                button_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signUp(usernameInput,emailInput,phoneInput,postAddressInput,passwordInput);
                    }
                });
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    };

    //sign Up
    private void signUp(String name, String email, String phone, String post, String password) {
        userInfo.setUsername(name);
        userInfo.setEmail(email);
        userInfo.setPhone(phone);
        userInfo.setPostAddress(post);
        userInfo.setPassword(password);

        // Get a reference to the "Users" node in the database
        DatabaseReference usersRef = firebaseDatabase.getReference("Users");

        // Check if the email is already in use
        Query emailQuery = usersRef.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    editText_email.setError("This email is already in use!");
                } else {
                    // Check if the phone number is already in use
                    Query phoneQuery = usersRef.orderByChild("phone").equalTo(phone);
                    phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                editText_phone.setError("This phone number is already in use!");
                            } else {
                                // Save the user data under a new key
                                String userId = usersRef.push().getKey();
                                usersRef.child(userId).setValue(userInfo);
                                //We need this when the user wants to login after the registration
                                mAuth.createUserWithEmailAndPassword(email.toString(),password.toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Register.this, "User authenticated", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                Toast.makeText(Register.this, "Registration completed successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}