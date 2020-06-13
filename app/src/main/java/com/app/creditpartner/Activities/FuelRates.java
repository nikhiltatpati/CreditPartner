package com.app.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.creditpartner.R;


public class FuelRates extends AppCompatActivity {

    private WebView wv1;


    String url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_rates);

        Intent intent=getIntent();
        String state = intent.getStringExtra("currentState").toLowerCase();
        Log.i("TAG",state);
        url= "https://www.bankbazaar.com/fuel/petrol-price-"+state+".html";
        Log.i("TAG",url);
        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new FuelRates.MyBrowser());


        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);
    }



    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
