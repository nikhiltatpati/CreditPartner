package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.creditpartner.Adapters.SideUserAdapter;
import com.example.creditpartner.Classes.UserSorter;
import com.example.creditpartner.Classes.Users;
import com.example.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference Ref;
    private String currentUserID, username, usernumber, usertype;
    private SideUserAdapter adapter;
    private androidx.appcompat.widget.SearchView searchView;
    private ImageButton adduser;
    private ArrayList<Users> usersArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        Initialize();

        SetupRecyclerview();


        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, AddUsersActivity.class);
                intent.putExtra("type", "add");
                intent.putExtra("key", "null");
                startActivity(intent);
            }
        });
    }

    private void SetupRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



        Ref.child("Customers").child("BasicInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        username = dataSnapshot1.child("name").getValue().toString();
                        usernumber = dataSnapshot1.child("phoneNumber").getValue().toString();
                        usertype = dataSnapshot1.child("privilege").getValue().toString();
                        usersArrayList.add(new Users(username, usertype, usernumber));
                }

                Collections.sort(usersArrayList, new UserSorter());

                adapter = new SideUserAdapter(UsersActivity.this, usersArrayList);
                recyclerView.setAdapter(adapter);
                // loadProducts.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.search_user);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null){
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });


    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);


        MenuItem searchItem = menu.findItem(R.id.search_user);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }*/




    private void Initialize() {
        SetupToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();
        currentUserID = currentUser.getUid();

        adduser = (ImageButton)findViewById(R.id.add_user);
        recyclerView = (RecyclerView)findViewById(R.id.users_recycler);
    }

    private void SetupToolbar() {


        mToolbar = (Toolbar) findViewById(R.id.user_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
