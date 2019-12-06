package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference Ref;
    private Toolbar mToolbar;
    private TextInputEditText number, otpCode;
    private Button LoginButton;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Initialize();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowLogin();
            }
        });




    }

    private void AllowLogin() {
        String otp = otpCode.getText().toString();
        final String myNumber = number.getText().toString();

        if(otp.isEmpty())
        {
            otpCode.setError("Enter Valid OTP");

        }
        else if(myNumber.isEmpty())
        {
            number.getText().toString();
        }
        else
        {
            Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        if(dataSnapshot1.child("phoneNumber").equals("+91" + myNumber))
                        {

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void Initialize() {

        Ref = FirebaseDatabase.getInstance().getReference();
        loadingBar = (ProgressBar)findViewById(R.id.load_login);
        loadingBar.setVisibility(View.INVISIBLE);
        number = (TextInputEditText)findViewById(R.id.number);
        otpCode = (TextInputEditText)findViewById(R.id.otp_code);
        LoginButton = (Button)findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.login_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    }
