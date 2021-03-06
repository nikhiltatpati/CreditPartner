package com.app.creditpartner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.creditpartner.Models.ItemBannerModelTop;
import com.app.creditpartner.R;

import java.util.List;

public class ItemBannerAdapterTop extends PagerAdapter {

    private List<ItemBannerModelTop> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private String label;

    public ItemBannerAdapterTop(List<ItemBannerModelTop> models, Context context) {
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
