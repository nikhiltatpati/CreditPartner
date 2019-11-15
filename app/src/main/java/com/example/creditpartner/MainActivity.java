package com.example.creditpartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();

        if(currentUser == null)
        {
            startActivity(new Intent(MainActivity.this, CustomerInfoActivity.class));
        }


    }

    private void Initialize() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }
}
