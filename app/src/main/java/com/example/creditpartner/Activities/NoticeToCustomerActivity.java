package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.creditpartner.Classes.MySingletonClass;
import com.example.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NoticeToCustomerActivity extends AppCompatActivity {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAo4Hho80:APA91bF-6ME8GVGia13gunidiOjFHBJA575kMeAu7Sb1lbrmgzYgSjwWvPmNkRIq9ydcrNZWA-Na0L-LrK1GUy1MHeXGDw5E-Pd9S-rpw6ITDgJ5lqSszhqbgEM-ptsVi582ajf2qhD6";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private Toolbar mToolbar;
    private DatabaseReference Ref;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE, NOTIFICATION_IMAGE;
    String TOPIC, currentUserID;


    EditText edtTitle;
    EditText edtMessage;
    EditText edtImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_to_customer);

        FirebaseMessaging.getInstance().subscribeToTopic("offers");
        edtTitle = findViewById(R.id.noti_name);
        edtMessage = findViewById(R.id.noti_text);
        edtImage = findViewById(R.id.noti_image_link);
        Button btnSend = findViewById(R.id.send_noti);

        Ref= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        SetupToolbar();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOPIC = "/topics/offers"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = edtTitle.getText().toString();
                NOTIFICATION_MESSAGE = edtMessage.getText().toString();
                NOTIFICATION_IMAGE = "https://dhanam77.github.io/assets/images/profile.png";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(new Date());


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("title", NOTIFICATION_TITLE);
                hashMap.put("message", NOTIFICATION_MESSAGE);
                hashMap.put("date", date);
                Ref.child("MyOffers").push().setValue(hashMap);
                Toast.makeText(NoticeToCustomerActivity.this, "Notification Sent!", Toast.LENGTH_SHORT).show();


                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
                    notifcationBody.put("image", NOTIFICATION_IMAGE);

                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                sendNotification(notification);
            }
        });
    }

    private void SetupToolbar() {
        mToolbar = findViewById(R.id.noticebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Send Notice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        edtTitle.setText("");
                        edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NoticeToCustomerActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingletonClass.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}