package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicationDetailsActivity extends AppCompatActivity {

    private TextView emailTIET, mobileTIET, pinTIET, nameTIET, productName, rateText, mbText;
    private CircleImageView productImage;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private String currentUSerID, date, name,Image;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);
        
        Initialize();


        GetKey();
        
        SetupDetails();

        GetCompanyDetails();
        
    }

    private void GetCompanyDetails() {

        Ref.child("CompanyList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren())
                    {
                        if(dataSnapshot11.child("companyName").getValue().toString().equals(name) &&
                                dataSnapshot11.child("companyImage").getValue().toString().equals(Image))
                        {
                            rateText.setText(dataSnapshot11.child("companyRate").getValue().toString());
                            mbText.setText(dataSnapshot11.child("companyMinimumBalance").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void GetKey() {
        Ref.child("My Applications").child(currentUSerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("Name").getValue().toString().equals(name))
                    {
                        nameTIET.setText(dataSnapshot1.child("myName").getValue().toString());
                        mobileTIET.setText(dataSnapshot1.child("myMobile").getValue().toString());
                        emailTIET.setText(dataSnapshot1.child("myEmail").getValue().toString());
                        pinTIET.setText("Pin Code - " + dataSnapshot1.child("myPin").getValue().toString());

                        loadingBar.setVisibility(View.GONE);


                        break;

                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void SetupDetails() {

        productName.setText(name);
        Glide.with(ApplicationDetailsActivity.this)
        .load(Image)
        .into(productImage);
    }

    private void Initialize() {

        name = getIntent().getStringExtra("name");
        Image = getIntent().getStringExtra("image");
        date = getIntent().getStringExtra("date");

        SetupToolbar();

        loadingBar =(ProgressBar)findViewById(R.id.load);
        emailTIET = (TextView) findViewById(R.id.appdetail_email);
        mobileTIET = (TextView) findViewById(R.id.appdetail_number);
        nameTIET = (TextView) findViewById(R.id.appdetail_myname);
        pinTIET = (TextView) findViewById(R.id.appdetail_pin);
        rateText = (TextView) findViewById(R.id.app_rate);
        mbText = (TextView) findViewById(R.id.app_mb);
        productImage = (CircleImageView)findViewById(R.id.appdetail_image);
        productName = (TextView)findViewById(R.id.appdetail_name);


        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUSerID = mAuth.getCurrentUser().getUid();


    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.appdetail_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
