package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
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
    private String currentUSerID, companyTitle, productTitle, companyRate, type, companyImage,productID,murl,key;
    SecureRandom random = new SecureRandom();
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_form);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        Log.i("TAG_KEY",key);


        companyTitle = intent.getStringExtra("companyTitle");
        Log.i("TAG_proTitle",companyTitle);


        InitializeFields();
        getProductDetails();
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
//                    hashMap.put("code1", refCode1.getText().toString());
//                    hashMap.put("code2", refCode2.getText().toString());

                    Ref.child("My Applications").child(currentUSerID).push().setValue(hashMap);


                    Intent intent = new Intent(ApplyFormActivity.this, TaxWebsiteActivity.class);
                    intent.putExtra("companyTitle", companyTitle);
                    intent.putExtra("productTitle", productTitle);

                    intent.putExtra("url", murl);
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

                String privilege = dataSnapshot.child("privilege").getValue().toString();



                String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                mobileTIET.setText(phoneNumber.substring(3));


                String ref = dataSnapshot.child("reference").getValue().toString();
                //refCode1.setText(ref + generateRandom());

                if(ref.isEmpty() || ref.equals("") || ref.equals("NA")){
                    murl="https://primeindia.o18.link/c?o="+productID+"&m=2606&a=74725&sub_aff_id={"+phoneNumber+"}";
                    Log.i("TAG",murl);
                    if(privilege.equals("SuperAdmin")){
                        mTextView.setText(murl);
                    }

                }else{
                    murl="https://primeindia.o18.link/c?o="+productID+"&m=2606&a="+ref+"&sub_aff_id={"+phoneNumber+"}";

                    if(privilege.equals("SuperAdmin")){
                        mTextView.setText(murl);
                    }
                }


                String email = dataSnapshot.child("email").getValue().toString();
                emailTIET.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getProductDetails(){
        Ref.child("CompanyList").child(productTitle).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //name.setText(dataSnapshot.child("companyName").getValue().toString());
                // url.setText(dataSnapshot.child("companyURL").getValue().toString());
                productID=dataSnapshot.child("productID").getValue().toString();
//                value1.setText(dataSnapshot.child("value1").getValue().toString());
//                value2.setText(dataSnapshot.child("value2").getValue().toString());
//                value1.setText(dataSnapshot.child("value1").getValue().toString());
//                field1.setText(dataSnapshot.child("field1").getValue().toString());
//                field2.setText(dataSnapshot.child("field2").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String generateRandom() {
        StringBuilder sb = new StringBuilder( 8 );
        for( int i = 0; i < 8; i++ )
            sb.append( AB.charAt( random.nextInt(AB.length()) ) );
        return sb.toString();
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
      //  refCode1 = (EditText) findViewById(R.id.form__code1);
       // refCode2 = (EditText) findViewById(R.id.form_code2);
        gstText = (EditText) findViewById(R.id.form_gst_number);
        panText = (EditText) findViewById(R.id.form_pan_number);
        mobileTIET = (EditText) findViewById(R.id.form_number);
        nameTIET = (EditText) findViewById(R.id.form_name);
        pinTIET = (EditText) findViewById(R.id.form_pin_code);

        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUSerID = mAuth.getCurrentUser().getUid();
        applyForm = (TextView) findViewById(R.id.apply_form);
        mTextView=findViewById(R.id.form_url);



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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
