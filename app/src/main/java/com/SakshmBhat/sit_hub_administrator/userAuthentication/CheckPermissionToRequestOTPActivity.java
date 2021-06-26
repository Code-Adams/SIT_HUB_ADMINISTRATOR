package com.SakshmBhat.sit_hub_administrator.userAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.SakshmBhat.sit_hub_administrator.MainActivity;
import com.SakshmBhat.sit_hub_administrator.R;
import com.SakshmBhat.sit_hub_administrator.SplashScreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckPermissionToRequestOTPActivity extends AppCompatActivity {

    private EditText phoneNumberET;
    private Button checkStatus;
    private TextView errorText;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission_to_request_otpactivity);

        if(!checkConnectivity()){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    CheckPermissionToRequestOTPActivity.this);
            builder.setTitle("No Internet");
            builder.setMessage("Close app.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                        }
                    });
            builder.show();
        }else {
            initialize();
            checkStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if (phoneNumberET.getText().toString().trim().length() < 11 && phoneNumberET.getText().toString().trim().length() > 9) {

                            pd.setMessage("Verifying...");
                            pd.setCancelable(false);
                            pd.show();

                            checkOTPPermissionStatus();
                        } else {

                            errorText.setText("Phone Number must be ten digits!");
                            errorText.setVisibility(View.VISIBLE);
                            phoneNumberET.setError("Wrong Format");
                            phoneNumberET.requestFocus();
                            phoneNumberET.setGravity(Gravity.CENTER);
                        }

                }
            });
        }

    }

    private void checkOTPPermissionStatus() {

            final String phoneNumber = phoneNumberET.getText().toString().trim();

            FirebaseDatabase.getInstance().getReference().child("AllowAdminOtpRequestOf").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.exists()) {

                            if (snapshot.child(phoneNumber).exists()) {
                                if (String.valueOf(snapshot.child(phoneNumber).getValue()).equals("1")) {

                                    pd.dismiss();
                                    Intent getOtpAndVerify = new Intent(CheckPermissionToRequestOTPActivity.this, OtpActivity.class);
                                    getOtpAndVerify.putExtra("phoneNumber", phoneNumber);
                                    getOtpAndVerify.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    startActivity(getOtpAndVerify);
                                    finish();

                                } else {
                                    pd.dismiss();
                                    errorText.setText("You don't have permission to request authentication with this number.\nContact Sys-Admin.");
                                    errorText.setVisibility(View.VISIBLE);
                                    errorText.setGravity(Gravity.CENTER);

                                }
                            } else {
                                pd.dismiss();
                                errorText.setText("You don't have permission to request authentication with this number.\nContact Sys-Admin");
                                errorText.setVisibility(View.VISIBLE);
                                errorText.setGravity(Gravity.CENTER);

                            }

                        }else{
                            pd.dismiss();
                            errorText.setText("You don't have permission to request authentication with this number.\ncontact sys-admin.");
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setGravity(Gravity.CENTER);
                        }

                    } catch (Exception e) {

                        pd.dismiss();
                        errorText.setText("You don't have permission to request authentication with this number.\ncontact sys-admin.");
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setGravity(Gravity.CENTER);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void initialize() {
        phoneNumberET=findViewById(R.id.phoneNumber);
        checkStatus=findViewById(R.id.checkOTPPermissionBtn);
        errorText=findViewById(R.id.errorMessage);
        pd= new ProgressDialog(CheckPermissionToRequestOTPActivity.this);
    }


    public boolean checkConnectivity() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;
        return connected;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}