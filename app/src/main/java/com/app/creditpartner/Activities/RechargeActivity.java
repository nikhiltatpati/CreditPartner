package com.app.creditpartner.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.creditpartner.Adapters.RechargeAdapter;
import com.app.creditpartner.Fragment.BroadbandRechargeFragment;
import com.app.creditpartner.Fragment.DTHRechargeFragment;
import com.app.creditpartner.Fragment.DataCardRechargeFragment;
import com.app.creditpartner.Fragment.MobileRechargeFragment;
import com.app.creditpartner.R;
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