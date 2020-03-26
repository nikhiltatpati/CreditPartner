package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BannerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUSerID,adLink;
    private DatabaseReference Ref;
    private WebView mWebView;
    private Toolbar mToolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        Initialize();
        mWebView .loadUrl(adLink);

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript


        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void Initialize() {

        adLink = getIntent().getStringExtra("adLink");
        mWebView = (WebView)findViewById(R.id.banner_webview);
        SetupTOolbar();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUSerID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();


    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar) findViewById(R.id.banner_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(adLink);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
