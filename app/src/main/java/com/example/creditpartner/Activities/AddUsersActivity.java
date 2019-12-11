package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddUsersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;
    private TextInputLayout emailTIL, nameTIL, mobileTIL;
    private Spinner privilegeSpinner;
    private TextInputEditText emailTIET, mobileTIET, nameTIET;
    private Button addUserButton;

    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private static final String countryCode = "+91";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);


        InitializeFields();

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
                String privilege = privilegeSpinner.getSelectedItem().toString();

                if (name.isEmpty() || name.length() < 5) {
                    nameTIET.setError("Enter your full name!");
                }

                else if (customerNumber.length() < 10) {
                    mobileTIET.setError("Enter a valid number!");

                }


                else if (!isEmailValid(email)){
                    emailTIET.setError("Email is invalid!");
                }

                else if (privilege.equals("Select")){
                    Toast.makeText(AddUsersActivity.this, "Add a privilege!", Toast.LENGTH_SHORT).show();
                }


                else
                {
                    SendDataToDatabase(countryCode + mobileTIET.getText().toString(),privilegeSpinner.getSelectedItem().toString(),emailTIET.getText().toString()
                            , nameTIET.getText().toString());
                }


            }
        });



    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void SendDataToDatabase(String phoneNumber,String privilege, String email, String name) {


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("privilege", privilege);

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

        //TextInputLayouts
        emailTIL = (TextInputLayout) findViewById(R.id.add_user_mail);
        mobileTIL = (TextInputLayout) findViewById(R.id.add_user_number);
        nameTIL = (TextInputLayout) findViewById(R.id.add_user_name);


        //TextInputEditTexts
        emailTIET = (TextInputEditText) findViewById(R.id.useremail);
        mobileTIET = (TextInputEditText) findViewById(R.id.usernumber);
        nameTIET = (TextInputEditText) findViewById(R.id.username);
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
