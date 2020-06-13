package com.app.creditpartner.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddAdsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputEditText urltiet, nameTIET, clickstiet;
    private Spinner adTypeSpinner;
    private EditText startDate, endDate;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private DatabaseReference Ref;
    private Button addAdsButton, chooseImage;
    final Calendar myCalendar = Calendar.getInstance();
    private Uri filePath;
    private ImageView viewImage;

    private String dburl, type, key;

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);

        InitializeFields();

        if (type.equals("edit")) {
            GetDetails();
        }

        adTypeSpinner.setOnItemSelectedListener(this);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select AdType");

        categories.add("Banner");
        categories.add("Pop-Up");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        adTypeSpinner.setAdapter(dataAdapter);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();


            }
        };

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();


            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAdsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAdsActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        addAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameTIET.getText().toString();
                String url = urltiet.getText().toString();
                String clicks = clickstiet.getText().toString();
                String type = adTypeSpinner.getSelectedItem().toString();
                String startdate = startDate.getText().toString();
                String enddate = endDate.getText().toString();

                if (name.isEmpty()) {
                    nameTIET.setError("Cannot be empty!");
                } else if (url.isEmpty()) {
                    urltiet.setError("Cannot be empty!");

                } else if (startdate.isEmpty()) {
                    startDate.setError("Cannot be empty!");

                } else if (enddate.isEmpty()) {
                    endDate.setError("Cannot be empty!");

                } else if (clicks.isEmpty()) {
                    clickstiet.setError("Cannot be empty!");

                } else if (type.equals("Select")) {
                    Toast.makeText(AddAdsActivity.this, "Add Type of Ad!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }


            }
        });


    }

    private void GetDetails() {

        Ref.child("Banners").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                urltiet.setText(dataSnapshot.child("adLink").getValue().toString());
                nameTIET.setText(dataSnapshot.child("customerName").getValue().toString());
                clickstiet.setText(dataSnapshot.child("noOfClicks").getValue().toString());
                startDate.setText(dataSnapshot.child("startDate").getValue().toString());
                endDate.setText(dataSnapshot.child("endDate").getValue().toString());

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


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("customerName", nameTIET.getText().toString());
                            hashMap.put("adImage", dburl);
                            hashMap.put("adLink", urltiet.getText().toString());
                            hashMap.put("adType", adTypeSpinner.getSelectedItem().toString());
                            hashMap.put("noOfClicks", clickstiet.getText().toString());
                            hashMap.put("startDate", startDate.getText().toString());
                            hashMap.put("endDate", endDate.getText().toString());

                            if (type.equals("edit")) {
                                Ref.child("Banners").child(key).setValue(hashMap);
                            } else {

                                Ref.child("Banners").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AddAdsActivity.this, "Ad added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            Intent intent = new Intent(AddAdsActivity.this, AdsActivity.class);
                            startActivity(intent);
                            finish();


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

            Toast.makeText(AddAdsActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
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
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void SendDataToDatabase(String name, String url, String clicks, String image, String startdate, String enddate, String type) {


    }


    private void InitializeFields() {


        SetupToolbar();

        key = getIntent().getStringExtra("key");
        type = getIntent().getStringExtra("type");
        //TextInputEditTexts
        clickstiet = (TextInputEditText) findViewById(R.id.adclicks);
        urltiet = (TextInputEditText) findViewById(R.id.adurl);
        nameTIET = (TextInputEditText) findViewById(R.id.customername);
        adTypeSpinner = (Spinner) findViewById(R.id.ad_type_spinner);
        startDate = (EditText) findViewById(R.id.start_date);
        endDate = (EditText) findViewById(R.id.end_date);
        viewImage = (ImageView) findViewById(R.id.view_image);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();

        addAdsButton = (Button) findViewById(R.id.add_ad_button);
        chooseImage = (Button) findViewById(R.id.choose_image);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);


    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.add_ads_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Ads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}




