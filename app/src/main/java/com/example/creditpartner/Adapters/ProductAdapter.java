package com.example.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.MainActivity;
import com.example.creditpartner.Activities.ProductDetailActivity;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private View myView;
    private Context mContext;
    private ArrayList<Products> products;

    public ProductAdapter(Context mContext, ArrayList<Products> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Products product = products.get(position);
        holder.productName.setText(product.getProductName());
        Glide.with(mContext)
                .load(product.getProductImage())
                .into(holder.productImage);

        holder.productCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("productName", product.getProductName());
                mContext.startActivity(intent);
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
        private CardView productCardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = (ImageView)myView.findViewById(R.id.product_image);
            productName = (TextView)myView.findViewById(R.id.product_name);
            productCardview = (CardView)myView.findViewById(R.id.product_cardview);

        }
    }
}
