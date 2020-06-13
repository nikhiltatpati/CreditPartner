package com.app.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.app.creditpartner.R;

import java.util.Set;

public class FirebaseActivity extends AppCompatActivity {

    private WebView mWebView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        SetupTOolbar();

        mWebView = (WebView)findViewById(R.id.firebase_view);



    }


    @Override
    protected void onStart() {
        super.onStart();
        mWebView.loadUrl("https://console.firebase.google.com/u/1/project/paisabazaar-6049f/notification/compose");
        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar)findViewById(R.id.fire_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notice to Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
