package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;

public class IFSCCodeFinderActivity extends AppCompatActivity {

    private EditText ifscCity, ifscState, ifscBank, ifscBranch;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private Button find;
    private String currentUSerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifsccode_finder);
        
        
        InitializeFields();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bank = ifscBank.getText().toString();
                String branch = ifscBranch.getText().toString();
                String city = ifscCity.getText().toString();
                String state = ifscState.getText().toString();



                if (bank.isEmpty()) {
                    ifscBank.setError("Invalid Bank!");
                } else if (branch.isEmpty()) {
                    ifscBranch.setError("Invalid Branch!");

                } else if (city.isEmpty()) {
                    ifscCity.setError("Invalid city!");
                }

                else if (state.isEmpty()) {
                    ifscState.setError("Invalid State!");
                }else {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("bank", bank);
                    hashMap.put("branch", branch);
                    hashMap.put("city", city);
                    hashMap.put("state", state);

                    Ref.child("IFSC").child(currentUSerID).child("FormInfo").push().setValue(hashMap);

                }
                }
        });
        
    }

    private void InitializeFields() {

        SetupToolbar();
        ifscBank = (EditText) findViewById(R.id.ifsc_bank);
        ifscBranch = (EditText) findViewById(R.id.ifsc_branch);
        ifscCity = (EditText) findViewById(R.id.ifsc_city);
        ifscState = (EditText) findViewById(R.id.ifsc_state);


        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUSerID = mAuth.getCurrentUser().getUid();
        find = (Button) findViewById(R.id.ifsc_find);

    }
    private void SetupToolbar() {
        mToolbar = findViewById(R.id.ifsc_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("IFSC Code Finder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
