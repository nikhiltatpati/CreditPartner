package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class PaisaTrackerActivity extends AppCompatActivity {

    private RecyclerView latestTransactionsRecyclerView;
    private FloatingActionButton addExpenseFAB;
    private TextView totalExpense;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private Toolbar mToolbar;
    private DatabaseReference Ref;
    private TextView noTransactions;
    private int totalValue;

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

        GetLatestTransactions();

        CalculateTotalExpense();


    }

    private void CalculateTotalExpense() {
        totalValue = 0;

        Ref.child("Transactions").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    int value = Integer.parseInt(dataSnapshot1.child("expenseValue").getValue().toString());
                    totalValue += value;

                }
                String totalString = String.valueOf(totalValue);
                totalExpense.setText(totalString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void GetLatestTransactions() {
        LinearLayoutManager manager = new LinearLayoutManager(PaisaTrackerActivity.this);
        latestTransactionsRecyclerView.setLayoutManager(manager);

        Ref.child("Transactions").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String categoryName = dataSnapshot1.child("categoryName").getValue().toString();
                        String expenseValue = dataSnapshot1.child("expenseValue").getValue().toString();
                        String date = dataSnapshot1.child("date").getValue().toString();

                        expensesArrayList.add(new Expenses(categoryName, expenseValue, date));
                    }

                    ExpenseAdapter adapter = new ExpenseAdapter(PaisaTrackerActivity.this, expensesArrayList);
                    latestTransactionsRecyclerView.setAdapter(adapter);


                } else {
                    noTransactions.setVisibility(View.VISIBLE);
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

        SetupTOolbar();
        latestTransactionsRecyclerView = (RecyclerView) findViewById(R.id.latest_transactions_recyclerview);
        addExpenseFAB = (FloatingActionButton) findViewById(R.id.add_expense_fab);
        totalExpense = (TextView) findViewById(R.id.total_expense);
        noTransactions = (TextView) findViewById(R.id.no_transactions);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();
    }

    private void SetupTOolbar() {
        mToolbar = (Toolbar) findViewById(R.id.paisa_tracker_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Paisa Tracker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
