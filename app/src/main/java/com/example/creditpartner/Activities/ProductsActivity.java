
package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.creditpartner.Adapters.CompanyAdapter;
import com.example.creditpartner.Adapters.ProductAdapter;
import com.example.creditpartner.Adapters.SideProductAdapter;
import com.example.creditpartner.Adapters.SideUserAdapter;
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

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private String decideScreen;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageButton additems;
    private DatabaseReference Ref;
    private String currentUserID, username, usernumber, usertype;
    private ArrayList<Products> productsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        Initialize();

        SetupRecyclerView(decideScreen);


    }

    private void SetupRecyclerView(String decideScreen) {

        if(decideScreen.equals("Products"))
        {
            additems.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);


            Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productsArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String productName = dataSnapshot1.child("Name").getValue().toString();
                        String productImage = dataSnapshot1.child("Image").getValue().toString();

                        productsArrayList.add(new Products(productName, productImage));

                    }
                    SideProductAdapter adapter = new SideProductAdapter(ProductsActivity.this, productsArrayList);
                    recyclerView.setAdapter(adapter);
                   // loadProducts.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        else if(decideScreen.equals("Ads"))
            {




            }

        else
        {


        }

    }

    private void Initialize() {
        SetupToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUserID = currentUser.getUid();

        recyclerView = (RecyclerView)findViewById(R.id.products_recycler);
        additems = (ImageButton)findViewById(R.id.add_items);
    }

    private void SetupToolbar() {

        decideScreen = getIntent().getStringExtra("decideScreen");

        mToolbar = (Toolbar) findViewById(R.id.product_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(decideScreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
