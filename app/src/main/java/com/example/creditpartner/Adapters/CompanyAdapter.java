package com.example.creditpartner.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.AddCompanyActivity;
import com.example.creditpartner.Activities.ApplyFormActivity;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.Interfaces.ItemLongClickListener;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> implements Filterable {


    private View myView;
    private Context mContext;
    private ArrayList<Companies> companiesArrayList;
    private ArrayList<Companies> companiesArrayListFull;
    private String productName;
    private boolean isShown = false;
    private DatabaseReference Ref;
    String productTitle;
    private FirebaseAuth mAuth;
    private String currentUserID, privilege, key;
    // private final OnStartDragListener mDragStartListener;


    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    public CompanyAdapter(Context mContext, ArrayList<Companies> companiesArrayList, String productTitle) {
        this.mContext = mContext;
        this.companiesArrayList = companiesArrayList;
        companiesArrayListFull = new ArrayList<>(companiesArrayList);
        this.productTitle = productTitle;
        Ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //   this.mDragStartListener = onStartDragListener;
        currentUserID = mAuth.getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_layout, parent, false);
        return new CompanyAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyAdapter.ViewHolder holder, int position) {

        final Companies companies = companiesArrayList.get(position);
        holder.companyName.setText(companies.getCompanyName());
        holder.companyValue1.setText(companies.getValue1());
        holder.companyValue2.setText(companies.getValue2());
        holder.companyField1.setText(companies.getField1());
        holder.companyField2.setText(companies.getField2());

        if (companies.getFeatures() != null) {
            holder.featuresText.loadDataWithBaseURL(null, companies.getFeatures(), "text/html", "utf-8", null);

        }
        // set the html content on a TextView


        Glide.with(mContext)
                .load(companies.getCompanyImage())
                .into(holder.companyImage);


        SharedPreferences pref = mContext.getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        productName = pref.getString("productTitle", null);

        holder.selectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), ApplyFormActivity.class);
                intent.putExtra("companyTitle", companies.getCompanyName());
                intent.putExtra("companyImage", companies.getCompanyImage());
                intent.putExtra("companyRate", companies.getValue1());
                intent.putExtra("productTitle", productName);
                intent.putExtra("type", "noType");
                mContext.startActivity(intent);
            }
        });


     /*   holder.dragButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }

                return false;
            }
        });*/


        Ref.child("CompanyList").child(productTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("companyName")) {
                        if (dataSnapshot1.child("companyName").getValue().toString().equals(companies.getCompanyName())
                                && dataSnapshot1.child("companyImage").getValue().toString().equals(companies.getCompanyImage())) {
                            key = dataSnapshot1.getKey();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                privilege = dataSnapshot.child("privilege").getValue().toString();

                if (privilege.equals("SuperAdmin")) {
                    holder.dragButton.setVisibility(View.VISIBLE);
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

                        Ref.child("CompanyList").child(productTitle).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    if (dataSnapshot1.child("companyName").getValue().toString().equals(companies.getCompanyName())
                                            && dataSnapshot1.child("value2").getValue().toString().equals(companies.getValue2())
                                            && dataSnapshot1.child("value1").getValue().toString().equals(companies.getValue1())) {
                                        key = dataSnapshot1.getKey();

                                        Ref.child("CompanyList").child(productName).child(key).removeValue();
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

                        Intent intent = new Intent(mContext, AddCompanyActivity.class);
                        intent.putExtra("type", "edit");
                        intent.putExtra("productTitle", productTitle);
                        intent.putExtra("key", "" + key);
                        mContext.startActivity(intent);

                    }
                });


                AlertDialog dialog = builder.create();
                if (privilege.equals("SuperAdmin")) {
                    dialog.show();
                }

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

    @Override
    public Filter getFilter() {

        return companyFIlter;


    }


    private Filter companyFIlter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Companies> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(companiesArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Companies companies : companiesArrayListFull) {
                    if (companies.getCompanyName().toLowerCase().contains(filterPattern)) {
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
            companiesArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnLongClickListener {

        private ImageButton dragButton;
        private ImageView companyImage;
        private TextView companyName, companyValue1, companyValue2, companyField1, companyField2;
        private Button selectCompany, viewDetails;
        private CardView detailsCard;
        private WebView featuresText;
        ItemLongClickListener itemLongClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            companyImage = (ImageView) myView.findViewById(R.id.company_image);
            companyName = (TextView) myView.findViewById(R.id.company_name);
            companyValue1 = (TextView) myView.findViewById(R.id.percent_rate);
            companyValue2 = (TextView) myView.findViewById(R.id.minimum_balance_money);
            companyField1 = (TextView) myView.findViewById(R.id.interest_rate);
            companyField2 = (TextView) myView.findViewById(R.id.minimum_balance);
            selectCompany = (Button) myView.findViewById(R.id.company_select);
            viewDetails = (Button) myView.findViewById(R.id.company_view_details);
            detailsCard = (CardView) myView.findViewById(R.id.details_card);
            featuresText = (WebView) myView.findViewById(R.id.features_text);
            dragButton = (ImageButton) myView.findViewById(R.id.drag_button);

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

