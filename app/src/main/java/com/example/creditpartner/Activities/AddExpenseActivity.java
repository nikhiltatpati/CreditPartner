package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText expense_value, category_name;
    private TextView addExpense;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID, category;
    private DatabaseReference Ref;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText expenseDate;
    String monthString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Initialize();

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTransactionTODatabase();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                monthString = String.valueOf(month + 1);
                updateLabel();


            }
        };

        category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddExpenseActivity.this, SeletCategoryActivity.class));
            }
        });

        expenseDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddExpenseActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        expenseDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void AddTransactionTODatabase() {
        String expense = expense_value.getText().toString();
        String category = category_name.getText().toString();
        String date = expenseDate.getText().toString();

        if (expense.isEmpty() || category.isEmpty() || date.length() < 7) {
            Toast.makeText(AddExpenseActivity.this, "Enter valid details", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("expenseValue", expense);
            hashMap.put("categoryName", category);
            hashMap.put("date", date);

            Ref.child("Transactions").child(currentUserID).push().setValue(hashMap);
            startActivity(new Intent(AddExpenseActivity.this, PaisaTrackerActivity.class));


        }




    }



    private void Initialize() {

        SetupToolbar();

        if(getIntent() != null) {
            category = getIntent().getStringExtra("productName");
        }
        expense_value = (EditText) findViewById(R.id.expense_value);
        category_name = (EditText) findViewById(R.id.category_name);
        category_name.setText(category);

        addExpense = (TextView) findViewById(R.id.add_transaction);
        expenseDate = (EditText) findViewById(R.id.expense_date);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        Ref = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddExpenseActivity.this, PaisaTrackerActivity.class));
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_expense_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
