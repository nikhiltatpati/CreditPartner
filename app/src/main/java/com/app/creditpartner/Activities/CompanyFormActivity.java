package com.app.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.creditpartner.R;


public class CompanyFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_form);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

