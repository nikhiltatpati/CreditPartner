package com.example.creditpartner.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditpartner.R;

public class DynamicAddActivity extends AppCompatActivity {

    private EditText addAdminNumber, addProductName, addProductImage;
    private Button addButton, removeButton;
    private String optionSelected;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_add);

        Initialize();

        GetSuitableViews();

        Log.e("option", optionSelected + "");


    }

    private void GetSuitableViews() {
        if (optionSelected.equals("Add Product")) {
            addAdminNumber.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);

        } else if (optionSelected.equals("Add Admin")) {
            addProductName.setVisibility(View.GONE);
            addProductImage.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);

        } else if (optionSelected.equals("Remove Product")) {
            addProductImage.setVisibility(View.GONE);
            addAdminNumber.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
        } else {

            addProductName.setVisibility(View.GONE);
            addProductImage.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void Initialize() {
        optionSelected = getIntent().getStringExtra("optionSelected");

        SetupToolbar();
        addAdminNumber = (EditText) findViewById(R.id.add_admin_number);
        addProductImage = (EditText) findViewById(R.id.add_product_image);
        addProductName = (EditText) findViewById(R.id.add_product_name);

        addButton = (Button) findViewById(R.id.add_option_button);
        removeButton = (Button) findViewById(R.id.remove_option_button);

    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.options_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(optionSelected);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
