package com.app.creditpartner.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.creditpartner.Adapters.ItemBannerAdapter;
import com.app.creditpartner.Adapters.ItemBannerAdapterBottom;
import com.app.creditpartner.Adapters.ItemBannerAdapterTop;
import com.app.creditpartner.Adapters.ProductAdapter;
import com.app.creditpartner.Adapters.SliderAdapter;
import com.app.creditpartner.Classes.Products;
import com.app.creditpartner.Classes.Slides;
import com.app.creditpartner.Models.ItemBannerBottom;
import com.app.creditpartner.Models.ItemBannerModel;
import com.app.creditpartner.Models.ItemBannerModelTop;
import com.app.creditpartner.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private String currentUserID, phoneNumber, superAdmin = "False", reference;
    private Button viewGoldRates, viewFuelRates, viewEMI, viewIFSC,viewCreditScore;
    private boolean isSuperAdmin;

    private ViewPager mSlideViewPager, mSlideViewPager1, mSlideViewPager2;

    private ProgressBar loadProducts;

    private SliderAdapter sliderAdapter;
    private String email;
    private String label;

    ItemBannerAdapter adapter;
    ItemBannerAdapterTop adapter1;
    ItemBannerAdapterBottom adapter2;
    List<ItemBannerModel> models;
    List<ItemBannerModelTop> models1;
    List<ItemBannerBottom> models2;


    private BottomNavigationView bottomNavigationView;
    private Toolbar mToolbar;
    private int mCurrentPage;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    private ImageButton addAdminButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView productRecyclerView1,productRecyclerView2,productRecyclerView3,productRecyclerView4,productRecyclerView5;
    private ArrayList<Products> productsArrayList1 = new ArrayList<>();
    private ArrayList<Products> productsArrayList2 = new ArrayList<>();
    private ArrayList<Products> productsArrayList3 = new ArrayList<>();
    private ArrayList<Products> productsArrayList4 = new ArrayList<>();
    private ArrayList<Products> productsArrayList5 = new ArrayList<>();

    private ArrayList<Slides> slidesList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
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

//            subscribeToPushService();


            Initialize();


            FirebaseMessaging.getInstance().subscribeToTopic("offers")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                            }
                        }
                    });


            SetNavigationView();

            Thread reference = new Thread(new Runnable() {
                @Override
                public void run() {
                    LinkReferences();
                   // GetSlides();
                    //GetSlides2();

                }
            });

            Thread superadminChecker = new Thread(new Runnable() {
                @Override
                public void run() {
                    CheckSuperAdmin();
                }
            });

            reference.start();
            superadminChecker.start();


            SetupRecyclerView();


//        After setting the adapter use the timer
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                }
            };

            Timer timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                }
            }, DELAY_MS, PERIOD_MS);

        }



        models1 = new ArrayList<>();
        models1.add(new ItemBannerModelTop(R.drawable.game12));
        models1.add(new ItemBannerModelTop(R.drawable.creditscore));
        models1.add(new ItemBannerModelTop(R.drawable.game3));
        models1.add(new ItemBannerModelTop(R.drawable.game4));
        models1.add(new ItemBannerModelTop(R.drawable.game5));

        adapter1 = new ItemBannerAdapterTop(models1,this);

        mSlideViewPager2 = findViewById(R.id.main_viewpager);
        mSlideViewPager2.setAdapter(adapter1);




        models2 = new ArrayList<>();
        models2.add(new ItemBannerBottom(R.drawable.bodog));
        models2.add(new ItemBannerBottom(R.drawable.casumo));
        models2.add(new ItemBannerBottom(R.drawable.norton2));
        models2.add(new ItemBannerBottom(R.drawable.cashoola));
        models2.add(new ItemBannerBottom(R.drawable.bet));
        models2.add(new ItemBannerBottom(R.drawable.game12));

        adapter2 = new ItemBannerAdapterBottom(models2,this);

      //  mSlideViewPager2 = findViewById(R.id.main_viewpager2);
        mSlideViewPager2.setAdapter(adapter2);



        models = new ArrayList<>();
        models.add(new ItemBannerModel(R.drawable.report, "report", "Check your Credit Score for FREE Get better deals on Credit Cards and Loans"));
