package com.example.creditpartner.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creditpartner.Activities.AddCompanyActivity;
import com.example.creditpartner.Activities.AddUsersActivity;
import com.example.creditpartner.Activities.ProductDetailActivity;
import com.example.creditpartner.Activities.UsersAccountActivity;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.Classes.Users;
import com.example.creditpartner.Interfaces.ItemLongClickListener;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SideUserAdapter extends RecyclerView.Adapter<SideUserAdapter.ViewHolder> implements Filterable {

    private View myView;
    private Context mContext;
    private ArrayList<Users> usersArrayList;
    private ArrayList<Users> usersArrayListFull;
    private DatabaseReference Ref;
    String productTitle;
    private FirebaseAuth mAuth;
    private String currentUserID, key;

    public SideUserAdapter(Context mContext, ArrayList<Users> usersArrayList) {
        this.mContext = mContext;
        this.usersArrayList = usersArrayList;
        usersArrayListFull = new ArrayList<>(usersArrayList);
        Ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public SideUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_user_layout,parent,false);
        return new SideUserAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SideUserAdapter.ViewHolder holder, int position) {

        final Users users = usersArrayList.get(position);
        holder.userName.setText(users.getUserName());
     /*   Glide.with(mContext)
                .load(product.getProductImage())
                .into(holder.productImage);*/
        holder.userEmail.setText(users.getUserEmail());
        holder.userNumber.setText(users.getUserNumber());


        holder.usersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext.getApplicationContext(), UsersAccountActivity.class);
                intent.putExtra("userNumber", users.getUserNumber());
                intent.putExtra("userName", users.getUserName());
                mContext.startActivity(intent);
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

                        Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    if (dataSnapshot1.child("name").getValue().toString().equals(users.getUserName())
                                            && dataSnapshot1.child("phoneNumber").getValue().toString().equals(users.getUserNumber())) {
                                        key = dataSnapshot1.getKey();

                                        Ref.child("Customers").child("BasicInfo").child(key).removeValue();
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
                        Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    if (dataSnapshot1.child("name").getValue().toString().equals(users.getUserName())
                                            && dataSnapshot1.child("phoneNumber").getValue().toString().equals(users.getUserNumber())) {
                                        key = dataSnapshot1.getKey();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent intent = new Intent(mContext, AddUsersActivity.class);
                        intent.putExtra("type","edit");
                        intent.putExtra("key",""+key);
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
        return usersArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Users> filteredList = new ArrayList<>();

            if(constraint== null || constraint.length() ==0)
            {
                filteredList.addAll(usersArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Users users : usersArrayListFull)
                {
                    if(users.getUserName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(users);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            usersArrayList.clear();
            usersArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnLongClickListener{

        private ImageView userImage;
        private TextView userName, userEmail, userNumber;
        private RelativeLayout usersLayout;
        ItemLongClickListener itemLongClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = (ImageView)myView.findViewById(R.id.user_icon);
            userName = (TextView)myView.findViewById(R.id.user_name);
            userEmail = (TextView)myView.findViewById(R.id.user_email);
            userNumber = (TextView)myView.findViewById(R.id.user_number);
            usersLayout = (RelativeLayout) myView.findViewById(R.id.user_layout);

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
