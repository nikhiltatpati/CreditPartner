package com.app.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.creditpartner.R;
import com.bumptech.glide.Glide;
import com.app.creditpartner.Activities.ProductDetailActivity;
import com.app.creditpartner.Activities.TaxInfoActivity;
import com.app.creditpartner.Classes.Products;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SideProductAdapter extends RecyclerView.Adapter<SideProductAdapter.ViewHolder> implements Filterable {

    private View myView;
    private Context mContext;
    private ArrayList<Products> productsArrayList;
    private ArrayList<Products> productsArrayListFull;

    private FirebaseAuth mAuth;
    private String currentUserID;

    public SideProductAdapter(Context mContext, ArrayList<Products> productsArrayList) {
        this.mContext = mContext;
        this.productsArrayList = productsArrayList;
        productsArrayListFull = new ArrayList<>(productsArrayList);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public SideProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_product_layout,parent,false);
        return new SideProductAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull SideProductAdapter.ViewHolder holder, int position) {

        final Products product = productsArrayList.get(position);
        holder.productName.setText(product.getProductName());
        Glide.with(mContext)
                .load(product.getProductImage())
                .into(holder.productImage);

        holder.productCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(product.getProductName().equals("Cibile Score"))
                {
                    Toast.makeText(mContext,"Coming soon! Hang on!", Toast.LENGTH_SHORT).show();
                }
                else if(product.getProductName().equals("Taxes")) {
                    Intent intent = new Intent(mContext.getApplicationContext(), TaxInfoActivity.class);
                    intent.putExtra("productName", product.getProductName());
                    intent.putExtra("currentUserID", currentUserID);
                    mContext.startActivity(intent);
                }
                else
                {


                    Intent intent = new Intent(mContext.getApplicationContext(), ProductDetailActivity.class);
                    intent.putExtra("productName", product.getProductName());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    @Override
    public Filter getFilter() {

        return categoryFilter;


    }


    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Products> filteredList = new ArrayList<>();

            if(constraint== null || constraint.length() ==0)
            {
                filteredList.addAll(productsArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Products products : productsArrayListFull)
                {
                    if(products.getProductName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(products);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productsArrayList.clear();
            productsArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };




    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private CardView productCardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = (ImageView)myView.findViewById(R.id.product_image);
            productName = (TextView)myView.findViewById(R.id.product_name);
            productCardview = (CardView)myView.findViewById(R.id.product_cardview);

        }
    }
}