//        models.add(new ItemBannerModel(R.drawable.fuel, "fuel", "To View Petrol and Diesel Rate tap here to give access to your location"));
//        models.add(new ItemBannerModel(R.drawable.gold, "gold", "To View Gold and Silver Rate tap here to give access to your location"));
        models.add(new ItemBannerModel(R.drawable.find, "find", "Find All IFSC Codes"));
        models.add(new ItemBannerModel(R.drawable.calculator, "calculator", "Calculate Your EMIs in a Jiffy"));

        adapter = new ItemBannerAdapter(models,this);

        mSlideViewPager1 = findViewById(R.id.main_viewpager1);
        mSlideViewPager1.setAdapter(adapter);

//
//        viewGoldRates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialogbox, null);
//                mBuilder.setTitle("Select State");
//                Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.state));
//                adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
//                mSpinner.setAdapter(adapter);
//
//                mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view,
//                                               int position, long id) {
//                        label = parent.getItemAtPosition(position).toString();
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        Toast.makeText(adapterView.getContext(), "Please select something",Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(MainActivity.this, GoldRates.class);
//                        intent.putExtra("currentState", label);
//                        startActivity(intent);
//
//                    }
//
//                });
//                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.show();
//            }
//        });

//        viewFuelRates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialogbox, null);
//                mBuilder.setTitle("Select State");
//                Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.state));
//                adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
//                mSpinner.setAdapter(adapter);
//                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(MainActivity.this, FuelRates.class);
//                        intent.putExtra("currentState",mSpinner.getSelectedItem().toString());
//                        startActivity(intent);
//
//                    }
//
//                });
//                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.show();
//            }
//        });


//        viewIFSC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, IFSCCodeFinderActivity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });

//        viewEMI.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, EMICalculatorActivity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });

//        viewCreditScore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, CreditReportActivity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//
//


   /*     final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (mCurrentPage == slidesList.size()) {
                    mCurrentPage = 0;
                }
                mSlideViewPager2.setCurrentItem(mCurrentPage++, true);
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
*/
    }


    private void LinkReferences() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("reference")) {
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

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("userID")) {
                        if (dataSnapshot1.child("userID").getValue().toString().equals(reference)) {
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
                        //navigationView.getMenu().setGroupVisible(R.id.admin_menu, true);
                       // navigationView.getMenu().findItem(R.id.notice).setVisible(true);
                    }

                }
                if (dataSnapshot.hasChild("email")) {
                    email = dataSnapshot.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


//    private void GetSlides() {
//
//        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                slidesList.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    if (dataSnapshot1.hasChild("adText")) {
//                        String image = dataSnapshot1.child("adImage").getValue().toString();
//                        String link = dataSnapshot1.child("adLink").getValue().toString();
//                        String text = dataSnapshot1.child("adText").getValue().toString();
//                        slidesList.add(new Slides(image, text, link));
//
//
//                    } else {
//                        String image = dataSnapshot1.child("adImage").getValue().toString();
//                        String link = dataSnapshot1.child("adLink").getValue().toString();
//
//                        slidesList.add(new Slides(image, "", link));
//
//                    }
//                }
//
//                sliderAdapter = new SliderAdapter(MainActivity.this, slidesList);
//
//                mSlideViewPager.setAdapter(sliderAdapter);
//
//                mSlideViewPager.addOnPageChangeListener(viewListener);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
//    private void GetSlides2() {
//
//        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                slidesList.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    if (dataSnapshot1.hasChild("adText")) {
//                        String image = dataSnapshot1.child("adImage").getValue().toString();
//                        String link = dataSnapshot1.child("adLink").getValue().toString();
//                        String text = dataSnapshot1.child("adText").getValue().toString();
//                        slidesList.add(new Slides(image, text, link));
//
//
//
//                    } else {
//                        String image = dataSnapshot1.child("adImage").getValue().toString();
//                        String link = dataSnapshot1.child("adLink").getValue().toString();
//
//                        slidesList.add(new Slides(image, "", link));
//
//                    }
//                }
//
//                sliderAdapter = new SliderAdapter(MainActivity.this, slidesList);
//
////                mSlideViewPager2.setAdapter(sliderAdapter);
////                mSlideViewPager3.setAdapter(sliderAdapter);
////                mSlideViewPager2.addOnPageChangeListener(viewListener);
////                mSlideViewPager3.addOnPageChangeListener(viewListener);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    private void SetupRecyclerView() {

        GridLayoutManager manager1 = new GridLayoutManager(this, 4);
        GridLayoutManager manager2 = new GridLayoutManager(this, 4);
        GridLayoutManager manager3 = new GridLayoutManager(this, 4);
        GridLayoutManager manager4 = new GridLayoutManager(this, 4);
        //GridLayoutManager manager5 = new GridLayoutManager(this, 4);


        productRecyclerView1.setLayoutManager(manager1);
        productRecyclerView2.setLayoutManager(manager2);
        productRecyclerView3.setLayoutManager(manager3);
        productRecyclerView4.setLayoutManager(manager4);
 //       productRecyclerView5.setLayoutManager(manager5);



//        productRecyclerView5.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


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


        Ref.child("ProductList").orderByChild("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList1.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {

                            String type=dataSnapshot1.child("type").getValue().toString();
                            if(type.equals("invest")){
                                String productName = dataSnapshot1.child("Name").getValue().toString();
                                String productImage = dataSnapshot1.child("Image").getValue().toString();
                                Log.i("TAG",productName);
                                productsArrayList1.add(new Products(productName, productImage));
                            }
                    }
                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList1);
                productRecyclerView1.setAdapter(adapter);
                loadProducts.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        Ref.child("ProductList").orderByChild("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList2.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {

                        String type=dataSnapshot1.child("type").getValue().toString();
                        if(type.equals("insu")){
                            String productName = dataSnapshot1.child("Name").getValue().toString();
                            String productImage = dataSnapshot1.child("Image").getValue().toString();
                            Log.i("TAG",productName);
                            productsArrayList2.add(new Products(productName, productImage));
                        }
                    }
                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList2);
                productRecyclerView2.setAdapter(adapter);
                loadProducts.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });







        Ref.child("ProductList").orderByChild("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList3.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {

                        String type=dataSnapshot1.child("type").getValue().toString();
                        if(type.equals("loan")){
                            String productName = dataSnapshot1.child("Name").getValue().toString();
                            String productImage = dataSnapshot1.child("Image").getValue().toString();
                            Log.i("TAG",productName);
                            productsArrayList3.add(new Products(productName, productImage));
                        }
                    }
                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList3);
                productRecyclerView3.setAdapter(adapter);
                loadProducts.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });







        Ref.child("ProductList").orderByChild("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsArrayList4.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {

                        String type=dataSnapshot1.child("type").getValue().toString();
                        if(type.equals("misc")){
                            String productName = dataSnapshot1.child("Name").getValue().toString();
                            String productImage = dataSnapshot1.child("Image").getValue().toString();
                            Log.i("TAG",productName);
                            productsArrayList4.add(new Products(productName, productImage));
                        }
                    }
                }
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList4);
                productRecyclerView4.setAdapter(adapter);
                loadProducts.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



