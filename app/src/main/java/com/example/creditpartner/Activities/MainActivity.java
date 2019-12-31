package com.example.creditpartner.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.creditpartner.Adapters.CategoryAdapter;
import com.example.creditpartner.Adapters.ProductAdapter;
import com.example.creditpartner.Adapters.SliderAdapter;
import com.example.creditpartner.Classes.MyApplication;
import com.example.creditpartner.Classes.Products;
import com.example.creditpartner.Classes.Slides;
import com.example.creditpartner.R;
import com.example.creditpartner.SendMailTask;
import com.example.creditpartner.Services.MailService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private String currentUserID, phoneNumber, superAdmin = "False", reference;
    private boolean isSuperAdmin = false;


    private ViewPager mSlideViewPager;

    private ProgressBar loadProducts;

    private SliderAdapter sliderAdapter;
    private String email;

    private BottomNavigationView bottomNavigationView;
    private Toolbar mToolbar;
    private int mCurrentPage;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

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

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();

        if (currentUser == null) {

            ChangeActivity(CustomerInfoActivity.class);
        } else {
            if (getIntent().getExtras() != null) {

                for (String key : getIntent().getExtras().keySet()) {
                    String value = getIntent().getExtras().getString(key);

                    if (key.equals("AnotherActivity") && value.equals("True")) {
                        Intent intent = new Intent(this, MyAccountActivity.class);
                        intent.putExtra("value", value);
                        startActivity(intent);
                        finish();
                    }

                }
            }

            subscribeToPushService();

        }
            Initialize();
            CheckSuperAdmin();


            FirebaseMessaging.getInstance().subscribeToTopic("offers")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                            }
                        }
                    });


            SetNavigationView();

            LinkReferences();
            GetSlides();


            SetupRecyclerView();

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();


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


    private void LinkReferences() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("reference"))
                {
                    reference = dataSnapshot.child("reference").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.hasChild("userID"))
                    {
                        if(dataSnapshot1.child("userID").getValue().toString().equals(reference))
                        {
                            Map map = new HashMap<>();
                            map.put("with", dataSnapshot1.getKey());
                            Ref.child("Customers").child("BasicInfo").child(currentUserID).updateChildren(map);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CheckSuperAdmin() {


        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("privilege")) {
                    //  addAdminButton.setVisibility(View.VISIBLE);
                    if (dataSnapshot.child("privilege").getValue().equals("SuperAdmin")) {
                        isSuperAdmin = true;
                        superAdmin = "True";
                        navigationView.getMenu().setGroupVisible(R.id.admin_menu, true);
                        navigationView.getMenu().findItem(R.id.notice).setVisible(true);
                    }

                }
                email = dataSnapshot.child("email").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("AndroidBash", "Subscribed");

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
    }


    private void GetSlides() {

        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slidesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("adText")) {
                        String image = dataSnapshot1.child("adImage").getValue().toString();
                        String link = dataSnapshot1.child("adLink").getValue().toString();
                        String text = dataSnapshot1.child("adText").getValue().toString();
                        slidesList.add(new Slides(image, text, link));


                    } else {
                        String image = dataSnapshot1.child("adImage").getValue().toString();
                        String link = dataSnapshot1.child("adLink").getValue().toString();

                        slidesList.add(new Slides(image, "", link));

                    }
                }

                sliderAdapter = new SliderAdapter(MainActivity.this, slidesList);

                mSlideViewPager.setAdapter(sliderAdapter);

                mSlideViewPager.addOnPageChangeListener(viewListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void SetupRecyclerView() {

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        productRecyclerView.setLayoutManager(manager);

        /*Query query = Ref.child("ProductList").orderByChild("order");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {
                        String productName = dataSnapshot1.child("Name").getValue().toString();
                        String productImage = dataSnapshot1.child("Image").getValue().toString();
                        productsArrayList.add(new Products(productName, productImage));


                    }


                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList);
                productRecyclerView.setAdapter(adapter);
                loadProducts.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        */
        Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {
                        String productName = dataSnapshot1.child("Name").getValue().toString();
                        String productImage = dataSnapshot1.child("Image").getValue().toString();
                        productsArrayList.add(new Products(productName, productImage));


                    }


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
                    TextView numText = (TextView) header.findViewById(R.id.sidenav_header_number);
                    numText.setText(number);
                }

                if (dataSnapshot.child("privilege").exists()) {
                    String privilege = dataSnapshot.child("privilege").getValue().toString();
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

        Menu menu = navigationView.getMenu();


        switch (menuItem.getItemId()) {

            case R.id.side_products: {


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(intent);
                break;


            }

            case R.id.side_ads: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, AdsActivity.class);

                startActivity(intent);
                break;
            }

            case R.id.side_users: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.side_home: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.side_myapps: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, MyApplications.class);

                startActivity(intent);
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

            case R.id.side_taxes: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, TaxInfoActivity.class);
                startActivity(intent);
                break;
            }


            case R.id.side_loan: {

                boolean b = !menu.findItem(R.id.side_businessloan).isVisible();

                menu.findItem(R.id.side_businessloan).setVisible(b);
                menu.findItem(R.id.side_carloan).setVisible(b);
                menu.findItem(R.id.side_educationloan).setVisible(b);
                menu.findItem(R.id.side_personalloan).setVisible(b);
                menu.findItem(R.id.side_instantloan).setVisible(b);

                menu.findItem(R.id.side_homeloan).setVisible(b);
                break;

            }

            case R.id.side_insurance: {
                boolean b = !menu.findItem(R.id.side_carinsurance).isVisible();
                menu.findItem(R.id.side_carinsurance).setVisible(b);
                menu.findItem(R.id.side_healthinsurance).setVisible(b);
                menu.findItem(R.id.side_terminsurance).setVisible(b);
                break;

            }

            case R.id.Tools: {
                boolean b = !menu.findItem(R.id.side_emi_cal).isVisible();
                menu.findItem(R.id.side_emi_cal).setVisible(b);
                menu.findItem(R.id.side_ifsc).setVisible(b);
                menu.findItem(R.id.side_income).setVisible(b);

                break;
            }

            case R.id.side_emi_cal: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, EMICalculatorActivity.class));

                break;
            }


            case R.id.side_ifsc: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, IFSCCodeFinderActivity.class));

                break;
            }

            case R.id.side_income: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, IncomeTaxCalculatorActivity.class));

                break;
            }


            case R.id.side_logout: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

            }
            case R.id.side_myaccount: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                ChangeActivity(MyAccountActivity.class);
                break;

            }

            case R.id.side_myoffer: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, MyOfferActivity.class));
                break;

            }

            case R.id.notice: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                break;

            }
       /*     case R.id.side_refer_and_earn: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                ChangeActivity(ReferAndEarnActivity.class);
                break;

            }*/

            case R.id.side_credit_report: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, CreditReportActivity.class));
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

            case R.id.side_saving: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Saving Account");
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

    /* public void addDotsIndicator(int position) {

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
 */
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //       addDotsIndicator(position);

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
                case R.id.bot_paisa_tracker: {
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


        currentUserID = currentUser.getUid();


        loadProducts = findViewById(R.id.load_products);
        mSlideViewPager = findViewById(R.id.main_viewpager);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        productRecyclerView = findViewById(R.id.product_recyclerview);


        drawerLayout = findViewById(R.id.drawer_layout);
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, mToolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
