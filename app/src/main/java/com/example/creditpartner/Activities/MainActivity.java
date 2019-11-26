package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.creditpartner.Adapters.ProductAdapter;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.Classes.SliderAdapter;
import com.example.creditpartner.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Toolbar mToolbar;

    private int mCurrentPage;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    final int NUM_PAGES = 4;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView productRecyclerView;
    private ArrayList<Products> productsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();

        SetupViewPager();

        SetupRecyclerView();

        if (currentUser == null) {
            ChangeActivity(CustomerInfoActivity.class);
        }

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (mCurrentPage == NUM_PAGES - 1) {
                    mCurrentPage = 0;
                }
                mSlideViewPager.setCurrentItem(mCurrentPage++, true);
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }

    private void SetupRecyclerView() {

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        productRecyclerView.setLayoutManager(manager);

        Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    String productName = dataSnapshot1.child("Name").getValue().toString();
                    String productImage = dataSnapshot1.child("Image").getValue().toString();

                    productsArrayList.add(new Products(productName,productImage));

                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList);
                productRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void SetupViewPager() {

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);


    }

    private void Initialize() {

        SetupToolbar();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mSlideViewPager = (ViewPager)findViewById(R.id.main_viewpager);
        mDotsLayout = findViewById(R.id.dots_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        productRecyclerView = (RecyclerView)findViewById(R.id.product_recyclerview);

        Ref = FirebaseDatabase.getInstance().getReference();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, mToolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void ChangeActivity(Class Activity) {
        Intent intent = new Intent(MainActivity.this, Activity);
        startActivity(intent);
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Partner");

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    public void addDotsIndicator(int position) {

        mDots = new TextView[3];
        mDotsLayout.removeAllViews(); //without this multiple number of dots will be created

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;")); //code for the dot icon like thing
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));

            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorAccent)); //setting currently selected dot to white
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

            mCurrentPage = position;


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
