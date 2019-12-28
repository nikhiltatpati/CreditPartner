package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.creditpartner.Adapters.MyOfferAdapter;
import com.example.creditpartner.Adapters.SideUserAdapter;
import com.example.creditpartner.Classes.Notifications;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.Classes.Users;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyOfferActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private String currentUserID;
    private RecyclerView recyclerView;
    private ArrayList<Notifications> arrayList = new ArrayList<>();
    private Toolbar mToolbar;
    private TextView noOffers;
    private ImageButton addOffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offer);

        InitializeFields();

        SetupRecyclerView();

        CheckSuperAdmin();

        addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOfferActivity.this, NoticeToCustomerActivity.class));
            }
        });


    }

    private void CheckSuperAdmin() {


        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("privilege")) {
                    //  addAdminButton.setVisibility(View.VISIBLE);
                    if (dataSnapshot.child("privilege").getValue().equals("SuperAdmin")) {
                        addOffer.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void SetupRecyclerView() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Ref.child("MyOffers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (dataSnapshot1.hasChild("title") && dataSnapshot1.hasChild("message")
                            && dataSnapshot1.hasChild("date")) {

                        arrayList.add(new Notifications(dataSnapshot1.child("title").getValue().toString(),
                                dataSnapshot1.child("message").getValue().toString(),
                                dataSnapshot1.child("date").getValue().toString()));
                    }

                    if (arrayList.size() == 0) {
                        noOffers.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    MyOfferAdapter adapter = new MyOfferAdapter(MyOfferActivity.this, arrayList);
                    recyclerView.setAdapter(adapter);
                    // loadProducts.setVisibility(View.INVISIBLE);
                }



        }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    });

}

    private void InitializeFields() {

        SetupToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.my_offer_recycler);

        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();
        noOffers = (TextView) findViewById(R.id.no_offers);
        addOffer = (ImageButton) findViewById(R.id.add_offer);

    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.my_offer_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

}
