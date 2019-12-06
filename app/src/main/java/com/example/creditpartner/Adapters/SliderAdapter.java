package com.example.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.creditpartner.Activities.PaisaTrackerActivity;
import com.example.creditpartner.Activities.ProductDetailActivity;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DatabaseReference Ref;
    private int totalValue;
    private String totalString;




    public SliderAdapter(Context context) {
        this.context = context;


    }

    public int[] slideImages = {
            R.drawable.creditscore,
            R.drawable.mf,
            R.drawable.rupee
    };

    public String[] slideHeadings =  new String[3];

    public String[] slideDescriptions = {
            "Free monthly updates" ,
            "No Commission, no charges",
            "Total Spendings"
    };

    public String[] slideLinksString = {
            "Coming Soon!" ,
            "View Details",
            "View Details"
    };

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        Initialize();

        Ref.child("Transactions").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalValue = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    int value = Integer.parseInt(dataSnapshot1.child("expenseValue").getValue().toString());
                    totalValue += value;

                }
                totalString = String.valueOf(totalValue);
                slideHeadings[0] = "Get your CIBIL Report Absolutely Free";
                slideHeadings[1] = "Mutual Funds";
                slideHeadings[2] = "₹ " + totalString;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView slideImageView = (ImageView) view.findViewById(R.id.iv_image_icon);
        TextView slideHeading = (TextView) view.findViewById(R.id.tv_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.tv_description);
        TextView slideLinks = (TextView)view.findViewById(R.id.tv_links);

        slideImageView.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeadings[position]);
        slideDescription.setText(slideDescriptions[position]);
        slideLinks.setText(slideLinksString[position]);

        slideLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 1)
                {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productName","Mutual Funds" );
                    context.startActivity(intent);
                }

                if(position == 2)
                {
                    Intent intent = new Intent(context, PaisaTrackerActivity.class);
                    context.startActivity(intent);
                }

            }
        });


        container.addView(view);

        return view;

    }



    private void Initialize() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();
    }







    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);  //todo: RelativeLayout??
    }
}
