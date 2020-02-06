package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RechargeActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String companyTitle;
    private WebView mWebView;
    private static final String URL = "https://recharge.spicetech.in";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);


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


        mWebView.loadUrl(URL);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void Initialize() {

        SetupTOolbar();


        mWebView = (WebView) findViewById(R.id.recharge_webview);


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar) findViewById(R.id.recharge_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Recharge");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
