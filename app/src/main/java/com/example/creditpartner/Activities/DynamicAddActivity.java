package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DynamicAddActivity extends AppCompatActivity {

    private EditText addAdminNumber, addProductName, addProductImage;
    private Button addButton, removeButton;
    private String optionSelected;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private String currentUserID,key;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_add);

        Initialize();

        GetSuitableViewsAndPerformAction();




    }

    private void GetSuitableViewsAndPerformAction() {
        if (optionSelected.equals("Add Product")) {
            addAdminNumber.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productname = addProductName.getText().toString();
                    String productimage = addProductImage.getText().toString();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Image", productimage);
                    hashMap.put("Name", productname);

                    Ref.child("ProductList").push().setValue(hashMap);

                    ChangeActivity();

                }
            });



        } else if (optionSelected.equals("Add Admin")) {
            addProductName.setVisibility(View.GONE);
            addProductImage.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);


            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String adminnumber = addAdminNumber.getText().toString();
                    Ref.child("Privileges").child(adminnumber).setValue("Admin");

                    ChangeActivity();

                }
            });

        } else if (optionSelected.equals("Remove Product")) {
            addProductImage.setVisibility(View.GONE);
            addAdminNumber.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String productname = addProductName.getText().toString();
                    Log.e("s", productname);

                    Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                if(dataSnapshot1.child("Name").getValue().toString().equals(productname))
                                {
                                    key = dataSnapshot1.getKey();
                                    Ref.child("ProductList").child(key).removeValue();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    ChangeActivity();


                }
            });
        } else {

            addProductName.setVisibility(View.GONE);
            addProductImage.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String adminnumber = addAdminNumber.getText().toString();
                    Ref.child("Privileges").child(adminnumber).removeValue();
                }
            });
            ChangeActivity();

        }
    }

    private void ChangeActivity() {
        startActivity(new Intent(DynamicAddActivity.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void Initialize() {
        optionSelected = getIntent().getStringExtra("optionSelected");

        SetupToolbar();
        addAdminNumber = (EditText) findViewById(R.id.add_admin_number);
        addProductImage = (EditText) findViewById(R.id.add_product_image);
        addProductName = (EditText) findViewById(R.id.add_product_name);

        addButton = (Button) findViewById(R.id.add_option_button);
        removeButton = (Button) findViewById(R.id.remove_option_button);

        Ref = FirebaseDatabase.getInstance().getReference();

    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.options_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(optionSelected);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
