package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreditReportActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputEditText panNumber, emailText;
    private Button getCreditReport;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUSerID;
    private DatabaseReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_report);


        Initialize();


        getCreditReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetCreditReport();
            }
        });


    }

    private void GetCreditReport() {
        String emailTextString = emailText .getText().toString();
        String panNumberString = panNumber.getText().toString();

        if(emailTextString.isEmpty())
        {
            emailText.setError("Enter Valid Email");
        }

        else if(panNumberString.isEmpty())
        {
            panNumber.setError("Pan Number");

        }

        else
        {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("panNumber", panNumberString);
            hashMap.put("email", emailTextString);
            Ref.child("CreditReportDetails").child(currentUSerID).setValue(hashMap);

        }
    }

    private void Initialize() {

        SetupTOolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUSerID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.company_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


}
