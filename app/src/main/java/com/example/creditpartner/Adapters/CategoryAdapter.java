package com.example.creditpartner.Adapters;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.AddCompanyActivity;
import com.example.creditpartner.Activities.AddExpenseActivity;
import com.example.creditpartner.Activities.ProductDetailActivity;
import com.example.creditpartner.Activities.TaxesActivity;
import com.example.creditpartner.Activities.TextEditorActivity;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private View myView;
    private Context mContext;
    private ArrayList<Products> products;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public CategoryAdapter(Context mContext, ArrayList<Products> products) {
        this.mContext = mContext;
        this.products = products;

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
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


                Intent intent = new Intent(mContext, AddExpenseActivity.class);
                intent.putExtra("category", product.getProductName());
                ((Activity) mContext).setResult(1, intent);
                ((Activity) mContext).finish();

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

            productImage = (ImageView) myView.findViewById(R.id.categoryy_image);
            productName = (TextView) myView.findViewById(R.id.categoryy_name);
            productCardview = (FrameLayout) myView.findViewById(R.id.category_cardview);

        }
    }
}

