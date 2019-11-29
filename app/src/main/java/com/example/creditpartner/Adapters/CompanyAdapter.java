package com.example.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.CompanyWebsiteActivity;
import com.example.creditpartner.Activities.ProductDetailActivity;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.R;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {


    private View myView;
    private Context mContext;
    private ArrayList<Companies> companies;

    public CompanyAdapter(Context mContext, ArrayList<Companies> companies) {
        this.mContext = mContext;
        this.companies = companies;
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_layout,parent,false);
        return new CompanyAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, int position) {

        final Companies companies1 = companies.get(position);
        holder.companyName.setText(companies1.getCompanyName());
        Glide.with(mContext)
                .load(companies1.getCompanyImage())
                .into(holder.companyImage);

      /*  holder.productCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("productName", product.getProductName());
                mContext.startActivity(intent);
            }
        });*/

        holder.selectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), CompanyWebsiteActivity.class);
                intent.putExtra("companyTitle",companies1.getCompanyName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return companies.size();
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

