package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.creditpartner.R;

public class AddAdminsActivity extends AppCompatActivity {

    private Button addAdmin, removeAdmin, addProduct, removeProduct;
    private String isSuperAdmin;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admins);
        Initialize();

    }

    private void Initialize() {

        SetupToolbar();

        isSuperAdmin = getIntent().getStringExtra("isSuperAdmin");

        addAdmin = (Button)findViewById(R.id.add_admin);
        removeAdmin = (Button)findViewById(R.id.remove_admin);
        addProduct = (Button)findViewById(R.id.add_product);
        removeProduct = (Button)findViewById(R.id.remove_product);

        if(isSuperAdmin.equals("True"))
        {
            addAdmin.setVisibility(View.VISIBLE);
            removeAdmin.setVisibility(View.VISIBLE);
        }
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_admin_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
