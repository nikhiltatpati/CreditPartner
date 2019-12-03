package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAdminsActivity extends AppCompatActivity {

    private Button addAdmin, removeAdmin, addProduct, removeProduct;
    private String isSuperAdmin;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admins);
        Initialize();



        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAdminsActivity.this, DynamicAddActivity.class);
                intent.putExtra("optionSelected", addProduct.getText().toString());
                startActivity(intent);
            }
        });

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAdminsActivity.this, DynamicAddActivity.class);
                intent.putExtra("optionSelected", addAdmin.getText().toString());
                startActivity(intent);
            }
        });

        removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAdminsActivity.this, DynamicAddActivity.class);
                intent.putExtra("optionSelected", removeProduct.getText().toString());
                startActivity(intent);
            }
        });

        removeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAdminsActivity.this, DynamicAddActivity.class);
                intent.putExtra("optionSelected", removeAdmin.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void Initialize() {

        SetupToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUserID = currentUser.getUid();


        isSuperAdmin = getIntent().getStringExtra("isSuperAdmin");

        addAdmin = (Button)findViewById(R.id.add_admin);
        removeAdmin = (Button)findViewById(R.id.remove_admin);
        addProduct = (Button)findViewById(R.id.add_product);
        removeProduct = (Button)findViewById(R.id.remove_product);

        if(isSuperAdmin.equals("True"))
        {
            addAdmin.setVisibility(View.VISIBLE);
            removeAdmin.setVisibility(View.VISIBLE);
        }
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_admin_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
