package com.app.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.creditpartner.R;
import com.bumptech.glide.Glide;
import com.app.creditpartner.Activities.CreditReportActivity;
import com.app.creditpartner.Activities.GoldRates;
import com.app.creditpartner.Activities.ProductDetailActivity;
import com.app.creditpartner.Classes.Products;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private View myView;
    private Context mContext;
    private ArrayList<Products> products;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public ProductAdapter(Context mContext, ArrayList<Products> products) {
        this.mContext = mContext;
        this.products = products;

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
        return new ProductAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        final Products product = products.get(position);
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

                else if(product.getProductName().equals("Tax Returns") ||product.getProductName().equals("Tax Registration") )
                {
                    Toast.makeText(mContext,"Under Construction!", Toast.LENGTH_SHORT).show();
                }
                else if(product.getProductName().equals("Credit Score"))
                {
                    Intent intent = new Intent(mContext.getApplicationContext(), CreditReportActivity.class);

                    mContext.startActivity(intent);
                }
                else if(product.getProductName().equals("Amazon")){
                    Intent intent = new Intent(mContext, GoldRates.class);
                    intent.putExtra("currentState",  product.getProductName());
                    mContext.startActivity(intent);

                }

                else if(product.getProductName().equals("Flipkart")){
                    Intent intent = new Intent(mContext, GoldRates.class);
                    intent.putExtra("currentState", product.getProductName());
                    mContext.startActivity(intent);

                }

                else if(product.getProductName().equals("Snapdeal")){
                    Intent intent = new Intent(mContext, GoldRates.class);
                    intent.putExtra("currentState",  product.getProductName());
                    mContext.startActivity(intent);

                }

                else if(product.getProductName().equals("TestBook")){
                    Intent intent = new Intent(mContext, GoldRates.class);
                    intent.putExtra("currentState",  product.getProductName());
                    mContext.startActivity(intent);

                }

                else {


                    Intent intent = new Intent(mContext.getApplicationContext(), ProductDetailActivity.class);
                    intent.putExtra("productName", product.getProductName());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private FrameLayout productCardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = (ImageView)myView.findViewById(R.id.product_image);
            productName = (TextView)myView.findViewById(R.id.product_name);
            productCardview = (FrameLayout)myView.findViewById(R.id.product_cardview);

        }
    }
}
