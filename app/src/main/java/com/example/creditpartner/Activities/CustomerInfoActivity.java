package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomerInfoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout emailTIL, nameTIL, mobileTIL, referenceTIL;
    private TextInputEditText emailTIET, mobileTIET, referenceTIET, nameTIET;
    private Button generateOTP;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private String isReferenceValid = "False";

    private String mVerificationId, currentUserID;

    private static final String countryCode = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        InitializeFields();

        if(currentUSer != null)
        {
            Intent intent = new Intent(CustomerInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String customerNumber = mobileTIET.getText().toString();
                    String email = emailTIET.getText().toString();
                    String name = nameTIET.getText().toString();
                    final String reference = referenceTIET.getText().toString();;

                    if (name.isEmpty() || name.length() < 5) {
                    nameTIET.setError("Enter your full name!");
                    }

                    if (customerNumber.length() < 10) {
                        mobileTIET.setError("Enter a valid number!");

                    }


                    if (!isEmailValid(email)){
                        emailTIET.setError("Email is invalid!");
                    }


                   else
                    {
                        SendDataToNextActivity(countryCode + mobileTIET.getText().toString(), referenceTIET.getText().toString(), emailTIET.getText().toString()
                                , nameTIET.getText().toString(), referenceTIET.getText().toString());
                }


            }
        });


    }

    private void SendDataToNextActivity(String phoneNumber, String referenceNumber, String email, String name, String reference) {


        Intent intent = new Intent(CustomerInfoActivity.this, VerifyInfoActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        intent.putExtra("reference", reference);

        startActivity(intent);



    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void InitializeFields() {


        SetupToolbar();

        //TextInputLayouts
        emailTIL = (TextInputLayout) findViewById(R.id.customer_email);
        mobileTIL = (TextInputLayout) findViewById(R.id.customer_number);
        nameTIL = (TextInputLayout) findViewById(R.id.customer_name);
        referenceTIL = (TextInputLayout) findViewById(R.id.customer_reference);


        //TextInputEditTexts
        emailTIET = (TextInputEditText) findViewById(R.id.email);
        mobileTIET = (TextInputEditText) findViewById(R.id.number);
        nameTIET = (TextInputEditText) findViewById(R.id.name);
        referenceTIET = (TextInputEditText) findViewById(R.id.reference);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();


        generateOTP = (Button) findViewById(R.id.generate_button);
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.customer_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Partner");

    }
}
