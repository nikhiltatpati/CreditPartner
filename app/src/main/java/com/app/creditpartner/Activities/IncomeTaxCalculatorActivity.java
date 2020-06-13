package com.app.creditpartner.Activities;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.creditpartner.R;


public class IncomeTaxCalculatorActivity extends AppCompatActivity {

    EditText et;
    TextView tx;
    TextView tx1;
    Button b1;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_tax_calculator);

        et=findViewById(R.id.edit1);
        tx=findViewById(R.id.text1);
        tx1=findViewById(R.id.text2);
        b1=findViewById(R.id.button3);

        SetupTOolbar();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Integer.parseInt(String.valueOf(et.getText()));
                    calculate();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "You have exceeded the Input Limit!", Toast.LENGTH_LONG).show();
                    tx1.setText("");
                }
            }
        });
    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar) findViewById(R.id.income_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Income Tax Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @SuppressLint("SetTextI18n")
    public void calculate()
    {
        long Total = 0;
        long Tax=0;
        long in= Integer.parseInt(et.getText().toString());
        if (in >= 200000 && in < 1000000)
        {
            Tax= (in * 5)/100;
            Total = in + Tax;
        }
        else if(in >= 1000000 && in < 2000000)
        {
            Tax = (in * 10)/100;
            Total = in + Tax;
        }
        else if(in >= 2000000 && in < 3000000)
        {
            Tax= (in * 15)/100;
            Total = in + Tax;
        }
        else if (in >= 3000000 && in < 4000000)
        {
            Tax = (in * 20)/100;
            Total = in + Tax;
        }
        else if(in >= 4000000 && in < 5000000)
        {
            Tax = (in * 25)/100;
            Total = in + Tax;
        }
        else if (in >= 5000000 && in < 7000000)
        {
            Tax = (in * 30)/100;
            Total = in + Tax;
        }
        else if (in >= 7000000 && in < 10000000)
        {
            Tax = (in * 35)/100;
            Total = in + Tax;
        }
        else if (in >= 10000000)
        {
            Tax = (in * 40)/100;
            Total = in + Tax;
        }
        tx1.setText("Tax on your income "+et.getText()+"= \t"+Tax+"\n \n"+
                "Total Income (Inclusion of Tax) "+"= \t"+Total);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}