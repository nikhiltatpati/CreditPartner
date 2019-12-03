package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;

public class AddExpenseActivity extends AppCompatActivity {
    
    private EditText expense_value, category_name;
    private DatePicker datePicker;
    private TextView addExpense;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DatabaseReference Ref;
    

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
        
    }

    private void AddTransactionTODatabase() {
        String expense = expense_value.getText().toString();
        String category = category_name.getText().toString();
        int date_day = datePicker.getDayOfMonth();
        int date_month = datePicker.getMonth();
        int date_year = datePicker.getYear();
        String date = date_day + "/" + date_month + "/" + date_year;

        if(expense.isEmpty() || category.isEmpty() || date.length() < 7)
        {
            Toast.makeText(AddExpenseActivity.this,"Enter valid details",Toast.LENGTH_LONG).show();
        }

        else
        {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("expenseValue", expense);
            hashMap.put("categoryName", category);
            hashMap.put("date", date);

            Ref.child("Transactions").child(currentUserID).push().setValue(hashMap);
            startActivity(new Intent(AddExpenseActivity.this, PaisaTrackerActivity.class));
            finish();


        }


    }

    private void Initialize() {
        
        SetupToolbar();
        expense_value = (EditText)findViewById(R.id.expense_value);
        category_name = (EditText)findViewById(R.id.category_name);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        addExpense = (TextView) findViewById(R.id.add_transaction);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        Ref = FirebaseDatabase.getInstance().getReference();
        
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_expense_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
