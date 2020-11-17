package com.SakshmBhat.sit_hub_administrator.faculty_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateFacultyActivity extends AppCompatActivity {

    private CircleImageView facultyCircleImageView;
    private EditText  updatedName, updatedPost,updatedEmail;
    private Button proceedUpdationBtn,deleteFacultyBtn;
    private String oldName,oldPost,oldEmail,oldImageUrl;
    private String newName,newPost,newEmail,newImageUrl;

    private final int REQ=1;
    private Bitmap bitmap=null;

    private AlertDialog dialogBox;
    private AlertDialog.Builder dialogBuilder;

    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String downloadUrl;
    private String uniqueKey, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        facultyCircleImageView=findViewById(R.id.clickToUpdateFacultyImage);

        pd= new ProgressDialog(this);

        uniqueKey=getIntent().getStringExtra("key");
        category= getIntent().getStringExtra("category");

        updatedName=findViewById(R.id.updatedFacultyName);
        updatedPost=findViewById(R.id.updatedFacultyPost);
        updatedEmail=findViewById(R.id.updatedFacultyEmail);

        proceedUpdationBtn=findViewById(R.id.clickToProceedUpdationOfFacultyInfoBtn);
        deleteFacultyBtn=findViewById(R.id.clickToDeleteFacultyBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();

        //Get old attributes from previous activity(Intent created in FacultyInfoAdapter)
        oldName= getIntent().getStringExtra("name");
        oldEmail= getIntent().getStringExtra("email");
        oldPost= getIntent().getStringExtra("post");
        oldImageUrl= getIntent().getStringExtra("imageUrl");
        uniqueKey=getIntent().getStringExtra("key");
        newImageUrl= oldImageUrl;//new and old  made equal initially, as new one is passed to upload. But what if user does't wnat to change image or remove it and so does't click on circle image view. In that case newImageUrl will not be updated and have null value or some arbitrary value so uncertainty occurs


        //Set old Image
        try {
            Picasso.get().load(oldImageUrl).into(facultyCircleImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //As activity is launched show what older attributes were
        updatedName.setText(oldName);
        updatedEmail.setText(oldEmail);
        updatedPost.setText(oldPost);

        facultyCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if there is already an image available only then ask if user wants to remove it
                if(!oldImageUrl.isEmpty() || bitmap!=null){
                //If Circle image view is clicked, ask user if he/she wants to remove picture or add new image
                //First build a dialog
                dialogBuilder = new AlertDialog.Builder(UpdateFacultyActivity.this);
                dialogBuilder.setTitle("Choose action:");
                //Create negative and positive responses and add click listener to them.
                dialogBuilder.setPositiveButton("Add new Image", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialogBox.dismiss();
                        openGallery();

                    }
                });
                dialogBuilder.setNegativeButton("Remove existing image", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialogBox.dismiss();
                        //Set image url to be uploaded as null
                        newImageUrl="";
                        //Set faculty image as default
                        facultyCircleImageView.setImageResource(R.drawable.user);



                    }
                });
                //create dialog box and show it
                    try {
                        dialogBox = dialogBuilder.create();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialogBox.show();

            }else{
                    openGallery();
                }

            }
        });

        proceedUpdationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newName= updatedName.getText().toString();
                newPost= updatedPost.getText().toString();
                newEmail= updatedEmail.getText().toString();

                validateInput();

            }
        });

        deleteFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //If delete button  is clicked, ask user if he/she is sure removing the faculty
                    //First build a dialog
                    dialogBuilder = new AlertDialog.Builder(UpdateFacultyActivity.this);
                    dialogBuilder.setTitle("Are you sure?");
                    dialogBuilder.setMessage("Delete '"+updatedName.getText().toString()+"' permanently:");
                    //Create negative and positive responses and add click listener to them.
                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialogBox.dismiss();
                            deleteFaculty();

                        }
                    });
                    dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Do nothing just dismiss the dialog box
                            dialogBox.dismiss();


                        }
                    });
                    //create dialog box and show it
                try {
                    dialogBox = dialogBuilder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogBox.show();

            }
        });


    }

    private void deleteFaculty() {

        //Show progress to the user as uploading
        pd.setMessage("Deleting Faculty...");
        pd.show();

        databaseReference.child(category).child(uniqueKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //Stop the progress dialog as the upload is complete
                        pd.dismiss();
                        Toast.makeText(UpdateFacultyActivity.this, "Faculty deletion: Success!",Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(UpdateFacultyActivity.this, UpdateAndAddFacultyActivity.class);
                        //Prevent user from coming back on pressing back button
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //stop the progress dialog if the upload fails
                pd.dismiss();

                Toast.makeText(UpdateFacultyActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                Toast.makeText(UpdateFacultyActivity.this, "Try again.",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void validateInput() {

       if(newName.isEmpty())
       {

          updatedName.setError("Name must to proceed!");
          updatedName.requestFocus();

       }else  if(newPost.isEmpty())
       {

           updatedPost.setError("Post must to proceed!");
           updatedPost.requestFocus();

       }else  if(newEmail.isEmpty())
       {

           updatedEmail.setError("Email must to proceed!");
           updatedEmail.requestFocus();

       }else if(!isEmailValid(newEmail)){

           updatedEmail.setError("Invalid e-mail format!");
           updatedEmail.requestFocus();

       }else if(bitmap==null){

           uploadFacultyAttributesMethod(newImageUrl);

       }else{
           uploadUpdatedImage();
       }



    }

    //This function checks if email is in proper format
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void uploadUpdatedImage(){

        //Show progress to the user as uploading
        pd.setMessage("Uploading Image...");
        pd.show();

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference imageFilePath;

        imageFilePath = storageReference.child("Faculty").child(finalImageForUpload + "jpg");

        //Creating task to upload image

        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get path(URL) of image and store it in database
        uploadTask.addOnCompleteListener(UpdateFacultyActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                    pd.dismiss();
                                    uploadFacultyAttributesMethod(downloadUrl);

                                }
                            });

                        }
                    });

                }
                else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(UpdateFacultyActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(UpdateFacultyActivity.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void uploadFacultyAttributesMethod(String downloadUrl) {

        pd.setMessage("Updating faculty details...");
        pd.show();

        HashMap hashMap= new HashMap();
        hashMap.put("name",newName);
        hashMap.put("post",newPost);
        hashMap.put("email",newEmail);
        hashMap.put("imageUrl",downloadUrl);


        databaseReference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                //Stop the progress dialog as the upload is complete
                pd.dismiss();
                Toast.makeText(UpdateFacultyActivity.this, "Faculty details  upload: Success!",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(UpdateFacultyActivity.this, UpdateAndAddFacultyActivity.class);
                //Prevent user from coming back on pressing back button
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //stop the progress dialog if the upload fails
                pd.dismiss();

                Toast.makeText(UpdateFacultyActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                Toast.makeText(UpdateFacultyActivity.this, "Try again.",Toast.LENGTH_SHORT).show();

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

            //Set bitmap to show image preview in circle image view
            facultyCircleImageView.setImageBitmap(bitmap);

        }

    }
}