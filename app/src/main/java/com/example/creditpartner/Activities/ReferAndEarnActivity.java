package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.creditpartner.R;

import java.sql.Ref;

public class ReferAndEarnActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button referButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        Initialize();

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenWhatsapp();
            }
        });


    }

    private void OpenWhatsapp() {

        String url = "https://api.whatsapp.com/send?phone=";
        final Intent whatIntent = new Intent(android.content.Intent.ACTION_SEND);

        whatIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("CreditPartner is a good app take my link and open" ));
        whatIntent.setType("text/plain");
        startActivity(Intent.createChooser(whatIntent, "Send to friend"));
    }

    private void Initialize() {
        SetupTOolbar();
        referButton = (Button)findViewById(R.id.refer_button);

    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar) findViewById(R.id.refer_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Refer And Earn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
