package com.app.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class TaxWebsiteActivity extends AppCompatActivity {
    private WebView mWebView;
    private Toolbar mToolbar;
    private ProgressBar addProgress;


    private String murl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_website);

        Intent intent = getIntent();
        murl = intent.getStringExtra("url");
        Log.i("URL",murl);


        Initialize();



        mWebView.loadUrl(murl);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                addProgress = (ProgressBar) findViewById(R.id.add_company_progressbar);
                addProgress.setVisibility(View.INVISIBLE);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
