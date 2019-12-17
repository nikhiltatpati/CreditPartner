package com.example.creditpartner.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyInfoActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String phoneNumber, name, email, reference, privilege;
    private String verificationID, currentUserID;
    private ImageView verifyLogo;
    private TextView verifyButton;
    private EditText otpCode;
    private FirebaseAuth mAuth;
    private TextView otpMessage, resendOTP;
    private DatabaseReference Ref;
    private FirebaseUser currentUser;
    private String[] permissionArray = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS};
    private static final int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_info);

        Initialize();


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = otpCode.getText().toString().trim();

                if (code.isEmpty()) {
                    otpCode.setError("Enter valid code!");
                    otpCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sendVerificationCode(phoneNumber);

    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
            signInWithCredentials(credential);
        } catch (Exception e) {
            Toast.makeText(VerifyInfoActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    } else {

                                    }
                                }
                            });

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                            Date date = new Date();
                            String timeStamp = simpleDateFormat.format(date);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("phoneNumber", phoneNumber);
                            hashMap.put("name", name);
                            hashMap.put("reference", reference);
                            hashMap.put("email", email);
                            hashMap.put("privilege", privilege);
                            hashMap.put("timeStamp",timeStamp);

                            currentUserID = currentUser.getUid();

                            Ref.child("Customers").child("BasicInfo").child(currentUserID).setValue(hashMap);
                            Intent intent = new Intent(VerifyInfoActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();



                        } else {
                            Toast.makeText(VerifyInfoActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationID = s;


        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyInfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    private void Initialize() {

        //Get all user details previously filled
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        reference = getIntent().getStringExtra("reference");
        privilege = getIntent().getStringExtra("privilege");

        otpCode = (EditText) findViewById(R.id.otp_code);
        verifyButton = (TextView) findViewById(R.id.verify_button);
        verifyLogo = (ImageView) findViewById(R.id.verify_logo);

        resendOTP = (TextView) findViewById(R.id.otp_resend);
        otpMessage = (TextView) findViewById(R.id.otp_message);

        mAuth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();

    }


}
