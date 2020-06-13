package com.app.creditpartner.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.creditpartner.Activities.AddAdsActivity;
import com.app.creditpartner.R;
import com.bumptech.glide.Glide;
import com.app.creditpartner.Activities.AdDetailsActiviy;
import com.app.creditpartner.Classes.Ads;
import com.app.creditpartner.Interfaces.ItemLongClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder> implements Filterable {


    private View myView;
    private Context mContext;
    private ArrayList<Ads> AdsArrayList;
    private ArrayList<Ads> AdsArrayListFull;
    private DatabaseReference Ref;
    String productTitle;
    private FirebaseAuth mAuth;
    private String currentUserID, key;


    public AdAdapter(Context mContext, ArrayList<Ads> AdsArrayList) {
        this.mContext = mContext;
        this.AdsArrayList = AdsArrayList;
        AdsArrayListFull = new ArrayList<>(AdsArrayList);
        Ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public AdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_ad_layout,parent,false);
        return new AdAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdAdapter.ViewHolder holder, int position) {

        final Ads ads = AdsArrayList.get(position);
        holder.customerName.setText(ads.getCustomerName());
        holder.adClicks.setText(ads.getNumberOfClicks());
        holder.adType.setText(ads.getAdType());
        Glide.with(mContext)
                .load(ads.getAdImage())
                .into(holder.adImage);


        holder.adDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AdDetailsActiviy.class);
                intent.putExtra("customerName", ads.getCustomerName());
                intent.putExtra("adImage", ads.getAdImage());
                mContext.startActivity(intent);

            }
        });

        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("adImage").getValue().toString().equals(ads.getAdImage())
                            && dataSnapshot1.child("noOfClicks").getValue().toString().equals(ads.getNumberOfClicks())) {

                        key = dataSnapshot1.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongCLick(View v, int pos) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Select");
                builder.setMessage("Do you want to edit or delete this product?");
                builder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    if (dataSnapshot1.child("adImage").getValue().toString().equals(ads.getAdImage())
                                            && dataSnapshot1.child("noOfClicks").getValue().toString().equals(ads.getNumberOfClicks())) {
                                        key = dataSnapshot1.getKey();

                                        Ref.child("Banners").child(key).removeValue();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(mContext, AddAdsActivity.class);
                        intent.putExtra("type","edit");
                        intent.putExtra("key",key);
                        mContext.startActivity(intent);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }


        });


    }

    @Override
    public int getItemCount() {
        return AdsArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return adsFilter;
    }

    private Filter adsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Ads> filteredList = new ArrayList<>();

            if(constraint== null || constraint.length() ==0)
            {
                filteredList.addAll(AdsArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Ads ads : AdsArrayListFull)
                {
                    if(ads.getCustomerName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(ads);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            AdsArrayList.clear();
            AdsArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnLongClickListener {

        private CircleImageView adImage;
        private TextView customerName, adClicks, adType;
        private CardView adDetails;
        private TextView featuresText;
        private ItemLongClickListener itemLongClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            adImage = (CircleImageView) myView.findViewById(R.id.ad_image);
            customerName = (TextView)myView.findViewById(R.id.ad_customer_name);
            adClicks = (TextView)myView.findViewById(R.id.ad_clicks);

            adDetails = (CardView)myView.findViewById(R.id.ad_details);
            adType = (TextView)myView.findViewById(R.id.ad_type);

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


