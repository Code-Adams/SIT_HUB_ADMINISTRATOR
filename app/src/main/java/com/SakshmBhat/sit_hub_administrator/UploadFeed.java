package com.SakshmBhat.sit_hub_administrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadFeed extends AppCompatActivity {

    private CardView uploadImage;//addImage
    private  final int REQ=1;

    private Bitmap bitmap;

    private ImageView imagePreview;

    private EditText feedTitle; //Notice title

    private Button uploadFeedBtn; //uploadnoticeButton

    private DatabaseReference databaseReference;
    private StorageReference  storageReference;

    String downloadUrl = "";// Initialise as null

    private ProgressDialog  pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_feed);

        pd = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        feedTitle =findViewById(R.id.feedTitleField);

        uploadFeedBtn = findViewById(R.id.uploadFeedButton);

        uploadImage= findViewById(R.id.uploadImageCard);
        imagePreview = findViewById(R.id.feedImagePreview);

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

                }
                else if(bitmap==null){

                    uploadDataMethod();
                }
                else{

                    uploadImageMethod();

                }

            }
        });

    }

    private void uploadDataMethod() {

      databaseReference = databaseReference.child("Feed");

      final String uniqueKey = databaseReference.push().getKey();

      String feedTitleString = feedTitle.getText().toString();

      //Getting Date
        Calendar calenderForUploadDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String uploadDate = currentDate.format(calenderForUploadDate.getTime());

      //Getting Time
        Calendar calenderForUploadTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        String uploadTime = currentTime.format(calenderForUploadTime.getTime());

      //Make a FeedData Java Class object to store all attributes in one obj

        FeedData feedData = new FeedData(feedTitleString,downloadUrl,uploadDate,uploadTime,uniqueKey);

      //Save feed data details in firebase

        databaseReference.child(uniqueKey).setValue(feedData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Stop the progress dialog as the upload is complete

                pd.dismiss();

                Toast.makeText(UploadFeed.this, "Feed upload: Success!",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //stop the progress dialog if the upload fails

                pd.dismiss();

                Toast.makeText(UploadFeed.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void uploadImageMethod() {

        //Show progress to the user as uploading
        pd.setMessage("Uploading...");
        pd.show();

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference imageFilePath;

        imageFilePath = storageReference.child("Feed").child(finalImageForUpload + "jpg");

        //Creating task to upload image

        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get path(URL) of image and store it in database
        uploadTask.addOnCompleteListener(UploadFeed.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                    Toast.makeText(UploadFeed.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(UploadFeed.this, "Try again!",Toast.LENGTH_SHORT).show();
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
}