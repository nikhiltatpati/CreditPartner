package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offer);

        InitializeFields();

        SetupRecyclerView();


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

                    if(dataSnapshot1.hasChild("title") && dataSnapshot1.hasChild("message")
                    && dataSnapshot1.hasChild("date"))
                    {

                        arrayList.add(new Notifications(dataSnapshot1.child("title").getValue().toString(),
                                dataSnapshot1.child("message").getValue().toString(),
                                dataSnapshot1.child("date").getValue().toString()));
                    }



                    MyOfferAdapter adapter = new MyOfferAdapter(MyOfferActivity.this, arrayList);
                    recyclerView.setAdapter(adapter);
                    // loadProducts.setVisibility(View.INVISIBLE);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void InitializeFields() {

        SetupToolbar();

        recyclerView = (RecyclerView)findViewById(R.id.my_offer_recycler);

        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();

    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.my_offer_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

}
