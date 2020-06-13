package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersAccountActivity extends AppCompatActivity {


    private DatabaseReference Ref;
    private Toolbar mToolbar;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;
    private String username, userNumber, privilege, email, currentUserID ;
    private TextView nameText, emailText, phoneNumberText, privilegeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_account);


        Initialize();


        GetUserID();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetUserDetails();

            }
        },3000);


    }

    private void GetUserID() {


        Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("phoneNumber").getValue().equals(userNumber))
                    {
                        currentUserID = dataSnapshot1.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetUserDetails() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                nameText.setText(username);
                phoneNumberText.setText(userNumber);
                if(dataSnapshot.hasChild("email"))
                {
                    email = dataSnapshot.child("email").getValue().toString();
                    emailText.setText(email);
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


       loadingBar.setVisibility(View.INVISIBLE);

    }

    private void Initialize() {


        username = getIntent().getStringExtra("userName");
        userNumber = getIntent().getStringExtra("userNumber");

        SetupTOolbar();

        Ref = FirebaseDatabase.getInstance().getReference();
        loadingBar = (ProgressBar)findViewById(R.id.load_user);
        mAuth = FirebaseAuth.getInstance();

        nameText = (TextView)findViewById(R.id.useraccount_name);
        emailText = (TextView)findViewById(R.id.useraccount_email);
        phoneNumberText = (TextView)findViewById(R.id.useraccount_number);
        privilegeText = (TextView)findViewById(R.id.useraccount_privilege);

    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.user_account_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