//        Ref.child("ProductList").orderByChild("order").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                productsArrayList5.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    if (dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image")) {
//
//                        String type=dataSnapshot1.child("type").getValue().toString();
//                        if(type.equals("deals")){
//                            String productName = dataSnapshot1.child("Name").getValue().toString();
//                            String productImage = dataSnapshot1.child("Image").getValue().toString();
//                            Log.i("TAG",productName);
//                            productsArrayList5.add(new Products(productName, productImage));
//                        }
//                    }
//                }
//                ProductAdapter adapter = new ProductAdapter(MainActivity.this, productsArrayList5);
////                productRecyclerView5.setAdapter(adapter);
//                loadProducts.setVisibility(View.INVISIBLE);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });






    }


    //SET DATA IN NAVHEADER
    private void SetNavigationView() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final View header = navigationView.getHeaderView(0);

        //navigationView.getMenu().findItem(R.id.side_saving).setActionView(R.layout.menu_image);

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
                    numText.setText(number.substring(3));
                }

                if (dataSnapshot.child("email").exists()) {
                    String email = dataSnapshot.child("email").getValue().toString();
                    TextView numText = (TextView) header.findViewById(R.id.sidenav_header_email);
                    numText.setText(email);
                }

                if (dataSnapshot.child("privilege").exists()) {
                    String privilege = dataSnapshot.child("privilege").getValue().toString();
                    TextView privilegeText = (TextView) header.findViewById(R.id.sidenav_header_privilege);
                    if(privilege.equals("SuperAdmin")){
                        privilegeText.setVisibility(View.VISIBLE);
                    }
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Menu menu = navigationView.getMenu();


        switch (menuItem.getItemId()) {

//            case R.id.side_products: {
//
//
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//
//
//            }
//
//            case R.id.side_ads: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(MainActivity.this, AdsActivity.class);
//
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            }
//
//            case R.id.side_users: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            }
//
//            case R.id.side_home: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                break;
//            }

            case R.id.side_myapps: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, MyApplications.class);

                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }

            case R.id.side_credit_card: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Credit Card");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }

