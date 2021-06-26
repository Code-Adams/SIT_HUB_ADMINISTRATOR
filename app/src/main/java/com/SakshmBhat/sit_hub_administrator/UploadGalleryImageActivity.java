package com.SakshmBhat.sit_hub_administrator;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class UploadGalleryImageActivity extends AppCompatActivity {

    private Spinner imageCategory;
    private CardView selectImage;
    private Button uploadImageBtn;
    private ImageView imagePreview;

    private String categorySelected;

    private final int REQ = 1;

    private Bitmap bitmap;

    ProgressDialog pd;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    
    String downloadUrl,clubName=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gallery_image);
        initialize();

        //If any category is selected then get that category in a string
        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = imageCategory.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });
        
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(bitmap == null){
                    
                    Toast.makeText(UploadGalleryImageActivity.this, "Error: No image selected!",Toast.LENGTH_SHORT).show();
                    
                }//Now check if user has selected a category from drop down
                else if(imageCategory.getVisibility()==View.VISIBLE && categorySelected.equals("Select Category")){

                    Toast.makeText(UploadGalleryImageActivity.this, "Category required for upload.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(UploadGalleryImageActivity.this, "Please Select a category",Toast.LENGTH_SHORT).show();
                    
                }else{
                    
                    pd.setMessage("Uploading...");
                    pd.show();
                    uploadImageMethod();
                }
                
            }
        });



    }

    private void initialize() {

        selectImage = findViewById(R.id.uploadGalleryImage);
        imageCategory = findViewById(R.id.imageCategorySpinner);
        uploadImageBtn= findViewById(R.id.uploadGalleryImageButton);
        imagePreview = findViewById(R.id.galleryImagePreview);

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("Gallery");

        //Set progress dialog for this activity
        pd = new ProgressDialog(UploadGalleryImageActivity.this);

        clubName=getIntent().getStringExtra("clubName");

        if(clubName.isEmpty()) {
            //List all categories to be given in the drop down in a string variable
            String[] items = new String[]{"Select Category", "Infrastructure", "Incubation Cell", "Research", "Sports", "Campus"};

            //Give categories listed in 'items' to the spinner(drop down)
            imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));
        }else{
            imageCategory.setVisibility(View.GONE);
        }

    }

    private void uploadImageMethod() {

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference imageFilePath;

        if(imageCategory.getVisibility()==View.VISIBLE) {
            imageFilePath = storageReference.child("college").child(categorySelected).child(finalImageForUpload + "webp");
        }else{
            imageFilePath=storageReference.child("clubs").child(getIntent().getStringExtra("clubName").trim()).child(finalImageForUpload + "webp");
        }

        //Creating task to upload image
        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get path(URL) of image and store it in database
        uploadTask.addOnCompleteListener(UploadGalleryImageActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Get download Url of image
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

                } else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(UploadGalleryImageActivity.this, "Oops! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(UploadGalleryImageActivity.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void uploadDataMethod() {

        //Save image Url to Database

        if(imageCategory.getVisibility()==View.VISIBLE){

            databaseReference=FirebaseDatabase.getInstance().getReference().child("Gallery").child(categorySelected);

        }else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Gallery").child("clubs").child(clubName);
        }
        final String uniqueKey = databaseReference.push().getKey();


        databaseReference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Stop the progress dialog as the upload is complete
                pd.dismiss();

                Toast.makeText(UploadGalleryImageActivity.this, "Image upload: Success!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadGalleryImageActivity.this,MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //stop the progress dialog if the upload fails
                pd.dismiss();

                Toast.makeText(UploadGalleryImageActivity.this, "Oops! Something went wrong.",Toast.LENGTH_SHORT).show();
                Toast.makeText(UploadGalleryImageActivity.this, "Try Again",Toast.LENGTH_SHORT).show();

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