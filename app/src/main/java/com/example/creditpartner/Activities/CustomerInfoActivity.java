package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private String mVerificationId, currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        InitializeFields();

        generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid(mobileTIET, referenceTIET, emailTIET, nameTIET)) {

                    String phoneNumber = mobileTIET.getText().toString();
                    Intent intent = new Intent(CustomerInfoActivity.this, VerifyInfoActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);

                    SendDataToFirebase(phoneNumber, referenceTIET.getText().toString(), emailTIET.getText().toString()
                            , nameTIET.getText().toString());
                } else {
                    String customerNumber = mobileTIET.getText().toString();
                    String email = emailTIET.getText().toString();
                    String name = nameTIET.getText().toString();

                    if (customerNumber.length() < 13) {
                        mobileTIET.setError("Enter a valid number!");

                    }

                    if (name.isEmpty() || name.length() < 7) {
                        nameTIET.setError("Enter your full name!");

                    }
                    if (email.isEmpty()) {
                        emailTIET.setError("Enter a valid email!");

                    }
                }


            }
        });


    }

    private void SendDataToFirebase(String phoneNumber, String referenceNumber, String email, String name) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("email", email);
        hashMap.put("name", name);


        Ref.child("Customers").child("BasicInfo").child(currentUserID).setValue(hashMap);

    }

    private boolean isValid(TextInputEditText mobileTIET, TextInputEditText referenceTIET, TextInputEditText emailTIET, TextInputEditText nameTIET) {

        String customerNumber = mobileTIET.getText().toString();
        String email = emailTIET.getText().toString();
        String name = nameTIET.getText().toString();
        return (customerNumber.isEmpty() || customerNumber.length() < 10 || email.isEmpty() || name.isEmpty() ? false : true);


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
        currentUSer = mAuth.getCurrentUser();
        currentUserID = currentUSer.getUid();

        generateOTP = (Button) findViewById(R.id.generate_button);
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.customer_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Partner");

    }
}
