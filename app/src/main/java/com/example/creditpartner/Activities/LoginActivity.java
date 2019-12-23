package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference Ref;
    private Toolbar mToolbar;
    private TextInputEditText number;
    private Button LoginButton;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;
    private String otp;
    private TextView newUSer;
    String key;
    boolean isUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Initialize();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowLogin();
            }


        });

        newUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CustomerInfoActivity.class));
            }
        });


    }

    private void AllowLogin() {
        final String myNumber = number.getText().toString();

        if (myNumber.isEmpty()) {
            number.getText().toString();
        } else {

            Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.child("phoneNumber").getValue().toString().equals("+91" + myNumber)) {

                            isUser = true;
                            key = dataSnapshot1.getKey();


                            break;

                        }
                    }

                    if (isUser = false) {
                        Toast.makeText(LoginActivity.this, "No such user registered!", Toast.LENGTH_SHORT).show();
                    } else {
                        Ref.child("Customers").child("BasicInfo").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String name = dataSnapshot.child("name").getValue().toString();
                                String email = dataSnapshot.child("email").getValue().toString();
                                String reference = dataSnapshot.child("reference").getValue().toString();
                                String privilege = dataSnapshot.child("privilege").getValue().toString();
                                String number = "+91" + myNumber;
                                String type = "old";

                                SendDataToNextActivity(number, reference,email, privilege
                                        , name, type, key);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void SendDataToNextActivity(String number, String reference, String email,String priv, String name, String type, String key) {
        Intent intent = new Intent(LoginActivity.this, VerifyInfoActivity.class);
        intent.putExtra("phoneNumber", number);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        intent.putExtra("reference", reference);
        intent.putExtra("privilege", priv);
        intent.putExtra("key", key);
        intent.putExtra("type", type);
        startActivity(intent);

    }

    private void Initialize() {

        SetupTOolbar();

        Ref = FirebaseDatabase.getInstance().getReference();
        loadingBar = (ProgressBar) findViewById(R.id.load_login);
        loadingBar.setVisibility(View.INVISIBLE);
        number = (TextInputEditText) findViewById(R.id.number);
        LoginButton = (Button) findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();


        newUSer = (TextView) findViewById(R.id.new_user);

    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar) findViewById(R.id.login_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
