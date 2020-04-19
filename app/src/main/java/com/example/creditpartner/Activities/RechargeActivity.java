package com.example.creditpartner.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.creditpartner.Adapters.RechargeAdapter;
import com.example.creditpartner.Fragment.BroadbandRechargeFragment;
import com.example.creditpartner.Fragment.DTHRechargeFragment;
import com.example.creditpartner.Fragment.DataCardRechargeFragment;
import com.example.creditpartner.Fragment.MobileRechargeFragment;
import com.example.creditpartner.R;
import com.google.android.material.tabs.TabLayout;

public class RechargeActivity extends AppCompatActivity {
    private RechargeAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_add,
            R.drawable.ic_add,
            R.drawable.ic_add,
            R.drawable.ic_add
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new RechargeAdapter(getSupportFragmentManager());
        adapter.addFragment(new MobileRechargeFragment(), "Tab 1");
        adapter.addFragment(new DTHRechargeFragment(), "Tab 2");
        adapter.addFragment(new DataCardRechargeFragment(), "Tab 3");
        adapter.addFragment(new BroadbandRechargeFragment(), "Tab 4");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}