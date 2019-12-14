package com.example.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.creditpartner.Activities.AdDetailsActiviy;
import com.example.creditpartner.Activities.CompanyWebsiteActivity;
import com.example.creditpartner.Classes.Ads;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.Classes.Users;
import com.example.creditpartner.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder> implements Filterable {


    private View myView;
    private Context mContext;
    private ArrayList<Ads> AdsArrayList;
    private ArrayList<Ads> AdsArrayListFull;



    public AdAdapter(Context mContext, ArrayList<Ads> AdsArrayList) {
        this.mContext = mContext;
        this.AdsArrayList = AdsArrayList;
        AdsArrayListFull = new ArrayList<>(AdsArrayList);
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView adImage;
        private TextView customerName, adClicks, adType;
        private CardView adDetails;
        private TextView featuresText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            adImage = (CircleImageView) myView.findViewById(R.id.ad_image);
            customerName = (TextView)myView.findViewById(R.id.ad_customer_name);
            adClicks = (TextView)myView.findViewById(R.id.ad_clicks);

            adDetails = (CardView)myView.findViewById(R.id.ad_details);
            adType = (TextView)myView.findViewById(R.id.ad_type);

        }
    }
}


