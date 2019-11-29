package com.example.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.CompanyWebsiteActivity;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.R;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {


    private View myView;
    private Context mContext;
    private ArrayList<Companies> companiesArrayList;
    private String productName;

    public CompanyAdapter(Context mContext, ArrayList<Companies> companiesArrayList) {
        this.mContext = mContext;
        this.companiesArrayList = companiesArrayList;
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_layout,parent,false);
        return new CompanyAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, int position) {

        final Companies companies = companiesArrayList.get(position);
        holder.companyName.setText(companies.getCompanyName());
        Glide.with(mContext)
                .load(companies.getCompanyImage())
                .into(holder.companyImage);


        SharedPreferences pref = mContext.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        productName = pref.getString("productTitle", null);

        holder.selectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), CompanyWebsiteActivity.class);
                intent.putExtra("companyTitle",companies.getCompanyName());
                intent.putExtra("productTitle",productName);
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return companiesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView companyImage;
        private TextView companyName;
        private Button selectCompany;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            companyImage = (ImageView)myView.findViewById(R.id.company_image);
            companyName = (TextView)myView.findViewById(R.id.company_name);
            selectCompany = (Button)myView.findViewById(R.id.company_select);

        }
    }
}

