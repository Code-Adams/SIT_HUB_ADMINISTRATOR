 package com.SakshmBhat.sit_hub_administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.faculty_list.UpdateAndAddFacultyActivity;
import com.SakshmBhat.sit_hub_administrator.feed.DeleteFeedActivity;
import com.SakshmBhat.sit_hub_administrator.feed.UploadFeedActivity;
import com.SakshmBhat.sit_hub_administrator.userAuthentication.CheckPermissionToRequestOTPActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

 public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     CardView uploadFeed, uploadGalleryImage, uploadEbook, editFacultydetails,deleteFeed;
     LinearLayout mainActivity,hideForStudent;
     String phoneNumber,userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideForStudent=findViewById(R.id.hideForStudent);
        mainActivity=findViewById(R.id.mainActivity);

        phoneNumber=getIntent().getStringExtra("phoneNumber");
        Toast.makeText(MainActivity.this, phoneNumber, Toast.LENGTH_SHORT).show();

      if(checkConnectivity()){
          if(FirebaseAuth.getInstance().getCurrentUser()!=null){

           FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                   if(snapshot.exists()){
                       if(snapshot.child(phoneNumber).exists()){

                           try{
                               if(String.valueOf(snapshot.child(phoneNumber).child("accessToken").getValue()).equals("0")){

                               AlertDialog.Builder builder = new AlertDialog.Builder(
                                       MainActivity.this);
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

                                   userType=String.valueOf(snapshot.child(phoneNumber).child("userType").getValue());
                                   if(userType.equals("Student Club")){
                                      hideForStudent.setVisibility(View.GONE);
                                   }
                                   mainActivity.setVisibility(View.VISIBLE);
                                   initialize();
                           }
                           else{

                               AlertDialog.Builder builder = new AlertDialog.Builder(
                                       MainActivity.this);
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
                                       MainActivity.this);
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


                       }
                       else{
                           Intent intent=new Intent(MainActivity.this,GetAndSetAdminDetailsActivity.class);
                           intent.putExtra("phoneNumber",phoneNumber);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           finish();
                       }

                   }
                   else{
                       AlertDialog.Builder builder = new AlertDialog.Builder(
                               MainActivity.this);
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
               public void onCancelled(@NonNull  DatabaseError error) {

                   AlertDialog.Builder builder = new AlertDialog.Builder(
                           MainActivity.this);
                   builder.setTitle("Database Error!");
                   builder.setCancelable(false);
                   builder.setMessage("Read request failed.\nContact sys-admin.");
                   builder.setPositiveButton("OK",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog,
                                                   int which) {
                                   finish();
                               }
                           });
                   builder.show();

               }
           });

       }
          else{

              AlertDialog.Builder builder = new AlertDialog.Builder(
                      MainActivity.this);
              builder.setTitle("Logged out!");
              builder.setCancelable(false);
              builder.setMessage("You have been logged out.");
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
      else{
          AlertDialog.Builder builder = new AlertDialog.Builder(
                  MainActivity.this);
          builder.setTitle("No Internet");
          builder.setCancelable(false);
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

     private void initialize() {

         uploadFeed=findViewById(R.id.addFeedCard);
         uploadGalleryImage=findViewById(R.id.addGalleryImageCard);
         uploadEbook=findViewById(R.id.addEbookCard);
         editFacultydetails=findViewById(R.id.editFacultyDetailsCard);
         deleteFeed=findViewById(R.id.deleteFeedCard);
         mainActivity=findViewById(R.id.mainActivity);

         uploadFeed.setOnClickListener(MainActivity.this);
         uploadGalleryImage.setOnClickListener(MainActivity.this);
         uploadEbook.setOnClickListener(MainActivity.this);
         editFacultydetails.setOnClickListener(MainActivity.this);
         deleteFeed.setOnClickListener(MainActivity.this);

     }

     @Override
     public void onClick(View v) {

        Intent intent;

        switch(v.getId()){

            case R.id.addFeedCard:

                        intent =new Intent(MainActivity.this, UploadFeedActivity.class);
                        intent.putExtra("userType",userType);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                        break;

            case R.id.addGalleryImageCard:
                        intent =new Intent(MainActivity.this, UploadGalleryImageActivity.class);
                        intent.putExtra("userType",userType);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                        break;

            case R.id.addEbookCard:
                       intent = new Intent(MainActivity.this, UploadEbookActivity.class);
                       intent.putExtra("userType",userType);
                       intent.putExtra("phoneNumber",phoneNumber);
                       startActivity(intent);
                       break;

            case R.id.editFacultyDetailsCard:
                        intent = new Intent(MainActivity.this, UpdateAndAddFacultyActivity.class);
                        intent.putExtra("userType",userType);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                        break;

            case R.id.deleteFeedCard:
                intent = new Intent(MainActivity.this, DeleteFeedActivity.class);
                intent.putExtra("userType",userType);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
                break;
        }

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