package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaxesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private Spinner genderSpinner;
    private EditText nameText, numberText, emailText, panText;
    private String productName, currentUserID;
    private TextView submitTax;
    private String privilege;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxes);

        Initialize();

        SetupSpinner();

        SetKnownDetails();

        submitTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitTax();
            }
        });


    }

    private void SubmitTax() {
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String phoneNumber = numberText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        String pan = panText.getText().toString();


        if(name.isEmpty())
        {
            nameText.setError("Invalid name!");
        }
        else if(email.isEmpty())
        {
            emailText.setError("Invalid email!");
        }else if(phoneNumber.isEmpty())
        {
            numberText.setError("Invalid number!");
        }else if(pan.isEmpty())
        {
            panText.setError("Invalid Pan Number!");
        }else if(gender.equals("Select Gender"))
        {
            Toast.makeText(TaxesActivity.this, "Please Select Gender!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            HashMap<String ,String> hashMap = new HashMap<>();
            hashMap.put("gender", gender);
            hashMap.put("panNumber", pan);
            hashMap.put("name", name);
            hashMap.put("phoneNumber", phoneNumber);
            hashMap.put("email", email);
            hashMap.put("privilege", privilege);

            Ref.child("Customers").child("BasicInfo").child(currentUserID).setValue(hashMap);

            startActivity(new Intent(TaxesActivity.this, TaxWebsiteActivity.class));
        }


    }

    private void SetKnownDetails() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                nameText.setText(name);

                privilege = dataSnapshot.child("privilege").getValue().toString();

                String email = dataSnapshot.child("email").getValue().toString();
                emailText.setText(email);

                String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                numberText.setText(phoneNumber);


                if(dataSnapshot.hasChild("panNumber"))
                {
                    panText.setText(dataSnapshot.child("panNumber").getValue().toString());
                }

                if(dataSnapshot.hasChild("gender"))
                {
                    String gender = dataSnapshot.child("panNumber").getValue().toString();
                    if(gender.equals("Male"))
                    {
                        genderSpinner.setSelection(0);
                    }
                    else
                    {
                        genderSpinner.setSelection(1);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SetupSpinner() {
        genderSpinner.setOnItemSelectedListener(this);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Gender");

        categories.add("Male");
        categories.add("Female");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        genderSpinner.setAdapter(dataAdapter);
    }

    private void Initialize() {

        productName = getIntent().getStringExtra("productName");


        SetupToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUserID = currentUser.getUid();

        nameText = (EditText)findViewById(R.id.tax_name);
        numberText = (EditText)findViewById(R.id.tax_number);
        panText = (EditText)findViewById(R.id.pan_number);
        emailText = (EditText)findViewById(R.id.tax_email);
        genderSpinner = (Spinner)findViewById(R.id.gender_spinner);

        submitTax = (TextView)findViewById(R.id.submit_tax);
    }

    private void SetupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.tax_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(productName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
