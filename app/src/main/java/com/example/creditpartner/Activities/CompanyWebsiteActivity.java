package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.creditpartner.R;

public class CompanyWebsiteActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String companyTitle;
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_website);

        Initialize();


        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView .loadUrl("http://www.google.com");

    }

    private void Initialize() {

        companyTitle = getIntent().getStringExtra("companyTitle");
        SetupTOolbar();

        mWebView = (WebView)findViewById(R.id.company_webview);


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.company_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(companyTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
