package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class AddUsersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;
    private Spinner privilegeSpinner;
    private TextInputEditText emailTIET, mobileTIET, nameTIET, idTIET;
    private Button addUserButton;

    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private static final String countryCode = "+91";
    private String type, key, res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);


        InitializeFields();

        if(type.equals("edit"))
        {
            GetDetails();
        }

        privilegeSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Privilege");

        categories.add("SuperAdmin");
        categories.add("Admin");
        categories.add("User");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        privilegeSpinner.setAdapter(dataAdapter);


        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String customerNumber = mobileTIET.getText().toString();



                String email = emailTIET.getText().toString();
                String name = nameTIET.getText().toString();
                String userid = idTIET.getText().toString();
                String privilege = privilegeSpinner.getSelectedItem().toString();

                if (name.isEmpty()) {
                    nameTIET.setError("Enter your full name!");
                }

                else if (!isEmailValid(email)){
                    emailTIET.setError("Email is invalid!");
                }

                else if (userid.isEmpty()){
                    Toast.makeText(AddUsersActivity.this, "Add a User ID!", Toast.LENGTH_SHORT).show();
                }

                else if (!isNotAvailable(mobileTIET.getText().toString()) || (customerNumber.length() < 10 || customerNumber.length() > 10)) {
                    mobileTIET.setError("Number is invalid or already registered!");

                }

                else if (privilege.equals("Select Privilege")){
                    Toast.makeText(AddUsersActivity.this, "Add a privilege!", Toast.LENGTH_SHORT).show();
                }


                else
                {
                    SendDataToDatabase(countryCode + customerNumber,privilegeSpinner.getSelectedItem().toString(),emailTIET.getText().toString()
                            , nameTIET.getText().toString(), idTIET.getText().toString());
                }


            }
        });



    }

    private boolean isNotAvailable(String customerNumber) {

        Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("phoneNumber").getValue().toString().equals("+91" + customerNumber));
                    {
                        res = "notAvailable";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(res == null)
        {
            return false;
        }
        return res == "notAvailable";


    }

    private void GetDetails() {

        Log.e("KEYYYY", "" + key);

        Ref.child("Customers").child("BasicInfo").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameTIET.setText(dataSnapshot.child("name").getValue().toString());
                emailTIET.setText(dataSnapshot.child("email").getValue().toString());
                mobileTIET.setText(dataSnapshot.child("phoneNumber").getValue().toString());
                if(dataSnapshot.hasChild("userID"))
                {
                    idTIET.setText(dataSnapshot.child("userID").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void SendDataToDatabase(String phoneNumber,String privilege, String email, String name, String id) {


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("privilege", privilege);
        hashMap.put("userID", id);

        Ref.child("Customers").child("BasicInfo").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddUsersActivity.this, "User added", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(AddUsersActivity.this, UsersActivity.class);
        startActivity(intent);
        finish();



    }

    private void InitializeFields() {


        SetupToolbar();

        key = getIntent().getStringExtra("key");
        type = getIntent().getStringExtra("type");



        //TextInputEditTexts
        emailTIET = (TextInputEditText) findViewById(R.id.useremail);
        mobileTIET = (TextInputEditText) findViewById(R.id.usernumber);
        nameTIET = (TextInputEditText) findViewById(R.id.username);
        idTIET = (TextInputEditText) findViewById(R.id.userid);
        privilegeSpinner = (Spinner) findViewById(R.id.privilegeSpinner);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();


        addUserButton = (Button) findViewById(R.id.add_user_button);
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_user_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
