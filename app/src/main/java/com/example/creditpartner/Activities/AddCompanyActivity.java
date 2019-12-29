package com.example.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.creditpartner.Classes.Upload;
import com.example.creditpartner.R;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class AddCompanyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout companyName, companyURL;
    private ImageView companyImage;
    private TextInputEditText name, url, value1, value2, productID, field1, field2;
    private Button features;
    private Button addCompanyButton, chooseImage;
    private String productTitle, type, key, dburl, text;
    private DatabaseReference Ref;
    private ProgressBar addProgress;
    private ImageView viewImage;
    //uri to store file
    private Uri filePath;
    private boolean noData = true;


    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private static final int PICK_IMAGE_REQUEST = 234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        Initialize();

        features.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCompanyActivity.this, TextEditorActivity.class);

                startActivityForResult(intent, 0);
            }
        });

        if (type.equals("edit")) {
            GetDetails();
        } else if (type.equals("taxedit")) {
            GetTaxDetails();
        }


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = name.getText().toString();
                String id = productID.getText().toString();
                String urlString = url.getText().toString();
                String value1S = value1.getText().toString();
                String value2S = value2.getText().toString();
                String field1S = field1.getText().toString();
                String field2S = field2.getText().toString();
                String featuresString = features.getText().toString();


                if (nameString.isEmpty()) {
                    name.setError("Enter Valid name!");
                } else if (urlString.isEmpty()) {
                    url.setError("Enter Valid url!");
                } else if (id.isEmpty()) {
                    url.setError("Enter Valid Product ID!");
                } else if (value1S.isEmpty()) {
                    value1.setError("Enter Valid Value!");
                } else if (value2S.isEmpty()) {
                    value2.setError("Enter Valid Value!");
                } else if (field1S.isEmpty()) {
                    field1.setError("Enter Valid Field!");
                } else if (field2S.isEmpty()) {
                    field2.setError("Enter Valid Field!");
                }  else if (noData == true) {
                    Toast.makeText(AddCompanyActivity.this, "Add Features!", Toast.LENGTH_SHORT).show();
                } else {


                    uploadFile();


                }

            }
        });


    }

    private void GetTaxDetails() {

        productID.setVisibility(View.GONE);
        url.setVisibility(View.GONE);

        Ref.child("Taxes").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("companyName").getValue().toString());
//                url.setText(dataSnapshot.child("companyURL").getValue().toString());
//                productID.setText(dataSnapshot.child("productID").getValue().toString());
                value1.setText(dataSnapshot.child("value1").getValue().toString());
                value2.setText(dataSnapshot.child("value2").getValue().toString());
                value1.setText(dataSnapshot.child("value1").getValue().toString());
                field1.setText(dataSnapshot.child("field1").getValue().toString());
                field2.setText(dataSnapshot.child("field2").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetDetails() {


        Ref.child("CompanyList").child(productTitle).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("companyName").getValue().toString());
                url.setText(dataSnapshot.child("companyURL").getValue().toString());
                productID.setText(dataSnapshot.child("productID").getValue().toString());
                value1.setText(dataSnapshot.child("value1").getValue().toString());
                value2.setText(dataSnapshot.child("value2").getValue().toString());
                value1.setText(dataSnapshot.child("value1").getValue().toString());
                field1.setText(dataSnapshot.child("field1").getValue().toString());
                field2.setText(dataSnapshot.child("field2").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {

            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast

                            //creating the upload object to store uploaded image details
                            //   dburl = taskSnapshot.getStorage().getDownloadUrl().toString();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri DownloadUrl = urlTask.getResult();
                            dburl = DownloadUrl.toString();

                            HashMap hashMap = new HashMap();
                            hashMap.put("companyName", name.getText().toString());
                            hashMap.put("companyURL", url.getText().toString());
                            hashMap.put("value1", value1.getText().toString());
                            hashMap.put("value2", value2.getText().toString());
                            hashMap.put("field1", field1.getText().toString());
                            hashMap.put("field2", field2.getText().toString());
                            hashMap.put("productID", productID.getText().toString());
                            hashMap.put("companyImage", dburl);
                            hashMap.put("companyFeatures", text);
                            if (type.equals("edit")) {
                                Ref.child("CompanyList").child(productTitle).child(key).setValue(hashMap);
                            } else {
                                Ref.child("CompanyList").child(productTitle).push().setValue(hashMap);

                            }

                            addProgress.setVisibility(View.INVISIBLE);


                            Intent intent = new Intent(AddCompanyActivity.this, ProductDetailActivity.class);
                            intent.putExtra("productName", productTitle);
                            finish();
                            startActivity(intent);




                        /*    //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);*/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(AddCompanyActivity.this, "Select Image", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                viewImage.setImageBitmap(bitmap);
                viewImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else
        {
        if(data!= null) {
            text = data.getStringExtra("text");
            if (text != null) {
                noData = false;
                Log.e("textac", "" + text);

            } else {
                noData = true;
            }
        }
        }



    }


    private void Initialize() {


        productTitle = getIntent().getStringExtra("productTitle");
        key = getIntent().getStringExtra("key");
        addProgress = (ProgressBar) findViewById(R.id.add_company_progressbar);
        addProgress.setVisibility(View.INVISIBLE);


        SetupToolbar();
        Ref = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);
        //TextInputLayouts
        companyName = (TextInputLayout) findViewById(R.id.company_name);
        viewImage = (ImageView) findViewById(R.id.image_chose);
        companyURL = (TextInputLayout) findViewById(R.id.company_url);

        features = (Button) findViewById(R.id.features);
        name = (TextInputEditText) findViewById(R.id.name);
        productID = (TextInputEditText) findViewById(R.id.id);
        url = (TextInputEditText) findViewById(R.id.url);
        value1 = (TextInputEditText) findViewById(R.id.value1);
        value2 = (TextInputEditText) findViewById(R.id.value2);
        field1 = (TextInputEditText) findViewById(R.id.field1);
        field2 = (TextInputEditText) findViewById(R.id.field2);
        addCompanyButton = (Button) findViewById(R.id.add_company_button);
        type = getIntent().getStringExtra("type");
        chooseImage = (Button) findViewById(R.id.choose_image);
    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_company_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Company");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}




