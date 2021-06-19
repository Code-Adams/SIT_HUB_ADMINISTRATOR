package com.SakshmBhat.sit_hub_administrator.userAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.MainActivity;
import com.SakshmBhat.sit_hub_administrator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private String phoneNumber,mVerificationId;
    private EditText otp;
    private boolean mVerificationInProgress = false,getOtpClicked=false;
    private Button verifyBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView resendOTP,timer;
    private Dialog dialog;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initialize();

        getOtpOnCreate();

    }

    private void getOtpOnCreate() {

        if (!getOtpClicked){

                getOtpClicked = true;
                sendVerificationCode(phoneNumber);
//                getOTP.setTextColor(Color.parseColor("#C0BEBE"));
                dialog = new Dialog(OtpActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_wait);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }

        }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(OtpActivity.this)
                        .setCallbacks(mCallBack)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            dialog.dismiss();
            verifyBtn.setTextColor(Color.parseColor("#000000"));
            Toast.makeText(OtpActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;

            timer.setVisibility(View.VISIBLE);

            new CountDownTimer(60000,1000){


                @Override
                public void onTick(long millisUntilFinished) {

                    timer.setText("" + millisUntilFinished/1000);

                }

                @Override
                public void onFinish() {

                    resendOTP.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.INVISIBLE);

                }
            }.start();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null){

                otp.setText(code);
                verifyCode(code);


            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            getOtpClicked = false;
           // getOTP.setTextColor(Color.parseColor("00000FF"));
            Toast.makeText(OtpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        signInWithCredential(credential);


    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            mCurrentUser = mAuth.getCurrentUser();
                            if (mCurrentUser !=null){

                                //dialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();

                            }else {

                                if (dialog != null){

                                    dialog.dismiss();
                                    Toast.makeText(OtpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }


    private void initialize() {

        phoneNumber= getIntent().getStringExtra("phoneNumber");
        phoneNumber= "+91"+phoneNumber;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        otp=findViewById(R.id.otpField);
        progressBar = findViewById(R.id.progressBar);
        verifyBtn = findViewById(R.id.verifyBtn);
        resendOTP = findViewById(R.id.resendOTP);
        timer=findViewById(R.id.countdownTimer);

    }
}