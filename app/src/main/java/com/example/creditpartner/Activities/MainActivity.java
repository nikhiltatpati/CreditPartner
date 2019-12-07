package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.creditpartner.Adapters.ProductAdapter;
import com.example.creditpartner.Adapters.SliderAdapter;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.Classes.Slides;
import com.example.creditpartner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private String currentUserID, phoneNumber;
    private boolean isSuperAdmin = false;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private TextView[] mDots;
    private ProgressBar loadProducts;

    private SliderAdapter sliderAdapter;

    private BottomNavigationView bottomNavigationView;
    private Toolbar mToolbar;
    private int mCurrentPage;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int currentPage = 0;

    private ImageButton addAdminButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView productRecyclerView;
    private ArrayList<Products> productsArrayList = new ArrayList<>();
    private ArrayList<Slides> slidesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();

        CheckSuperAdmin();

        GetSlides();


        SetupRecyclerView();

        if(currentUser != null)
        {
            SetNavigationView();

        }

        addAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAdminsActivity.class);
                intent.putExtra("isSuperAdmin", isSuperAdmin ? "True":"False");
                startActivity(intent);
            }
        });



//        After setting the adapter use the timer
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (mCurrentPage == slidesList.size()) {
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

    private void GetSlides() {

        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slidesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.hasChild("AdImage") && dataSnapshot1.hasChild("AdLink"))

                    {
                        String image = dataSnapshot1.child("AdImage").getValue().toString();
                        String link = dataSnapshot1.child("AdLink").getValue().toString();
                        slidesList.add(new Slides(image, "", link));


                    }
                    else if(dataSnapshot1.hasChild("AdImage") && dataSnapshot1.hasChild("AdLink") && dataSnapshot1.hasChild("Adtext"))
                    {

                        String image = dataSnapshot1.child("AdImage").getValue().toString();
                        String link = dataSnapshot1.child("AdLink").getValue().toString();
                        String text = dataSnapshot1.child("AdText").getValue().toString();
                        slidesList.add(new Slides(image, "", link));

                    }}

                sliderAdapter = new SliderAdapter(MainActivity.this, slidesList);

                mSlideViewPager.setAdapter(sliderAdapter);

                mSlideViewPager.addOnPageChangeListener(viewListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void CheckSuperAdmin() {
        if(currentUser != null) {


            Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("phoneNumber")) {
                        phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Ref.child("Privileges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(phoneNumber)) {
                    if (dataSnapshot.child(phoneNumber).getValue().toString().equals("SuperAdmin")){
                        addAdminButton.setVisibility(View.VISIBLE);
                        isSuperAdmin = true;
                    }
                    else if(dataSnapshot.child(phoneNumber).getValue().toString().equals("Admin"))
                    {
                        addAdminButton.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupRecyclerView() {

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        productRecyclerView.setLayoutManager(manager);

        Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String productName = dataSnapshot1.child("Name").getValue().toString();
                    String productImage = dataSnapshot1.child("Image").getValue().toString();

                    productsArrayList.add(new Products(productName, productImage));

                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList);
                productRecyclerView.setAdapter(adapter);
                loadProducts.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    //SET DATA IN NAVHEADER
    private void SetNavigationView() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View header = navigationView.getHeaderView(0);

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("name").exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    TextView nameText = (TextView) header.findViewById(R.id.sidenav_header_name);
                    nameText.setText(name);
                }
                if (dataSnapshot.child("phoneNumber").exists()) {
                    String number = dataSnapshot.child("phoneNumber").getValue().toString();
                    TextView nameText = (TextView) header.findViewById(R.id.sidenav_header_number);
                    nameText.setText(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Ref.child("Privileges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                   String privilege =  dataSnapshot.child(phoneNumber).getValue().toString();
                   TextView privilegeText = (TextView) header.findViewById(R.id.sidenav_header_privilege);
                   privilegeText.setText(privilege);
                }

                else {
                    String privilege =  "User";
                    TextView privilegeText = (TextView) header.findViewById(R.id.sidenav_header_privilege);
                    privilegeText.setText(privilege);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void ChangeActivity(Class Activity) {
        Intent intent = new Intent(MainActivity.this, Activity);
        startActivity(intent);
    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Partner");

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.side_home: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.side_credit_card: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Credit Card");
                startActivity(intent);
                break;
            }



            case R.id.side_logout: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, CustomerInfoActivity.class));
                finish();
                break;

            }
            case R.id.side_myaccount: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                ChangeActivity(MyAccountActivity.class);
                break;

            }
            case R.id.side_refer_and_earn: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                ChangeActivity(ReferAndEarnActivity.class);
                break;

            }

            case R.id.side_credit_report: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Snackbar.make(drawerLayout, "Coming Soon, hang on!", 3000).show();
                break;

            }
            case R.id.side_businessloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Business Loan");
                startActivity(intent);
                break;

            }

            case R.id.side_carloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Car Loan");
                startActivity(intent);
                break;

            }
            case R.id.side_educationloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Education Loan");
                startActivity(intent);
                break;

            }
            case R.id.side_homeloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Home Loan");
                startActivity(intent);
                break;

            }
            case R.id.side_instantloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Instant Loan");
                startActivity(intent);
                break;

            }
            case R.id.side_personalloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Personal Loan");
                startActivity(intent);
                break;

            }
            case R.id.side_terminsurance: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Term Insurance");
                startActivity(intent);
                break;

            }
            case R.id.side_carinsurance: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Car Insurance");
                startActivity(intent);
                break;

            }
            case R.id.side_healthinsurance: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Health Insurance");
                startActivity(intent);
                break;

            }

        }
        return true;
    }

    public void addDotsIndicator(int position) {

        mDots = new TextView[slidesList.size()];
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

    //handle bottomnavigation buttons
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.bot_account: {
                    ChangeActivity(MyAccountActivity.class);
                    break;
                }

                case R.id.bot_recharge: {
                    Snackbar.make(drawerLayout, "Coming Soon, hang on!", 3000).show();
                    break;
                }
                case R.id.bot_paisa_tracker:
                {
                    ChangeActivity(PaisaTrackerActivity.class);
                    break;
                }

                case R.id.bot_home: {
                    break;

                }


            }
            return true;

        }


    };

    private void Initialize() {

        SetupToolbar();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            ChangeActivity(CustomerInfoActivity.class);
        } else {
            currentUserID = currentUser.getUid();

        }


        loadProducts = findViewById(R.id.load_products);
        mSlideViewPager = findViewById(R.id.main_viewpager);
        mDotsLayout = findViewById(R.id.dots_layout);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        productRecyclerView = findViewById(R.id.product_recyclerview);

        addAdminButton = (ImageButton) findViewById(R.id.add_admin_button);
        addAdminButton.setVisibility(View.GONE);

        Ref = FirebaseDatabase.getInstance().getReference();

        drawerLayout = findViewById(R.id.drawer_layout);
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, mToolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

}
