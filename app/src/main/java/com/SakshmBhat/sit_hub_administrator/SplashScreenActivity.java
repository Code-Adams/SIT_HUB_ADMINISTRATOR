package com.SakshmBhat.sit_hub_administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.SakshmBhat.sit_hub_administrator.userAuthentication.CheckPermissionToRequestOTPActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(checkConnectivity()){

            try{
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreenActivity.this, CheckPermissionToRequestOTPActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }
                    }, 3000);
                }else{

                      phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

                    FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){
                                if(snapshot.child(phoneNumber).exists()){

                                    try{if(String.valueOf(snapshot.child(phoneNumber).child("accessToken").getValue()).equals("0")){

                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                SplashScreenActivity.this);
                                        builder.setTitle("Privileges Revoked!");
                                        builder.setCancelable(false);
                                        builder.setMessage("Admin access for \"" +phoneNumber+"\" has been revoked.\nContact sys-admin");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        finish();
                                                    }
                                                });
                                        builder.show();

                                    }
                                    else if(String.valueOf(snapshot.child(phoneNumber).child("accessToken").getValue()).equals("1")){

                                        new Handler().postDelayed(new Runnable(){
                                            @Override
                                            public void run() {
                                                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.putExtra("phoneNumber",phoneNumber);
                                                startActivity(i);
                                                finish();
                                            }
                                        }, 3000);


                                    }
                                    else{

                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                SplashScreenActivity.this);
                                        builder.setCancelable(false);
                                        builder.setTitle("Access Token Error!");
                                        builder.setMessage("Unknown access token encountered for \""+ phoneNumber +"\".\nContact sys-admin");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        finish();
                                                    }
                                                });
                                        builder.show();

                                    }
                                    }catch (Exception e){
                                        e.printStackTrace();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                SplashScreenActivity.this);
                                        builder.setCancelable(false);
                                        builder.setTitle("Access Token Error!");
                                        builder.setMessage("Unknown access token encountered for \""+ phoneNumber +"\".\nContact sys-admin");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        finish();
                                                    }
                                                });
                                        builder.show();

                                    }


                                }else{

                                    new Handler().postDelayed(new Runnable(){
                                        @Override
                                        public void run() {
                                            Intent intent=new Intent(SplashScreenActivity.this,GetAndSetAdminDetailsActivity.class);
                                            intent.putExtra("phoneNumber",phoneNumber);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }, 3000);
                                }

                            }else{

                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        SplashScreenActivity.this);
                                builder.setTitle("Database Error!");
                                builder.setCancelable(false);
                                builder.setMessage("Admin app access data not found.\nContact sys-admin.");
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }catch (Exception e){

            }


        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    SplashScreenActivity.this);
            builder.setCancelable(false);
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

        }


    }

    public boolean checkConnectivity() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}