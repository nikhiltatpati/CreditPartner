package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.creditpartner.Adapters.AdAdapter;
import com.example.creditpartner.Adapters.SideProductAdapter;
import com.example.creditpartner.Classes.Ads;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageButton additems;
    private DatabaseReference Ref;
    private ArrayList<Ads> adsArrayList = new ArrayList<>();
    private AdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);


        Initialize();

        SetupRecyclerView();

        ImplementSearch();


    }

    private void ImplementSearch() {

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.search_ads);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private void SetupRecyclerView() {


        additems.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String customerName = dataSnapshot1.child("customerName").getValue().toString();
                    String adImage = dataSnapshot1.child("adImage").getValue().toString();
                    String clicks = dataSnapshot1.child("noOfClicks").getValue().toString();
                    String type = dataSnapshot1.child("adType").getValue().toString();

                    adsArrayList.add(new Ads(customerName, clicks, type, adImage));

                }
                adapter = new AdAdapter(AdsActivity.this, adsArrayList);
                recyclerView.setAdapter(adapter);
                // loadProducts.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void Initialize() {
        SetupToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView)findViewById(R.id.ads_recycler);
        additems = (ImageButton)findViewById(R.id.add_items);
    }

    private void SetupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.ads_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Ads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}

