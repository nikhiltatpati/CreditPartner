package com.example.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.ApplyFormActivity;
import com.example.creditpartner.Activities.CompanyWebsiteActivity;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.R;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> implements Filterable {


    private View myView;
    private Context mContext;
    private ArrayList<Companies> companiesArrayList;
    private ArrayList<Companies> companiesArrayListFull;
    private String productName;
    private boolean isShown = false;

    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    public CompanyAdapter(Context mContext, ArrayList<Companies> companiesArrayList) {
        this.mContext = mContext;
        this.companiesArrayList = companiesArrayList;
        companiesArrayListFull = new ArrayList<>(companiesArrayList);
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_layout,parent,false);
        return new CompanyAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyAdapter.ViewHolder holder, int position) {

        final Companies companies = companiesArrayList.get(position);
        holder.companyName.setText(companies.getCompanyName());
        holder.companyInterestRate.setText(companies.getCompanyInterestRate());
        holder.companyMinimumBalance.setText(companies.getCompanyMinimumBalance());
        Glide.with(mContext)
                .load(companies.getCompanyImage())
                .into(holder.companyImage);
        holder.featuresText.setText(companies.getCompanyFeatures());



        SharedPreferences pref = mContext.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        productName = pref.getString("productTitle", null);

        holder.selectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), ApplyFormActivity.class);
                intent.putExtra("companyTitle",companies.getCompanyName());
                intent.putExtra("companyImage",companies.getCompanyImage());
                intent.putExtra("productTitle",productName);
                mContext.startActivity(intent);
            }
        });


        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isShown)
                {
                    holder.detailsCard.setVisibility(View.GONE);
                    holder.viewDetails.setText("View Details");
                    isShown = false;

                }
                else
                {
                    holder.detailsCard.setVisibility(View.VISIBLE);
                    holder.viewDetails.setText("Hide Details");
                    isShown = true;
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return companiesArrayList.size();
    }

    @Override
    public Filter getFilter() {

        return companyFIlter;


    }


    private Filter companyFIlter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Companies> filteredList = new ArrayList<>();

            if(constraint== null || constraint.length() ==0)
            {
                filteredList.addAll(companiesArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Companies companies : companiesArrayListFull)
                {
                    if(companies.getCompanyName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(companies);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            companiesArrayList.clear();
            companiesArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView companyImage;
        private TextView companyName, companyInterestRate, companyMinimumBalance;
        private Button selectCompany, viewDetails;
        private CardView detailsCard;
        private TextView featuresText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            companyImage = (ImageView)myView.findViewById(R.id.company_image);
            companyName = (TextView)myView.findViewById(R.id.company_name);
            companyInterestRate = (TextView)myView.findViewById(R.id.percent_rate);
            companyMinimumBalance = (TextView)myView.findViewById(R.id.minimum_balance_money);
            selectCompany = (Button)myView.findViewById(R.id.company_select);
            viewDetails = (Button)myView.findViewById(R.id.company_view_details);
            detailsCard = (CardView)myView.findViewById(R.id.details_card);
            featuresText = (TextView)myView.findViewById(R.id.features_text);

        }
    }
}

