package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseReference Ref;
    private FirebaseUser currentUser;
    private String currentUserID, name, email, phoneNumber, privilege;
    private FirebaseAuth mAuth;
    private TextView nameText, emailText, phoneNumberText, privilegeText;
    private ProgressBar loadAccountDetails;
    private Button gotoApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        Initialize();

        Thread getDetails = new Thread(new Runnable() {
            @Override
            public void run() {
                GetUserDetails();

            }
        });

        getDetails.start();

        gotoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, MyApplications.class));
            }
        });

    }



    private void GetUserDetails() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name"))
                {
                    name = dataSnapshot.child("name").getValue().toString();
                    nameText.setText(name);
                }

                if(dataSnapshot.hasChild("email"))
                {
                    email = dataSnapshot.child("email").getValue().toString();
                    emailText.setText(email);
                }

                if(dataSnapshot.hasChild("phoneNumber"))
                {
                    phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                    phoneNumberText.setText(phoneNumber.substring(3));
                }
                if(dataSnapshot.hasChild("privilege"))
                {
                    privilege = dataSnapshot.child("privilege").getValue().toString();
                    privilegeText.setText(privilege);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loadAccountDetails.setVisibility(View.INVISIBLE);

    }

    private void Initialize() {
       // SetupTOolbar();
        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        nameText = (TextView)findViewById(R.id.name);
        emailText = (TextView)findViewById(R.id.email);
        phoneNumberText = (TextView)findViewById(R.id.number);
        privilegeText = (TextView)findViewById(R.id.privilege);

        gotoApp = (Button)findViewById(R.id.myacc_app);

        loadAccountDetails = (ProgressBar)findViewById(R.id.load_account_details);
    }

//    private void SetupTOolbar() {
//        mToolbar = (Toolbar) findViewById(R.id.my_account_bar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("My Account");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
