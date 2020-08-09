package com.app.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.creditpartner.Activities.TaxWebsiteActivity;
import com.app.creditpartner.Activities.CreditReportActivity;
import com.app.creditpartner.Activities.ProductDetailActivity;
import com.app.creditpartner.Models.ItemBannerBottom;
import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ItemBannerAdapterBottom extends PagerAdapter {

    private List<ItemBannerBottom> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private String label,currentUSerID,murl;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;

    public ItemBannerAdapterBottom(List<ItemBannerBottom> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.banner_item_top, container, false);

        ImageView imageView;

        imageView = view.findViewById(R.id.image);
        imageView.setImageResource(models.get(position).getImage());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position ==2){

                    mAuth = FirebaseAuth.getInstance();
                    Ref = FirebaseDatabase.getInstance().getReference();
                    currentUSerID = mAuth.getCurrentUser().getUid();


                    Ref.child("Customers").child("BasicInfo").child(currentUSerID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();



                            String ref = dataSnapshot.child("reference").getValue().toString();


                            if(ref.isEmpty()){
                                Intent intent = new Intent(context, TaxWebsiteActivity.class);
                                murl="https://primeindia.o18.link/c?o=4222229&m=2606&a=68599&sub_aff_id="+phoneNumber;
                                Log.i("TAG",murl);
                                intent.putExtra("url", murl);
                                context.startActivity(intent);
                            }else{
                                murl="https://primeindia.o18.link/c?o=4222229&m=2606&a="+ref+"&sub_aff_id="+phoneNumber;
                                Log.i("TAG",murl);
                                Intent intent = new Intent(context, TaxWebsiteActivity.class);
                                intent.putExtra("url", murl);
                                context.startActivity(intent);
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else if(position ==5){
                    Intent intent = new Intent(context.getApplicationContext(), CreditReportActivity.class);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailActivity.class);
                    intent.putExtra("productName","Play Games");
                    context.startActivity(intent);
                }

            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
