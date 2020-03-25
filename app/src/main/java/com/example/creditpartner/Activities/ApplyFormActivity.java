package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ApplyFormActivity extends AppCompatActivity {


    private EditText emailTIET, mobileTIET, pinTIET, nameTIET, gstText, panText, refCode1, refCode2;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private TextView applyForm;
    private String currentUSerID, companyTitle, productTitle, companyRate, type, companyImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_form);

        InitializeFields();

        SetKnownDetails();

        applyForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String customerNumber = mobileTIET.getText().toString();
                String email = emailTIET.getText().toString();
                String name = nameTIET.getText().toString();
                final String pin = pinTIET.getText().toString();



                if (name.isEmpty()) {
                    nameTIET.setError("Enter your full name!");
                } else if (customerNumber.length() != 10) {
                    mobileTIET.setError("Enter a valid number!");

                } else if (!isEmailValid(email)) {
                    emailTIET.setError("Email is invalid!");
                } else if (pin.isEmpty()) {
                    pinTIET.setError("Location is invalid!");
                } else {

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    String dates = formatter.format(date).toString();


                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Date", dates);
                    hashMap.put("Name", companyTitle);
                    hashMap.put("Image", companyImage);
                    hashMap.put("myName", name);
                    hashMap.put("myEmail", email);
                    hashMap.put("myPin", pin);
                    hashMap.put("myMobile", customerNumber);
                    hashMap.put("code1", refCode1.getText().toString());
                    hashMap.put("code2", refCode2.getText().toString());

                    Ref.child("My Applications").child(currentUSerID).push().setValue(hashMap);


                    Intent intent = new Intent(ApplyFormActivity.this, TaxWebsiteActivity.class);
                    intent.putExtra("companyTitle", companyTitle);
                    intent.putExtra("productTitle", productTitle);
                    startActivity(intent);

                }


            }
        });


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void SetKnownDetails() {

        Ref.child("Customers").child("BasicInfo").child(currentUSerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                nameTIET.setText(name);


                String email = dataSnapshot.child("email").getValue().toString();
                emailTIET.setText(email);

                String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                mobileTIET.setText(phoneNumber.substring(3));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void InitializeFields() {


        SetupToolbar();

        companyTitle = getIntent().getStringExtra("companyTitle");
        productTitle = getIntent().getStringExtra("productTitle");
        companyRate = getIntent().getStringExtra("companyRate");
        companyImage = getIntent().getStringExtra("companyImage");

        type = getIntent().getStringExtra("type");


        //TextInputEditTexts
        emailTIET = (EditText) findViewById(R.id.form_email);
        refCode1 = (EditText) findViewById(R.id.form__code1);
        refCode2 = (EditText) findViewById(R.id.form_code2);
        gstText = (EditText) findViewById(R.id.form_gst_number);
        panText = (EditText) findViewById(R.id.form_pan_number);
        mobileTIET = (EditText) findViewById(R.id.form_number);
        nameTIET = (EditText) findViewById(R.id.form_name);
        pinTIET = (EditText) findViewById(R.id.form_pin_code);

        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUSerID = mAuth.getCurrentUser().getUid();
        applyForm = (TextView) findViewById(R.id.apply_form);


        if (type.equals("Pan")) {
            panText.setVisibility(View.VISIBLE);
        } else if (type.equals("GST")) {
            gstText.setVisibility(View.VISIBLE);
        }


    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.apply_form_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Apply Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

}
