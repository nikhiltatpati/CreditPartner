package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyWebsiteActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String companyTitle;
    private WebView mWebView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUSerID, URL, productTitle;
    private DatabaseReference Ref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_website);

        Initialize();

        LoadURL();


        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    private void LoadURL() {


        Ref.child("CompanyList").child(productTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("companyName").getValue().toString().equals(companyTitle) && dataSnapshot1.hasChild("companyURL"))
                    {
                        URL = dataSnapshot1.child("companyURL").getValue().toString();
                        break;
                    }
                }
                mWebView .loadUrl(URL);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void Initialize() {

        companyTitle = getIntent().getStringExtra("companyTitle");
        productTitle = getIntent().getStringExtra("productTitle");
        SetupTOolbar();


        mWebView = (WebView)findViewById(R.id.company_webview);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUSerID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.company_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(companyTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