//            case R.id.side_taxes: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(MainActivity.this, TaxInfoActivity.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            }


            case R.id.side_loan: {

                boolean b = !menu.findItem(R.id.side_carloan).isVisible();

                //menu.findItem(R.id.side_businessloan).setVisible(b);
                menu.findItem(R.id.side_carloan).setVisible(b);

                menu.findItem(R.id.side_twloan).setVisible(b);
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
                menu.findItem(R.id.side_two_wheeler).setVisible(b);

                break;

            }

            case R.id.Tools: {
                boolean b = !menu.findItem(R.id.side_emi_cal).isVisible();
                menu.findItem(R.id.side_emi_cal).setVisible(b);
                menu.findItem(R.id.side_ifsc).setVisible(b);
                menu.findItem(R.id.side_income).setVisible(b);
//                menu.findItem(R.id.side_gold).setVisible(b);
//                menu.findItem(R.id.side_fuel).setVisible(b);

                break;
            }

            case R.id.chat_support: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(MainActivity.this, "To be added soon!", Toast.LENGTH_SHORT).show();

                break;
            }


//            case R.id.side_gold: {
//
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialogbox, null);
//                mBuilder.setTitle("Select State");
//                Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.state));
//                adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
//                mSpinner.setAdapter(adapter);
//
//                mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view,
//                                               int position, long id) {
//                        label = parent.getItemAtPosition(position).toString();
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        Toast.makeText(adapterView.getContext(), "Please select something",Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(MainActivity.this, GoldRates.class);
//                        intent.putExtra("currentState", label);
//                        startActivity(intent);
//
//                    }
//
//                });
//                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.show();
//
//                break;
//
//            }

//            case R.id.side_fuel: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialogbox, null);
//                mBuilder.setTitle("Select State");
//                Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.state));
//                adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
//                mSpinner.setAdapter(adapter);
//
//                mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view,
//                                               int position, long id) {
//                        label = parent.getItemAtPosition(position).toString();
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        Toast.makeText(adapterView.getContext(), "Please select something", Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(MainActivity.this, FuelRates.class);
//                        intent.putExtra("currentState", label);
//                        startActivity(intent);
//
//                    }
//
//                });
//                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.show();
//
//                break;
//
//            }

            case R.id.Investments: {
                boolean b = !menu.findItem(R.id.side_dmat).isVisible();
                menu.findItem(R.id.side_dmat).setVisible(b);
                menu.findItem(R.id.side_fd).setVisible(b);
                menu.findItem(R.id.side_mf).setVisible(b);
                menu.findItem(R.id.side_saving).setVisible(b);
                menu.findItem(R.id.side_credit_card).setVisible(b);

                break;
            }


            case R.id.side_taxes: {
                boolean b = !menu.findItem(R.id.side_tax_register).isVisible();
                menu.findItem(R.id.side_tax_register).setVisible(b);
                menu.findItem(R.id.side_tax_return).setVisible(b);

                break;
            }


            case R.id.side_tax_register: {
                mAuth = FirebaseAuth.getInstance();
                currentUserID = mAuth.getCurrentUser().getUid();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(getBaseContext(),"Under Construction!", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
//                intent.putExtra("productName", "Tax Registration");
//                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;            }

            case R.id.side_tax_return: {
                mAuth = FirebaseAuth.getInstance();
                currentUserID = mAuth.getCurrentUser().getUid();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(getBaseContext(),"Under Construction!", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
//                intent.putExtra("productName", "Tax Return");
//
//                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }


            case R.id.side_dmat: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Demat");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }


            case R.id.side_fd: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Fixed Deposit");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }


            case R.id.side_mf: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Mutual Funds");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }

            case R.id.side_emi_cal: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, EMICalculatorActivity.class));

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }


            case R.id.side_ifsc: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, IFSCCodeFinderActivity.class));

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }

            case R.id.side_income: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, IncomeTaxCalculatorActivity.class));

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }


            case R.id.side_logout: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;

            }
