package com.SakshmBhat.sit_hub_administrator.feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.MainActivity;
import com.SakshmBhat.sit_hub_administrator.R;
import com.SakshmBhat.sit_hub_administrator.userAuthentication.CheckPermissionToRequestOTPActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadFeedActivity extends AppCompatActivity {

    private CardView uploadImage;//addImage
    private  final int REQ=1;

    private Bitmap bitmap;

    private ImageView imagePreview;

    private LinearLayout linkTextnUrlContainer;

    private EditText feedTitle, linkText,linkUrl; //Notice title

    private Button uploadFeedBtn,addLink,cancelLink; //uploadnoticeButton

    private DatabaseReference databaseReference,dbRef;
    private StorageReference  storageReference;

    String linkUrlStr="noLink",linkTextStr="noLink";

    String uploaderUserName, uploaderProfilePicUrl;

    String downloadUrl = "",userType,phoneNumber;// Initialise as null

    private ProgressDialog  pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_feed);

        phoneNumber=getIntent().getStringExtra("phoneNumber");
        userType=getIntent().getStringExtra("userType");

        if(!checkConnectivity()){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    UploadFeedActivity.this);
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


        pd = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Feed");

        feedTitle =findViewById(R.id.feedTitleField);
        linkText=findViewById(R.id.linkText);
        linkUrl=findViewById(R.id.linkUrl);
        addLink=findViewById(R.id.addLink);
        cancelLink=findViewById(R.id.cancelLink);
        linkTextnUrlContainer=findViewById(R.id.linkTextnUrlContainer);

        uploadFeedBtn = findViewById(R.id.uploadFeedButton);

        uploadImage= findViewById(R.id.uploadImageCard);
        imagePreview = findViewById(R.id.feedImagePreview);

        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkTextnUrlContainer.setVisibility(View.VISIBLE);
                addLink.setEnabled(false);
            }
        });
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkTextnUrlContainer.setVisibility(View.GONE);
                addLink.setEnabled(true);
                linkUrl.getText().clear();
                linkText.getText().clear();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });

        uploadFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checking if feed title is provided or not
                if(feedTitle.getText().toString().isEmpty()){

                    feedTitle.setError("Feed title required to proceed!");
                    feedTitle.requestFocus();
                }else if(linkTextnUrlContainer.getVisibility()==View.VISIBLE && linkUrl.getText().toString().trim().isEmpty()){
                    linkUrl.setError("Url is required!");
                    linkUrl.requestFocus();
                }
                else if(linkTextnUrlContainer.getVisibility()==View.VISIBLE && linkText.getText().toString().trim().isEmpty()){

                    linkText.setError("Link Text is required!");
                    linkText.requestFocus();

                }else if(linkTextnUrlContainer.getVisibility()==View.VISIBLE && linkText.getText().toString().trim().length()>30){
                    linkText.setError("Max char:30");
                    linkText.requestFocus();
                }
                else if(bitmap==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            UploadFeedActivity.this);
                    builder.setTitle("No Image selected");
                    builder.setMessage("Continue without an image?");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    pd.setMessage("Uploading...");
                                    pd.show();
                                    uploadDataMethod();
                                }
                            });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                else{

                    uploadImageMethod();

                }

            }
        });

    }


    private void uploadDataMethod() {

      dbRef=databaseReference;

      final String uniqueKey = dbRef.push().getKey();

      String feedTitleString = feedTitle.getText().toString();

      //Getting Date
        Calendar calenderForUploadDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String uploadDate = currentDate.format(calenderForUploadDate.getTime());

      //Getting Time
        Calendar calenderForUploadTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        String uploadTime = currentTime.format(calenderForUploadTime.getTime());

        if(linkTextnUrlContainer.getVisibility()==View.VISIBLE){
            linkUrlStr= linkUrl.getText().toString().trim();
            linkTextStr=linkText.getText().toString().trim();
        }else{
            linkUrlStr= "noLink";
            linkTextStr="noLink";
        }

        FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").child(phoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(userType.equals("SIT Faculty")){
                        uploaderUserName=String.valueOf(snapshot.child("name").getValue()).trim();
                        uploaderProfilePicUrl=String.valueOf(snapshot.child("imageUrl").getValue()).trim();
                    }else{
                        uploaderUserName=String.valueOf(snapshot.child("clubName").getValue()).trim();
                        uploaderProfilePicUrl=String.valueOf(snapshot.child("clubLogoUrl").getValue()).trim();

                    }

                    FeedData feedData = new FeedData(feedTitleString,downloadUrl,uploadDate,uploadTime,uniqueKey,linkUrlStr,linkTextStr,uploaderUserName,uploaderProfilePicUrl);
                    //Save feed data details in firebase

                    dbRef.child(uniqueKey).setValue(feedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            FirebaseDatabase.getInstance().getReference().child("FeedByAdmin").child(phoneNumber).child(uniqueKey).setValue(feedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    pd.dismiss();
                                    Toast.makeText(UploadFeedActivity.this, "Upload: Success!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {

                                    pd.dismiss();
                                    Toast.makeText(UploadFeedActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //stop the progress dialog if the upload fails

                            pd.dismiss();

                            Toast.makeText(UploadFeedActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {



            }
        });

    }

    private void uploadImageMethod() {

        //Show progress to the user as uploading
        pd.setMessage("Uploading...");
        pd.show();

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference imageFilePath;

        imageFilePath = storageReference.child("Feed").child(finalImageForUpload + "webp");

        //Creating task to upload image

        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get path(URL) of image and store it in database
        uploadTask.addOnCompleteListener(UploadFeedActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){

                     uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                             //Get download Url of Image
                             imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                 public void onSuccess(Uri uri) {

                                   //Save the download url of image in form of string;

                                    downloadUrl = String.valueOf(uri);
                                   //Call upload data method to upload it yo database

                                    uploadDataMethod();

                                 }
                            });

                            }
                        });

                }
                else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(UploadFeedActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(UploadFeedActivity.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void openGallery() {

        Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(getImage, REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQ && resultCode ==RESULT_OK){

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Set bitmap to show image preview
            imagePreview.setImageBitmap(bitmap);
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