package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.creditpartner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCompanyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout companyName, companyURL, companyImage;
    private TextInputEditText name, url, image, balance, rate;
    private EditText features;
    private Button addCompanyButton;
    private String productTitle;
    private DatabaseReference Ref;
    private ProgressBar addProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        Initialize();

        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String urlString = url.getText().toString();
                String imageeString = image.getText().toString();
                String balanceString = balance.getText().toString();
                String rateString = rate.getText().toString();
                String featuresString = features.getText().toString();
                if(nameString.isEmpty())
                {
                    name.setError("Enter Valid name!");
                }
                else if(urlString.isEmpty())
                {
                    name.setError("Enter Valid url!");
                }else if(imageeString.isEmpty())
                {
                    name.setError("Enter Valid Image link!");
                }else if(balanceString.isEmpty())
                {
                    name.setError("Enter Valid balance!");
                }else if(rateString.isEmpty())
                {
                    name.setError("Enter Valid rate!");
                }

                addProgress.setVisibility(View.VISIBLE);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("companyName", name.getText().toString());
                hashMap.put("companyImage", image.getText().toString());
                hashMap.put("companyURL", url.getText().toString());
                hashMap.put("companyRate", rate.getText().toString());
                hashMap.put("companyMinimumBalance", balance.getText().toString());
                hashMap.put("companyFeatures", features.getText().toString());
                Ref.child("CompanyList").child(productTitle).push().setValue(hashMap);
                addProgress.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(AddCompanyActivity.this, ProductDetailActivity.class);
                startActivity(intent);
            }
        });


    }

    private void Initialize() {


        productTitle = getIntent().getStringExtra("productTitle");
        addProgress = (ProgressBar)findViewById(R.id.add_company_progressbar);
        addProgress.setVisibility(View.INVISIBLE);

        SetupToolbar();
        Ref = FirebaseDatabase.getInstance().getReference();
        //TextInputLayouts
        companyName = (TextInputLayout)findViewById(R.id.company_name);
        companyImage = (TextInputLayout) findViewById(R.id.company_image);
        companyURL = (TextInputLayout) findViewById(R.id.company_url);

        features = (EditText)findViewById(R.id.features);
        name = (TextInputEditText) findViewById(R.id.name);
        image = (TextInputEditText) findViewById(R.id.image);
        url = (TextInputEditText) findViewById(R.id.url);
        rate = (TextInputEditText) findViewById(R.id.rate);
        balance = (TextInputEditText) findViewById(R.id.balance);

        addCompanyButton = (Button)findViewById(R.id.add_company_button);

    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_company_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Company");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}




