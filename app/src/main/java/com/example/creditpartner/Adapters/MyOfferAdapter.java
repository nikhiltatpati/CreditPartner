package com.example.creditpartner.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creditpartner.Activities.AdDetailsActiviy;
import com.example.creditpartner.Activities.AddAdsActivity;
import com.example.creditpartner.Classes.Ads;
import com.example.creditpartner.Classes.Notifications;
import com.example.creditpartner.Interfaces.ItemLongClickListener;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOfferAdapter extends RecyclerView.Adapter<MyOfferAdapter.ViewHolder> {


    private View myView;
    private Context mContext;
    private ArrayList<Notifications> notifications;
    private DatabaseReference Ref;
    private FirebaseAuth mAuth;
    private String currentUserID, key;


    public MyOfferAdapter(Context mContext, ArrayList<Notifications> notifications) {
        this.mContext = mContext;
        this.notifications = notifications;
        Ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_offer_layout,parent,false);
        return new MyOfferAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOfferAdapter.ViewHolder holder, int position) {

        final Notifications notificationss = notifications.get(position);
        holder.notiTitle.setText(notificationss.getNotificationTitle());
        holder.notiText.setText(notificationss.getNotificationText());
        Glide.with(mContext)
                .load(R.drawable.ic_notifications_black_24dp)
                .into(holder.notiImage);




     /*   holder.setItemLongClickListener(new ItemLongClickListener() {
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




      */
    }
    @Override
     public int getItemCount() {
        return notifications.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnLongClickListener {

        private CircleImageView notiImage;
        private TextView notiTitle, notiText;
        private ItemLongClickListener itemLongClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notiImage = (CircleImageView)myView.findViewById(R.id.noti_image);


            notiTitle = (TextView)myView.findViewById(R.id.noti_title);
            notiText = (TextView)myView.findViewById(R.id.noti_text);

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


