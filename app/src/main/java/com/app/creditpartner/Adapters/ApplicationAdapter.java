package com.app.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.creditpartner.Classes.Applications;
import com.app.creditpartner.R;
import com.bumptech.glide.Glide;
import com.app.creditpartner.Activities.ApplicationDetailsActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> implements Filterable {

    private View myView;
    private Context mContext;
    private ArrayList<Applications> applicationsArrayList;
    private ArrayList<Applications> applicationsArrayListFull;

    public ApplicationAdapter(Context mContext, ArrayList<Applications> applicationsArrayList) {
        this.mContext = mContext;
        this.applicationsArrayList = applicationsArrayList;
        applicationsArrayListFull = new ArrayList<>(applicationsArrayList);
    }

    @NonNull
    @Override
    public ApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_layout,parent,false);
        return new ApplicationAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ViewHolder holder, int position) {

        final Applications apps = applicationsArrayList.get(position);
        holder.appName.setText(apps.getAppName());
        holder.appDate.setText(apps.getAppDate());
        Glide.with(mContext)
                .load(apps.getAppImage())
                .into(holder.appImage);

        holder.appLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ApplicationDetailsActivity.class);
                intent.putExtra("date", apps.getAppDate());
                intent.putExtra("name", apps.getAppName());
                intent.putExtra("image", apps.getAppImage());
                mContext.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return applicationsArrayList.size();
    }

    @Override
    public Filter getFilter() {

        return categoryFilter;


    }


    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Applications> filteredList = new ArrayList<>();

            if(constraint== null || constraint.length() ==0)
            {
                filteredList.addAll(applicationsArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Applications applications : applicationsArrayListFull)
                {
                    if(applications.getAppName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(applications);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            applicationsArrayList.clear();
            applicationsArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };




    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView appImage;
        private TextView appName, appDate;
        private RelativeLayout appLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appImage = (CircleImageView) myView.findViewById(R.id.app_image);
            appName = (TextView)myView.findViewById(R.id.app_name);
            appDate = (TextView)myView.findViewById(R.id.app_date);
            appLayout = (RelativeLayout) myView.findViewById(R.id.app_layout);

        }
    }
}
