package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creditpartner.Adapters.ExpenseAdapter;
import com.example.creditpartner.Classes.Expenses;
import com.example.creditpartner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaisaTrackerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView latestTransactionsRecyclerView;
    private FloatingActionButton addExpenseFAB;
    private TextView totalExpense;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DatabaseReference Ref;
    private String month, monthM1, monthM2;
    private TextView noTransactions, latestTransactions;
    private int totalValue = 0;

    private Spinner typeSpinner;
    private ArrayList<Expenses> expensesArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paisa_tracker);

        Initialize();

        addExpenseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity(AddExpenseActivity.class);
            }
        });

        //     SetupSpinner();
        GetLatestTransactions();

        CalculateTotalExpense();


    }

    private void SetupSpinner() {
        typeSpinner.setOnItemSelectedListener(this);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("This month");
        categories.add("Last 3 months");
        categories.add("This year");
        categories.add("All time");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        typeSpinner.setAdapter(dataAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PaisaTrackerActivity.this, MainActivity.class));
        finish();
    }

    private void CalculateTotalExpense() {

        //  if(typeSpinner.getSelectedItem().equals("This month"))
        //{
        Ref.child("Transactions").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.hasChild("expenseValue")) {
                        int value = Integer.parseInt(dataSnapshot1.child("expenseValue").getValue().toString());
                        totalValue += value;
                    }
                }
                String totalString = String.valueOf(totalValue);
                totalExpense.setText("₹ " + totalString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //}
        //else if(typeSpinner.getSelectedItem().equals("Last 3 months"))
        //{

    }
    //}


    private void GetLatestTransactions() {
        LinearLayoutManager manager = new LinearLayoutManager(PaisaTrackerActivity.this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        latestTransactionsRecyclerView.setLayoutManager(manager);

        Ref.child("Transactions").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expensesArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("categoryName") && dataSnapshot1.hasChild("expenseValue")
                            && dataSnapshot1.hasChild("date")) {


                        String categoryName = dataSnapshot1.child("categoryName").getValue().toString();
                        String expenseValue = dataSnapshot1.child("expenseValue").getValue().toString();
                        String date = dataSnapshot1.child("date").getValue().toString();

                        expensesArrayList.add(new Expenses(categoryName, expenseValue, date));
                    }
                }

                ExpenseAdapter adapter = new ExpenseAdapter(PaisaTrackerActivity.this, expensesArrayList);
                latestTransactionsRecyclerView.setAdapter(adapter);

                if (expensesArrayList.size() == 0) {
                    noTransactions.setVisibility(View.VISIBLE);
                    latestTransactions.setVisibility(View.INVISIBLE);
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void ChangeActivity(Class Activity) {
        startActivity(new Intent(PaisaTrackerActivity.this, Activity));

    }

    private void Initialize() {


        latestTransactionsRecyclerView = (RecyclerView) findViewById(R.id.latest_transactions_recyclerview);
        addExpenseFAB = (FloatingActionButton) findViewById(R.id.add_expense_fab);
        totalExpense = (TextView) findViewById(R.id.total_expense);
        noTransactions = (TextView) findViewById(R.id.no_transactions);
        latestTransactions = (TextView) findViewById(R.id.latest_transactions);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();

        //    typeSpinner = (Spinner)findViewById(R.id.date_expenses);

        Calendar calendar = Calendar.getInstance();
        month = String.valueOf(calendar.get(Calendar.MONTH + 1));
        monthM1 = String.valueOf(calendar.get(Calendar.MONTH));
        monthM2 = String.valueOf(calendar.get(Calendar.MONTH - 1));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
