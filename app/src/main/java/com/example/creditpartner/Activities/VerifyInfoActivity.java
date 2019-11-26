package com.example.creditpartner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyInfoActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String phoneNumber;
    private String verificationID;
    private Button verifyButton;
    private EditText otpCode;
    private ProgressBar loadOTP;
    private FirebaseAuth mAuth;
    private TextView otpMessage, resendOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_info);

        Initialize();

        sendVerificationCode(phoneNumber);


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = otpCode.getText().toString().trim();

                if (code.isEmpty()) {
                    otpCode.setError("Enter valid code!");
                    otpCode.requestFocus();
                    return;
                }
                loadOTP.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });

    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(VerifyInfoActivity.this, MainActivity.class);
                            startActivity(intent);
                            loadOTP.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(VerifyInfoActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                            loadOTP.setVisibility(View.INVISIBLE);

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
        SetupToolbar();

        //Get phone number previously filled
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        otpCode = (EditText) findViewById(R.id.otp_code);
        verifyButton = (Button) findViewById(R.id.verify_button);
        loadOTP = (ProgressBar) findViewById(R.id.load_otp);
        loadOTP.setVisibility(View.GONE); //Dont show before verify

        resendOTP = (TextView)findViewById(R.id.otp_resend);
        otpMessage = (TextView)findViewById(R.id.otp_message);

        mAuth = FirebaseAuth.getInstance();

    }

    private void SetupToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.verify_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Credit Partner");

    }

}
