package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.creditpartner.Adapters.CompanyAdapter;
import com.example.creditpartner.Adapters.TaxAdapter;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaxInfoActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<Companies> companiesArrayList = new ArrayList<>();
    private Toolbar mToolbar;
    private DatabaseReference Ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_info);


        Initialize();

        SetupRecyclerview();

    }

    private void SetupRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Ref.child("Taxes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companiesArrayList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    String companyImage = dataSnapshot1.child("companyImage").getValue().toString();
                    String companyname = dataSnapshot1.child("companyName").getValue().toString();
                    String value1 = dataSnapshot1.child("value1").getValue().toString();
                    String value2= dataSnapshot1.child("value2").getValue().toString();
                    String field1 = dataSnapshot1.child("field1").getValue().toString();
                    String field2 = dataSnapshot1.child("field2").getValue().toString();
                    String features = dataSnapshot1.child("companyFeatures").getValue().toString();

                    companiesArrayList.add(new Companies(companyImage, companyname, value1, value2, field1, field2, features));

                }

                TaxAdapter adapter = new TaxAdapter(TaxInfoActivity.this, companiesArrayList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    private void Initialize() {

        Ref = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView)findViewById(R.id.tax_recycler);
        mToolbar = (Toolbar)findViewById(R.id.taxesbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Taxes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
