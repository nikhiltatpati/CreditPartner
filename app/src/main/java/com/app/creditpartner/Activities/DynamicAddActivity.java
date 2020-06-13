package com.app.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.creditpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DynamicAddActivity extends AppCompatActivity {

    private EditText addAdminNumber, addProductName, addProductImage, addAdLink;
    private Button addButton, removeButton;
    private String optionSelected;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private String currentUserID,key, isSuperAdmin;
    private static final String countryCode = "+91";

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_add);

        Initialize();

        GetSuitableViewsAndPerformAction();




    }

    private void GetSuitableViewsAndPerformAction() {
        if (optionSelected.equals("Add Product")) {
            addAdminNumber.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);
            addAdLink.setVisibility(View.GONE);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productname = addProductName.getText().toString();
                    String productimage = addProductImage.getText().toString();

                    if(productimage.isEmpty() || productname.isEmpty())
                    {
                        Toast.makeText(DynamicAddActivity.this,"Please enter valid details!", Toast.LENGTH_LONG).show();;
                    }
                    else {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Image", productimage);
                        hashMap.put("Name", productname);

                        Ref.child("ProductList").push().setValue(hashMap);
                        Toast.makeText(DynamicAddActivity.this, "Product added!", Toast.LENGTH_LONG).show();

                        ChangeActivity();
                    }
                }
            });



        } else if (optionSelected.equals("Add Admin")) {
            addProductName.setVisibility(View.GONE);
            addProductImage.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);
            addAdLink.setVisibility(View.GONE);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String adminnumber = countryCode + addAdminNumber.getText().toString();

                    if(adminnumber.isEmpty() || adminnumber.length() <13)
                    {
                        Toast.makeText(DynamicAddActivity.this,"Please enter valid details!", Toast.LENGTH_LONG).show();;
                    }
                    else {
                        Ref.child("Privileges").child(adminnumber).setValue("Admin");
                        Toast.makeText(DynamicAddActivity.this, "Admin added!", Toast.LENGTH_LONG).show();

                        ChangeActivity();
                    }
                }
            });

        } else if (optionSelected.equals("Remove Product")) {
            addProductImage.setVisibility(View.GONE);
            addAdminNumber.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            addAdLink.setVisibility(View.GONE);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String productname = addProductName.getText().toString();

                    if(productname.isEmpty())
                    {
                        Toast.makeText(DynamicAddActivity.this,"Please enter valid details!", Toast.LENGTH_LONG).show();;
                    }
                    else {

                        Ref.child("ProductList").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("Name").getValue().toString().equals(productname)) {
                                        key = dataSnapshot1.getKey();
                                        Ref.child("ProductList").child(key).removeValue();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(DynamicAddActivity.this, "Product removed!", Toast.LENGTH_LONG).show();

                        ChangeActivity();

                    }
                }
            });
        } else if(optionSelected.equals("Remove Admin")){

            addProductName.setVisibility(View.GONE);
            addProductImage.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            addAdLink.setVisibility(View.GONE);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String adminnumber = countryCode + addAdminNumber.getText().toString();

                    if(adminnumber.isEmpty() || adminnumber.length() <13)
                    {
                        Toast.makeText(DynamicAddActivity.this,"Please enter valid details!", Toast.LENGTH_LONG).show();;
                    }
                    else
                    {
                        Ref.child("Privileges").child(adminnumber).removeValue();
                        Toast.makeText(DynamicAddActivity.this, "Admin removed!", Toast.LENGTH_LONG).show();
                        ChangeActivity();

                    }}
            });

        }

        else if(optionSelected.equals("Add Ads"))
        {
            removeButton.setVisibility(View.GONE);
            addAdminNumber.setVisibility(View.GONE);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   String name = addProductName.getText().toString();
                   String link = addAdLink.getText().toString();
                   String image = addProductImage.getText().toString();

                   if(name.isEmpty() || link.isEmpty() || image.isEmpty())
                   {
                       Toast.makeText(DynamicAddActivity.this,"Please enter valid details!", Toast.LENGTH_LONG).show();;
                   }
                   else
                   {
                       HashMap<String, String> hashMap = new HashMap<>();
                       hashMap.put("adText",name);
                       hashMap.put("adLink",link);
                       hashMap.put("adImage",image);


                       Ref.child("Banners").push().setValue(hashMap);
                       Toast.makeText(DynamicAddActivity.this, "Ad Added", Toast.LENGTH_LONG).show();
                       ChangeActivity();
                   }


                }
            });
        }
        else
        {
            addButton.setVisibility(View.GONE);
            addAdminNumber.setVisibility(View.GONE);
            addAdLink.setVisibility(View.GONE);
            addProductName.setVisibility(View.GONE);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String image = addProductImage.getText().toString();


                    if(image.isEmpty())
                    {
                        Toast.makeText(DynamicAddActivity.this,"Please enter valid details!", Toast.LENGTH_LONG).show();;
                    }
                    else
                    {

                        Ref.child("Banners").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    if(snapshot.hasChild("adImage"))
                                    {
                                        if(snapshot.child("adImage").getValue().toString().equals(image))
                                        {
                                            String key = snapshot.getKey();
                                            Ref.child(key).removeValue();
                                            Toast.makeText(DynamicAddActivity.this, "Ad Removed", Toast.LENGTH_LONG).show();
                                            ChangeActivity();

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                }
            });
        }
    }

    private void ChangeActivity() {
        startActivity(new Intent(DynamicAddActivity.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void Initialize() {
        optionSelected = getIntent().getStringExtra("optionSelected");
        isSuperAdmin = getIntent().getStringExtra("isSuperAdmin");

        SetupToolbar();
        addAdminNumber = (EditText) findViewById(R.id.add_admin_number);
        addProductImage = (EditText) findViewById(R.id.add_product_image);
        addProductName = (EditText) findViewById(R.id.add_product_name);
        addAdLink = (EditText) findViewById(R.id.add_ad_url);

        addButton = (Button) findViewById(R.id.add_option_button);
        removeButton = (Button) findViewById(R.id.remove_option_button);

        Ref = FirebaseDatabase.getInstance().getReference();

    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.options_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(optionSelected);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
