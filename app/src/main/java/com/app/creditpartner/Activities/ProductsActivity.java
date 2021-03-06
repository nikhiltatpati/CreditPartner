
package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.app.creditpartner.Adapters.SideProductAdapter;
import com.app.creditpartner.Classes.Products;
import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private String decideScreen;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageButton additems;
    private DatabaseReference Ref;
    private ArrayList<Products> productsArrayList = new ArrayList<>();
    private SideProductAdapter adapter;
    private HashMap<String, Integer> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        Initialize();

        SetupRecyclerView();

        ImplementSearch();


    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN
            | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();


            Collections.swap(productsArrayList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            hashMap = new HashMap<>();
            hashMap.put("fromPosition", fromPosition);
            hashMap.put("toPosition", toPosition);


            Log.e("FROM POSITION", String.valueOf(fromPosition));
            Log.e("TO POSITION", String.valueOf(toPosition));

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();

       /* if(hashMap.get("fromPosition")!= null  && hashMap.get("toPosition")!= null)
        {
            int fromPosition = hashMap.get("fromPosition");
            int toPosition = hashMap.get("toPosition");
            ChangeFirebaseOrder(fromPosition, toPosition);
        }
*/

    }

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


    private void ImplementSearch() {

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.search_products);


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
    }

    private void SetupRecyclerView() {


            additems.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);


      /*      Query query = Ref.child("ProductList").orderByChild("order");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
*/
            Ref.child("ProductList").orderByChild("order").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productsArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if(dataSnapshot1.hasChild("Name") && dataSnapshot1.hasChild("Image"))
                        {
                            String productName = dataSnapshot1.child("Name").getValue().toString();
                            String productImage = dataSnapshot1.child("Image").getValue().toString();

                            productsArrayList.add(new Products(productName, productImage));

                        }
                        adapter = new SideProductAdapter(ProductsActivity.this, productsArrayList);
                        recyclerView.setAdapter(adapter);
                        // loadProducts.setVisibility(View.INVISIBLE);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





    }

    private void Initialize() {
        SetupToolbar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Ref = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView)findViewById(R.id.products_recycler);
        additems = (ImageButton)findViewById(R.id.add_items);
    }

    private void SetupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.product_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
