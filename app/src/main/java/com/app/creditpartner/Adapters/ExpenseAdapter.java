package com.app.creditpartner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.creditpartner.Classes.Expenses;
import com.app.creditpartner.R;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Expenses> expensesArrayList;
    private View mView;

    public ExpenseAdapter(Context mContext, ArrayList<Expenses> expensesArrayList) {
        this.mContext = mContext;
        this.expensesArrayList = expensesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_layout,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Expenses expenses = expensesArrayList.get(position);
        holder.categoryName.setText(expenses.getCategoryName());
        holder.expense.setText("₹ "+expenses.getExpenseValue());
        holder.date.setText(expenses.getDate());

    }

    @Override
    public int getItemCount() {
        return expensesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName, expense, date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = (TextView)mView.findViewById(R.id.category_name);
            expense = (TextView)mView.findViewById(R.id.expense);
            date = (TextView)mView.findViewById(R.id.date);

        }
    }
}
