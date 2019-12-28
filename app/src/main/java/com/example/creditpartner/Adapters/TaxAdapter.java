package com.example.creditpartner.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.example.creditpartner.Activities.AddCompanyActivity;
import com.example.creditpartner.Activities.AddUsersActivity;
import com.example.creditpartner.Activities.ApplyFormActivity;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.Interfaces.ItemLongClickListener;
import com.example.creditpartner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.ViewHolder> {


    private View myView;
    private Context mContext;
    private ArrayList<Companies> companiesArrayList;
    private String productName, key;
    private boolean isShown = false;
    private DatabaseReference Ref;


    public TaxAdapter(Context mContext, ArrayList<Companies> companiesArrayList) {
        this.mContext = mContext;
        this.companiesArrayList = companiesArrayList;
        Ref = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public TaxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tax_layout, parent, false);
        return new TaxAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaxAdapter.ViewHolder holder, int position) {

        final Companies companies = companiesArrayList.get(position);
        holder.companyName.setText(companies.getCompanyName());
        holder.companyValue1.setText(companies.getValue1());
        holder.companyValue2.setText(companies.getValue2());
        holder.companyField1.setText(companies.getField1());
        holder.companyField2.setText(companies.getField2());
        Glide.with(mContext)
                .load(companies.getCompanyImage())
                .into(holder.companyImage);
        holder.featuresText.setText(companies.getFeatures());


        SharedPreferences pref = mContext.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        productName = pref.getString("productTitle", null);

        holder.selectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), ApplyFormActivity.class);
                intent.putExtra("companyTitle", companies.getCompanyName());
                intent.putExtra("companyImage", companies.getCompanyImage());
                intent.putExtra("productTitle", productName);

                if (companies.getCompanyName().equals("IT Registration") || companies.getCompanyName().equals("GST Registration")) {
                    intent.putExtra("type", "Simple");

                } else {
                    if (companies.getCompanyName().equals("IT Returns")) {
                        intent.putExtra("type", "Pan");

                    } else {
                        intent.putExtra("type", "GST");

                    }

                }

                mContext.startActivity(intent);
            }
        });

        holder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongCLick(View v, int pos) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Edit");
                builder.setMessage("Do you want to edit this product?");

                builder.setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (companies.getCompanyName().equals("IT Registration")) {
                            key = "1";

                        }
                        else if (companies.getCompanyName().equals("IT Returns")) {
                            key = "2";

                        }
                        Intent intent = new Intent(mContext, AddCompanyActivity.class);
                        intent.putExtra("type", "taxedit");
                        intent.putExtra("key", key);
                        Log.e("key", "" + key);
                        mContext.startActivity(intent);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }


        });


        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isShown) {
                    holder.detailsCard.setVisibility(View.GONE);
                    holder.viewDetails.setText("View Details");
                    isShown = false;

                } else {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnLongClickListener {

        private ImageView companyImage;
        private TextView companyName, companyValue1, companyValue2, companyField1, companyField2;
        private Button selectCompany, viewDetails;
        private CardView detailsCard;
        private TextView featuresText;
        ItemLongClickListener itemLongClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            companyValue1 = (TextView) myView.findViewById(R.id.percent_rate);
            companyValue2 = (TextView) myView.findViewById(R.id.minimum_balance_money);
            companyField1 = (TextView) myView.findViewById(R.id.interest_rate);
            companyField2 = (TextView) myView.findViewById(R.id.minimum_balance);

            companyImage = (ImageView) myView.findViewById(R.id.tax_image);
            companyName = (TextView) myView.findViewById(R.id.company_name);
            selectCompany = (Button) myView.findViewById(R.id.company_select);
            viewDetails = (Button) myView.findViewById(R.id.company_view_details);
            detailsCard = (CardView) myView.findViewById(R.id.details_card);
            featuresText = (TextView) myView.findViewById(R.id.features_text);

            itemView.setOnLongClickListener(this);


        }


        public void setItemLongClickListener(ItemLongClickListener ic) {
            this.itemLongClickListener = ic;
        }

        @Override
        public boolean onLongClick(View v) {

            this.itemLongClickListener.onItemLongCLick(v, getLayoutPosition());

            return false;
        }
    }
}