//            case R.id.side_myaccount: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                ChangeActivity(MyAccountActivity.class);
//                break;
//
//            }

//            case R.id.side_myoffer: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(MainActivity.this, MyOfferActivity.class));
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//
//            }
//
//            case R.id.notice: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(MainActivity.this, NoticeToCustomerActivity.class));
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//
//            }
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

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
//            case R.id.side_businessloan: {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
//                intent.putExtra("productName", "Business Loan");
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//
//            }

            case R.id.side_carloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Car Loan");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
            case R.id.side_educationloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Education Loan");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }


            case R.id.side_play: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Play Games");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }



            case R.id.side_saving: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Saving Account");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
            case R.id.side_homeloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Home Loan");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
            case R.id.side_instantloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Instant Loan");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
            case R.id.side_personalloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Personal Loan");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
            case R.id.side_terminsurance: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Term Insurance");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }
            case R.id.side_carinsurance: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Car Insurance");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }


            case R.id.side_two_wheeler: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Two Wheeler Insurance");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }


            case R.id.side_twloan: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Two Wheeler Loan");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            }




            case R.id.side_healthinsurance: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", "Health insurance");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

//                case R.id.bot_recharge: {
//                    ChangeActivity(RechargeActivity.class);
//
//                    break;
//                }
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
//        mSlideViewPager2 = findViewById(R.id.main_cardView1);
//        mSlideViewPager3 = findViewById(R.id.main_cardView2);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //ImageButton imageButton=findViewById(R.id.toolbar_button);

        productRecyclerView1 = findViewById(R.id.product_recyclerview1);
        productRecyclerView2 = findViewById(R.id.product_recyclerview2);
        productRecyclerView3 = findViewById(R.id.product_recyclerview3);
        productRecyclerView4 = findViewById(R.id.product_recyclerview4);
      //  productRecyclerView5 = findViewById(R.id.product_recyclerview5);

//        viewFuelRates = (Button)findViewById(R.id.view_fuel_rates);
//        viewGoldRates = (Button)findViewById(R.id.view_gold_rates);
//        viewCreditScore = (Button)findViewById(R.id.view_credit);
//        viewEMI = (Button)findViewById(R.id.view_emi);
//        viewIFSC = (Button)findViewById(R.id.view_ifsc);


        drawerLayout = findViewById(R.id.drawer_layout);
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, mToolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void run(View view){
        Intent intent = new Intent(this, MyOfferActivity.class);

        startActivity(intent);

    }

    public void run1(View view){
        Intent intent = new Intent(this, MyAccountActivity.class);

        startActivity(intent);

    }
}
