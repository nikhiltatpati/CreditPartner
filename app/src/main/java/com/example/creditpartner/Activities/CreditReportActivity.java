package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private TextInputEditText panNumber, emailText, nameText, numberText;
    private Button getCreditReport;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUSerID;
    private DatabaseReference Ref;
    private ProgressBar loadingBar;

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
        String nameTextString = nameText.getText().toString();
        String numberTextString = numberText.getText().toString();

        if(!isEmailValid(emailTextString))
        {
            emailText.setError("Enter Valid Email");
        }

        else if(panNumberString.isEmpty())
        {
            panNumber.setError("Enter valid Pan Number");

        }
        else if(nameTextString.isEmpty())
        {
            nameText.setError("Enter Valid Name");
        }

        else if(numberTextString.isEmpty() || numberTextString.length()!= 10)
        {
            numberText.setError("Invalid number");
        }

        else
        {
            loadingBar.setVisibility(View.VISIBLE);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("panNumber", panNumberString);
            hashMap.put("email", emailTextString);
            hashMap.put("name", nameTextString);
            hashMap.put("number", numberTextString);
            Ref.child("CreditReportDetails").child(currentUSerID).push().setValue(hashMap);
            Toast.makeText(CreditReportActivity.this, "Credit Report added to Database", Toast.LENGTH_SHORT).show();
            loadingBar.setVisibility(View.INVISIBLE);

        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void Initialize() {

        SetupTOolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUSerID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();

        panNumber = (TextInputEditText)findViewById(R.id.credit_pan);
        nameText = (TextInputEditText)findViewById(R.id.credit_name);
        emailText = (TextInputEditText)findViewById(R.id.credit_email);
        numberText = (TextInputEditText)findViewById(R.id.credit_number);

        getCreditReport = (Button)findViewById(R.id.credit_button);

        loadingBar = (ProgressBar)findViewById(R.id.load_credit_report);


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.credit_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
