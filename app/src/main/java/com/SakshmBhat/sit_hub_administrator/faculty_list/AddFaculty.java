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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFaculty extends AppCompatActivity {

    private CircleImageView teacherImageView;
    private EditText facultyName, facultyEmail, facultyPost;
    private Spinner facultyDeptSpinner;
    private Button uploadFacultyBtn;
    private String deptSelected;

    private  final int REQ=1;
    private Bitmap bitmap=null;

    private String name,email,post,dept,downloadUrl="";

    private AlertDialog dialogBox;
    private AlertDialog.Builder dialogBuilder;

    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        teacherImageView = findViewById(R.id.teacherCircleImageClickToAddImg);
        facultyName = findViewById(R.id.teacherNameField);
        facultyEmail = findViewById(R.id.teacherEmailField);
        facultyPost = findViewById(R.id.teacherPostField);
        facultyDeptSpinner = findViewById(R.id.facultyDepartmentSpinner);
        uploadFacultyBtn = findViewById(R.id.uploadTeacherButton);
        pd = new ProgressDialog(this);


        storageReference  = FirebaseStorage.getInstance().getReference();

        String[] spinnerDeptList = new String[]{"Select Department","ISE","CSE","ECE","ME","TE","EIE","CE","IE","IEM","Maths","Physics","Chemistry","BT","MBA","Architecture","Administration and Non-teaching"};

        //Give categories listed in 'items' to the spinner(drop down)
        facultyDeptSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerDeptList));

        //If any category is selected then get that category in a string
        facultyDeptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deptSelected = facultyDeptSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        teacherImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });

        uploadFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateInput();

            }
        });


    }

    private void validateInput() {

        name = facultyName.getText().toString();
        email = facultyEmail.getText().toString();
        post = facultyPost.getText().toString();

        if(name.isEmpty()){

            facultyName.setError("Name required!");
            facultyName.requestFocus();

        }else if(email.isEmpty()){

            facultyEmail.setError("E-mail required!");
            facultyEmail.requestFocus();

        }else if(post.isEmpty()){

            facultyPost.setError("Post required!");
            facultyPost.requestFocus();

        }else if(deptSelected.equals("Select Department")){

            Toast.makeText(this, "Error: No department selected! Select one to proceed.", Toast.LENGTH_SHORT).show();
            
        }else if(bitmap==null){

           //If image is not selected confirm from user if he/she is sure?
            //First build a dialog
            dialogBuilder = new AlertDialog.Builder(AddFaculty.this);
            dialogBuilder.setTitle("Proceed without an image?");
            //Create negative and positive responses and add click listener to them.
            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialogBox.dismiss();
                    uploadFacultyAttributesMethod();

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialogBox.dismiss();

                }
            });
            //create dialog box and show it
            dialogBox = dialogBuilder.create();
            dialogBox.show();

        }else{

            uploadImageMethod();

        }

    }

    private void uploadImageMethod() {

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
        uploadTask.addOnCompleteListener(AddFaculty.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadFacultyAttributesMethod();

                                }
                            });

                        }
                    });

                }
                else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(AddFaculty.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddFaculty.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void uploadFacultyAttributesMethod(){

        pd.setMessage("Uploading faculty details...");
        pd.show();

        //Initialialize here or use two database reference variables and initialize in oncreate, to avoid single reference variable having refernce of other(previously used) node
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference = databaseReference.child("Faculty").child(deptSelected);
        //Get unique key for reference when retrieving data
        final String uniqueKey = databaseReference.push().getKey();


        //Make a Faculty Attributes Java Class object to store all attributes in one obj

        FacultyAttributes facultyAttributes = new FacultyAttributes(name,email,post,downloadUrl,uniqueKey);

        //Save faculty attributes data details in firebase

        databaseReference.child(uniqueKey).setValue(facultyAttributes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Stop the progress dialog as the upload is complete

                pd.dismiss();

                Toast.makeText(AddFaculty.this, "Faculty details  upload: Success!",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //stop the progress dialog if the upload fails

                pd.dismiss();

                Toast.makeText(AddFaculty.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                Toast.makeText(AddFaculty.this, "Try again.",Toast.LENGTH_SHORT).show();

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
            teacherImageView.setImageBitmap(bitmap);

        }

    }


}

