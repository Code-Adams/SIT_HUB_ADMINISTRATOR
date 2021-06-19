package com.SakshmBhat.sit_hub_administrator.userAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.SakshmBhat.sit_hub_administrator.MainActivity;
import com.SakshmBhat.sit_hub_administrator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckPermissionToRequestOTPActivity extends AppCompatActivity {

    private EditText phoneNumberET;
    private Button checkStatus;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission_to_request_otpactivity);

        initialize();
        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnectivity()){
                    if(phoneNumberET.getText().toString().trim().length()<11 && phoneNumberET.getText().toString().trim().length()>9) {
                        checkOTPPermissionStatus();
                    }else{

                        errorText.setText("Phone Number must be ten digits!");
                        errorText.setVisibility(View.VISIBLE);
                        phoneNumberET.setError("Wrong Format");
                        phoneNumberET.requestFocus();

                    }
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            CheckPermissionToRequestOTPActivity.this);
                    builder.setTitle("No Data Connection");
                    builder.setMessage("Check your Internet Connection and try again!!");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            });
                    builder.show();

                }
            }
        });

    }

    private void checkOTPPermissionStatus() {

        final String phoneNumber= phoneNumberET.getText().toString().trim();

        FirebaseDatabase.getInstance().getReference().child("AllowAdminOtpRequestOf").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
               try{

                   if(snapshot.exists()){

                       if (snapshot.child(phoneNumber).exists()){
                           if(snapshot.child(phoneNumber).getValue().toString().equals("1")){

                               Intent getOtpAndVerify = new Intent(CheckPermissionToRequestOTPActivity.this, OtpActivity.class);
                               getOtpAndVerify.putExtra("phoneNumber",phoneNumber);
                               startActivity(getOtpAndVerify);

                           }else{
                               errorText.setText("You don't have permission to request authentication with this number.\nContact Sys-Admin.");

                           }
                       }else{
                           errorText.setText("You don't have permission to request authentication with this number.\nContact Sys-Admin");

                       }

                   }

               }catch (Exception e){

                   errorText.setText("You don't have permission to request authentication with this number.\nContact Sys-Admin");

               }


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

    private void initialize() {
        phoneNumberET=findViewById(R.id.phoneNumber);
        checkStatus=findViewById(R.id.checkOTPPermissionBtn);
        errorText=findViewById(R.id.errorMessage);
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

}