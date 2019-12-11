package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddAdsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputEditText imagetiet, urltiet, nameTIET, clickstiet;
    private Spinner adTypeSpinner;
    private EditText startDate, endDate;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private DatabaseReference Ref;
    private Button addAdsButton;
    final Calendar myCalendar = Calendar.getInstance();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);

        InitializeFields();

        adTypeSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select AdType");

        categories.add("Banner");
        categories.add("Pop-Up");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        adTypeSpinner.setAdapter(dataAdapter);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();


            }
        };

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();


            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAdsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAdsActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        addAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameTIET.getText().toString();
                String image = imagetiet.getText().toString();

                String url = urltiet.getText().toString();
                String clicks = clickstiet.getText().toString();
                String type = adTypeSpinner.getSelectedItem().toString();
                String startdate = startDate.getText().toString();
                String enddate = endDate.getText().toString();

                if (name.isEmpty()) {
                    nameTIET.setError("Cannot be empty!");
                } else if (url.isEmpty()) {
                    urltiet.setError("Cannot be empty!");

                } else if (image.isEmpty()) {
                    urltiet.setError("Cannot be empty!");

                } else if (startdate.isEmpty()) {
                    urltiet.setError("Cannot be empty!");

                } else if (enddate.isEmpty()) {
                    urltiet.setError("Cannot be empty!");

                } else if (clicks.isEmpty()) {
                    clickstiet.setError("Cannot be empty!");

                } else if (type.equals("Select")) {
                    Toast.makeText(AddAdsActivity.this, "Add Type of Ad!", Toast.LENGTH_SHORT).show();
                } else {
                    SendDataToDatabase(name, url, clicks, image, startdate, enddate, type);
                }


            }
        });


    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void SendDataToDatabase(String name, String url, String clicks, String image, String startdate, String enddate, String type) {


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("customerName", name);
        hashMap.put("adImage", image);
        hashMap.put("adLink", url);
        hashMap.put("adType", type);
        hashMap.put("noOfClicks", clicks);
        hashMap.put("startDate", startdate);
        hashMap.put("endDate", enddate);

        Ref.child("Banners").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddAdsActivity.this, "Ad added", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(AddAdsActivity.this, AdsActivity.class);
        startActivity(intent);
        finish();
    }



    private void InitializeFields() {


        SetupToolbar();


        //TextInputEditTexts
        imagetiet = (TextInputEditText) findViewById(R.id.adimagelink);
        clickstiet = (TextInputEditText) findViewById(R.id.adclicks);
        urltiet = (TextInputEditText) findViewById(R.id.adurl);
        nameTIET = (TextInputEditText) findViewById(R.id.customername);
        adTypeSpinner = (Spinner) findViewById(R.id.ad_type_spinner);
        startDate = (EditText)findViewById(R.id.start_date);
        endDate = (EditText)findViewById(R.id.end_date);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();

        addAdsButton = (Button) findViewById(R.id.add_ad_button);


    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_ads_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Ads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


