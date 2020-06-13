package com.app.creditpartner.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.creditpartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VerifyInfoActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String phoneNumber, name, email, reference, privilege;
    private String verificationID, currentUserID;
    private ImageView verifyLogo;
    private Button verifyButton;
    private EditText otpCode;
    private FirebaseAuth mAuth;
    private TextView otpMessage, resendOTP;
    private DatabaseReference Ref;
    private FirebaseUser currentUser;
    String key, type;
    private String[] permissionArray = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS};
    private static final int requestCode = 1;

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
                verifyCode(code);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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
                            currentUserID = currentUser.getUid();

                            startActivity(new Intent(VerifyInfoActivity.this, MainActivity.class));





                        /*    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                            Date date = new Date();
                            String timeStamp = simpleDateFormat.format(date);
*/


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("phoneNumber", phoneNumber);
                            hashMap.put("name", name);
                            hashMap.put("reference", reference);
                            hashMap.put("email", email);
                            //                          hashMap.put("timeStamp", timeStamp);
                            hashMap.put("privilege", privilege);

                            Ref.child("Customers").child("BasicInfo").child(currentUserID).setValue(hashMap);


                            if (type.equals("new")) {
    //                            sendMail();

                            }

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

    protected void sendMail() {
        final String username = "spicetechtask@gmail.com";
        final String password = "tech123spice";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("" + email));
            message.setSubject("Welcome to Credit Partner");
            message.setText("Message : We are glad to welcome you here at CreditPartner. I hope we don't disappoint you!");

            new VerifyInfoActivity.SendMailTask().execute(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

    private class SendMailTask extends AsyncTask<Message, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (SendFailedException ee) {

                return "error1";
            } catch (MessagingException e) {

                return "error2";
            }

        }


        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Success")) {

                super.onPostExecute(result);

            }
        }

    }


    private void Initialize() {

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        //Get all user details previously filled
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        reference = getIntent().getStringExtra("reference");
        key = getIntent().getStringExtra("key");
        privilege = getIntent().getStringExtra("privilege");
        type = getIntent().getStringExtra("type");

        otpCode = (EditText) findViewById(R.id.otp_code);
        verifyButton = (Button) findViewById(R.id.verify_button);
       // verifyLogo = (ImageView) findViewById(R.id.verify_logo);

        otpMessage = (TextView) findViewById(R.id.otp_message);

        mAuth = FirebaseAuth.getInstance();

        Ref = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
