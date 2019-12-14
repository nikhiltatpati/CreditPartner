package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private EditText emailTIET, mobileTIET, referenceTIET, nameTIET;
    private TextView generateOTP;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private boolean isReferenceValid = false;

    private String mVerificationId, currentUserID;

    private static final String countryCode = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        InitializeFields();

        if (currentUSer != null) {
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
                final String reference = referenceTIET.getText().toString();
                ;

                CheckReference(reference);

                if (name.isEmpty() || name.length() < 5) {
                    nameTIET.setError("Enter your full name!");
                } else if (customerNumber.length() < 10) {
                    mobileTIET.setError("Enter a valid number!");

                } else if (!isEmailValid(email)) {
                    emailTIET.setError("Email is invalid!");
                } else {
                    Ref.child("References").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(reference))
                            {
                                SendDataToNextActivity(countryCode + mobileTIET.getText().toString(), referenceTIET.getText().toString(), emailTIET.getText().toString()
                                        , nameTIET.getText().toString(), referenceTIET.getText().toString());
                            }

                            else
                            {
                                referenceTIET.setError("Reference Code is invalid!");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        });


    }

    private void CheckReference(String ref) {

        Ref.child("References").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(ref)) {
                    isReferenceValid = true;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.e("after", "" + isReferenceValid);


    }

    private void SendDataToNextActivity(String phoneNumber, String referenceNumber, String email, String name, String reference) {


        Intent intent = new Intent(CustomerInfoActivity.this, VerifyInfoActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        intent.putExtra("reference", reference);
        intent.putExtra("privilege", "User");
        startActivity(intent);


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void InitializeFields() {


        //TextInputEditTexts
        emailTIET = (EditText) findViewById(R.id.email);
        mobileTIET = (EditText) findViewById(R.id.number);
        nameTIET = (EditText) findViewById(R.id.name);
        referenceTIET = (EditText) findViewById(R.id.reference);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();


        generateOTP = (TextView) findViewById(R.id.generate_button);
    }


}
