package com.example.creditpartner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.creditpartner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaisaTrackerActivity extends AppCompatActivity {

    private RecyclerView latestTransactionsRecyclerView;
    private FloatingActionButton addExpenseFAB;
    private TextView totalExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paisa_tracker);

        Initialize();




    }

    private void Initialize() {
        latestTransactionsRecyclerView = (RecyclerView)findViewById(R.id.latest_transactions_recyclerview);
        addExpenseFAB = (FloatingActionButton)findViewById(R.id.add_expense_fab);
        totalExpense = (TextView)findViewById(R.id.total_expense);
    }
}
