package com.app.creditpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.app.creditpartner.R;
import com.bumptech.glide.Glide;
import com.app.creditpartner.Activities.BannerActivity;
import com.app.creditpartner.Classes.Slides;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DatabaseReference Ref;
    private int totalValue;
    private String totalString;
    private ArrayList<Slides> slidesList = new ArrayList<>();
    ImageView slideImageView;
    TextView slideHeading;
    private LayoutInflater inflater;

    private View view;


    public SliderAdapter(Context context, ArrayList<Slides> slidesList) {
        this.context = context;
        this.slidesList = slidesList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);  //todo: RelativeLayout??
    }

    @Override
    public int getCount() {
        return slidesList.size();
    }






    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        Initialize();

        slideImageView = (ImageView) view.findViewById(R.id.iv_image_icon);
        slideHeading = (TextView) view.findViewById(R.id.tv_heading);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        Ref = FirebaseDatabase.getInstance().getReference();

        final Slides slides = slidesList.get(position);
        Glide.with(context)
                .load(slides.getAdImage())
                .into(slideImageView);

        slideHeading.setText(slides.getAdText());
        final String adLink = slides.getAdLink();
        slideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BannerActivity.class);
                intent.putExtra("adLink", adLink);
                Log.e("adLink",adLink);
                context.startActivity(intent);
            }
        });


        container.addView(view, 0);

        return view;

    }



    private void Initialize() {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }






}
