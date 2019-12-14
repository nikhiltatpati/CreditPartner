package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class TaxWebsiteActivity extends AppCompatActivity {
    private WebView mWebView;
    private Toolbar mToolbar;

    private static final String URL = "https://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_website);

        Initialize();

        mWebView.loadUrl(URL);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    private void Initialize() {


        SetupTOolbar();


        mWebView = (WebView)findViewById(R.id.tax_webview);



    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.tax_web_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(URL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
