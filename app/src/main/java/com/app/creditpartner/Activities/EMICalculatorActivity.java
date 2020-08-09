package com.app.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EMICalculatorActivity extends AppCompatActivity {

    private EditText loanText, rateText, tenureText;
    private TextView EMIText;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUSer;
    private Button calc;
    private String currentUSerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalculator);


        InitializeFields();

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st1 = loanText.getText().toString();
                String st2 = rateText.getText().toString();
                String st3 = tenureText.getText().toString();

                if (TextUtils.isEmpty(st1)) {
                    loanText.setError("Enter Prncipal Amount");
                    loanText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st2)) {
                    rateText.setError("Enter Interest Rate");
                    rateText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st3)) {
                    tenureText.setError("Enter Years");
                    tenureText.requestFocus();
                    return;
                }

                float p = Float.parseFloat(st1);
                float i = Float.parseFloat(st2);
                float y = Float.parseFloat(st3);

                float Principal = calPric(p);

                float Rate = calInt(i);

                float Months = calMonth(y);

                float Dvdnt = calDvdnt( Rate, Months);

                float FD = calFinalDvdnt (Principal, Rate, Dvdnt);

                float D = calDivider(Dvdnt);

                float emi = calEmi(FD, D);

                float TA = calTa (emi, Months);

                float ti = calTotalInt(TA, Principal);



                EMIText.setText("₹" + String.valueOf(emi));





            }
        });
    }

    public  float calPric(float p) {

        return (float) (p);

    }

    public  float calInt(float i) {

        return (float) (i/12/100);

    }

    public  float calMonth(float y) {

        return (float) (y * 12);

    }

    public  float calDvdnt(float Rate, float Months) {

        return (float) (Math.pow(1+Rate, Months));

    }

    public  float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {

        return (float) (Principal * Rate * Dvdnt);

    }

    public  float calDivider(float Dvdnt) {

        return (float) (Dvdnt-1);

    }

    public  float calEmi(float FD, Float D) {

        return (float) (FD/D);

    }

    public  float calTa(float emi, Float Months) {

        return (float) (emi*Months);

    }

    public  float calTotalInt(float TA, float Principal) {

        return (float) (TA - Principal);

    }


    private void InitializeFields() {

        SetupToolbar();
        loanText = (EditText) findViewById(R.id.emi_loan);
        rateText = (EditText) findViewById(R.id.emi_rate);
        tenureText = (EditText) findViewById(R.id.emi_tenure);
        EMIText = (TextView)findViewById(R.id.emi);

        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUSerID = mAuth.getCurrentUser().getUid();
        calc = (Button) findViewById(R.id.emi_calc);

    }
    private void SetupToolbar() {
        mToolbar = findViewById(R.id.emi_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("EMI Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
