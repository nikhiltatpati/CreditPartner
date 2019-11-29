package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.creditpartner.Adapters.CompanyAdapter;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String productTitle, currentUserID;
    private RecyclerView companyRecyclerview;
    private ArrayList<Companies> companiesArrayList = new ArrayList<>();
    private String companyName, companyImage;
    private TextView addCompanyButton;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUser;
    private ProgressBar loadCompanies;

    private final static String adminNumber = "+911234567890";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Initialize();

        SetupRecyclerview();

        CheckUserOrAdmin();


    }

    private void CheckUserOrAdmin() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("phoneNumber"))
                {
                    if(!dataSnapshot.child("phoneNumber").getValue().toString().equals(adminNumber))
                    {
                       addCompanyButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadCompanies.setVisibility(View.INVISIBLE);

    }

    private void SetupRecyclerview() {

        //Linear layout for all companies
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        companyRecyclerview.setLayoutManager(linearLayoutManager);

        //Get Company list from firebase
        Ref.child("CompanyList").child(productTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companiesArrayList.clear();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.hasChild("Name"))
                    {
                        companyName = dataSnapshot1.child("Name").getValue().toString();
                    }
                    if(dataSnapshot1.hasChild("Image"))
                    {
                        companyImage = dataSnapshot1.child("Image").getValue().toString();
                    }

                    companiesArrayList.add(new Companies(companyImage, companyName));
                }

                CompanyAdapter adapter = new CompanyAdapter(ProductDetailActivity.this, companiesArrayList);
                companyRecyclerview.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void Initialize() {


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        productTitle = getIntent().getStringExtra("productName");
        companyRecyclerview = (RecyclerView)findViewById(R.id.company_recyclerview);
        Ref = FirebaseDatabase.getInstance().getReference();
        addCompanyButton = (TextView)findViewById(R.id.add_company);
        loadCompanies = (ProgressBar)findViewById(R.id.load_companies);
        SetupToolbars();
    }

    private void SetupToolbars() {

        mToolbar = (Toolbar)findViewById(R.id.productdetail_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(productTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
