package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creditpartner.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdDetailsActiviy extends AppCompatActivity {

    private DatabaseReference Ref;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;
    private String customerName, adImage;
    private TextView nameText, sdate, edate, clicks, type;
    private CircleImageView circleImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details_activiy);

        Initialize();


        GetAdDetails();

    }

    private void GetAdDetails() {

        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("customerName").getValue().toString().equals(customerName) &&
                            dataSnapshot1.child("adImage").getValue().toString().equals(adImage))
                    {
                        nameText.setText(customerName);
                        Glide.with(AdDetailsActiviy.this)
                                .load(adImage)
                                .into(circleImageView);

                        clicks.setText("Clicks - " +dataSnapshot1.child("noOfClicks").getValue().toString());
                        type.setText("Type - " + dataSnapshot1.child("adType").getValue().toString());
                        sdate.setText(dataSnapshot1.child("startDate").getValue().toString());
                        edate.setText(dataSnapshot1.child("endDate").getValue().toString());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Initialize() {


        customerName = getIntent().getStringExtra("customerName");
        adImage = getIntent().getStringExtra("adImage");

        Ref = FirebaseDatabase.getInstance().getReference();
   //     loadingBar = (ProgressBar)findViewById(R.id.load_user);
        mAuth = FirebaseAuth.getInstance();

        nameText = (TextView)findViewById(R.id.ad_detail_name);
        sdate = (TextView)findViewById(R.id.ad_detail_sdate);
        edate = (TextView)findViewById(R.id.ad_detail_edtae);
        clicks = (TextView)findViewById(R.id.ad_detail_clicks);
        type = (TextView)findViewById(R.id.ad_detail_type);

        circleImageView = (CircleImageView)findViewById(R.id.ad_logo);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
