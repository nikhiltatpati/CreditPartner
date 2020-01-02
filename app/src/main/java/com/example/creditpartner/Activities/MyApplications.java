
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.creditpartner.Adapters.ApplicationAdapter;
import com.example.creditpartner.Adapters.SideProductAdapter;
import com.example.creditpartner.Classes.Applications;
import com.example.creditpartner.Classes.MyApplication;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyApplications extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private String decideScreen, currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private ArrayList<Applications> applicationsArrayList = new ArrayList<>();
    private ApplicationAdapter adapter;
    private TextView noApps;
    androidx.appcompat.widget.SearchView searchView;
    private ProgressBar loadProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_applications);

        Initialize();

        SetupRecyclerView();

        ImplementSearch();

    }

    private void ImplementSearch() {

        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.search_apps);


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


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Ref.child("My Applications").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                applicationsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image") && dataSnapshot1.hasChild("Date")) {
                        String productName = dataSnapshot1.child("Name").getValue().toString();
                        String productImage = dataSnapshot1.child("Image").getValue().toString();
                        String productDate = dataSnapshot1.child("Date").getValue().toString();

                        applicationsArrayList.add(new Applications(productImage, productName, productDate));
                    }
                }

                if (applicationsArrayList.size() == 0) {
                    noApps.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }

                else
                {
                    adapter = new ApplicationAdapter(MyApplications.this, applicationsArrayList);

                    recyclerView.setAdapter(adapter);

                }

                loadProducts.setVisibility(View.INVISIBLE);

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
        currentUserID = currentUser.getUid();
        recyclerView = (RecyclerView) findViewById(R.id.apps_recyclerview);
        noApps = (TextView) findViewById(R.id.no_apps);
        loadProducts = (ProgressBar) findViewById(R.id.load_companies);
    }

    private void SetupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.myapp_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Applications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
