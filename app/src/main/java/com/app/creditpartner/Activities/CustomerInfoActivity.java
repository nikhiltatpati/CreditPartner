package com.app.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerInfoActivity extends AppCompatActivity {

    private EditText emailTIET, mobileTIET, referenceTIET, nameTIET;
    private TextView generateOTP, oldUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private boolean isReferenceValid = false, isExist = false;


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

        oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerInfoActivity.this, LoginActivity.class));
            }
        });

        generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String customerNumber = mobileTIET.getText().toString();
                String email = emailTIET.getText().toString();
                String name = nameTIET.getText().toString();
                final String reference = referenceTIET.getText().toString();


                if(customerNumber.isEmpty())
                {
                    mobileTIET.setError("Enter a valid number!");

                }

                else if (name.isEmpty() || name.length() < 5) {
                    nameTIET.setError("Enter your full name!");
                } else if (customerNumber.length() != 10) {
                    mobileTIET.setError("Enter a valid number!");

                } else if (!isEmailValid(email)) {
                    emailTIET.setError("Email is invalid!");
                }
                else {

                    Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long k = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                long n = dataSnapshot.getChildrenCount();
                                Log.e("n", n + "");
                                if (dataSnapshot1.child("phoneNumber").getValue().toString().equals(countryCode + customerNumber)) {
                                    Toast.makeText(CustomerInfoActivity.this, "Already a user with this number exists. Try Login instead!", Toast.LENGTH_SHORT).show();
                                    isExist = true;
                                    mobileTIET.setText("");
                                    mobileTIET.requestFocus();
                                    break;
                                }
                                else
                                {
                                    ++k;
                                    if(k==n)
                                    {
                                        SendDataToNextActivity(countryCode + mobileTIET.getText().toString(), referenceTIET.getText().toString(), emailTIET.getText().toString()
                                                , nameTIET.getText().toString(), referenceTIET.getText().toString());

                                    }
                                    else {
                                        continue;
                                    }
                                }
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


    private void SendDataToNextActivity(String phoneNumber, String referenceNumber, String email, String name, String reference) {


        Intent intent = new Intent(CustomerInfoActivity.this, VerifyInfoActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        intent.putExtra("reference", reference);
        intent.putExtra("privilege", "User");
        intent.putExtra("key", "null");
        intent.putExtra("type", "new");
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

        oldUser = (TextView) findViewById(R.id.old_user);


        generateOTP = (TextView) findViewById(R.id.generate_button);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
