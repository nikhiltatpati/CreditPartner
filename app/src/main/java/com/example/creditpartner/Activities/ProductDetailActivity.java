package com.example.creditpartner.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creditpartner.Adapters.CompanyAdapter;
import com.example.creditpartner.Adapters.SideUserAdapter;
import com.example.creditpartner.Classes.Companies;
import com.example.creditpartner.Classes.Users;
import com.example.creditpartner.Interfaces.OnStartDragListener;
import com.example.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProductDetailActivity extends AppCompatActivity  {

    private Toolbar mToolbar;
    private String productTitle, currentUserID, phoneNumber;
    private RecyclerView companyRecyclerview;
    private ArrayList<Companies> companiesArrayList = new ArrayList<>();
    private String companyName, companyImage, companyRate, companyBalance, companyFeatures;
    private ImageButton addCompanyButton;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private FirebaseUser currentUser;
    private ProgressBar loadCompanies;
    private SearchView searchView;
    private CompanyAdapter adapter;
    private TextView noCOmpanies;
    private boolean isSuperAdmin = false;

    ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        Initialize();

        SetupRecyclerview();

        CheckUserOrAdmin();

        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("productTitle", productTitle); // Storing string

        editor.commit(); // commit changes



        /*
        Add company feature only for superadmins and admins!
         */

        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, AddCompanyActivity.class);
                intent.putExtra("productTitle", productTitle);
                intent.putExtra("type", "add");
                intent.putExtra("key", "null");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class));
    }

    private void CheckUserOrAdmin() {

        Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("privilege").getValue().equals("SuperAdmin")) {
                    addCompanyButton.setVisibility(View.VISIBLE);
                    isSuperAdmin = true;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN
            | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(companiesArrayList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);


            ChangeFirebaseOrder(fromPosition, toPosition);


            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    private void ChangeFirebaseOrder(int fromPosition, int toPosition) {

        Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("order").getValue().toString().equals(String.valueOf(fromPosition+1)))
                    {
                        Ref.child("ProductList").child(dataSnapshot1.getKey()).child("order").setValue(toPosition+1);
                    }
                    if(dataSnapshot1.child("order").getValue().toString().equals(String.valueOf(toPosition+1)))
                    {
                        Ref.child("ProductList").child(dataSnapshot1.getKey()).child("order").setValue(fromPosition+1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void SetupRecyclerview() {

        //Linear layout for all companies
        loadCompanies.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        companyRecyclerview.setLayoutManager(linearLayoutManager);

        if (productTitle == null) {
            SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            productTitle = pref.getString("productTitle", null);
        }





        //Get Company list from firebase
        Ref.child("CompanyList").child(productTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companiesArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("companyName")) {
                        companyName = dataSnapshot1.child("companyName").getValue().toString();
                    }
                    if (dataSnapshot1.hasChild("companyImage")) {
                        companyImage = dataSnapshot1.child("companyImage").getValue().toString();
                    }

                    if (dataSnapshot1.hasChild("companyRate")) {
                        companyRate = dataSnapshot1.child("companyRate").getValue().toString() + "%";
                    }

                    if (dataSnapshot1.hasChild("companyMinimumBalance")) {
                        companyBalance = "₹" + dataSnapshot1.child("companyMinimumBalance").getValue().toString();
                    }

                    if (dataSnapshot1.hasChild("companyFeatures")) {
                        companyFeatures = dataSnapshot1.child("companyFeatures").getValue().toString();
                    }

                    companiesArrayList.add(new Companies(companyImage, companyName, companyRate, companyBalance, companyFeatures));
                }

                if (companiesArrayList.size() == 0) {
                    noCOmpanies.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                    companyRecyclerview.setVisibility(View.GONE);
                }

                Collections.sort(companiesArrayList, new Comparator<Companies>() {
                    @Override
                    public int compare(Companies companies, Companies t1) {
                        String s1 = companies.getCompanyName();
                        String s2 = t1.getCompanyName();
                        return s1.compareToIgnoreCase(s2);
                    }

                });

                adapter = new CompanyAdapter(ProductDetailActivity.this, companiesArrayList, productTitle);
                companyRecyclerview.setAdapter(adapter);
                loadCompanies.setVisibility(View.INVISIBLE);

                Ref.child("Customers").child("BasicInfo").child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("privilege").getValue().equals("SuperAdmin")) {
                            isSuperAdmin = true;
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                            itemTouchHelper.attachToRecyclerView(companyRecyclerview);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Log.e("superadmin", String.valueOf(isSuperAdmin));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.search_companies);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    private void Initialize() {


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        productTitle = getIntent().getStringExtra("productName");

        noCOmpanies = (TextView) findViewById(R.id.no_companies);
        companyRecyclerview = (RecyclerView) findViewById(R.id.company_recyclerview);
        Ref = FirebaseDatabase.getInstance().getReference();
        addCompanyButton = (ImageButton) findViewById(R.id.add_company);
        addCompanyButton.setVisibility(View.GONE);

        loadCompanies = (ProgressBar) findViewById(R.id.load_companies);
        SetupToolbars();
    }

    private void SetupToolbars() {

        mToolbar = (Toolbar) findViewById(R.id.productdetail_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(productTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}


