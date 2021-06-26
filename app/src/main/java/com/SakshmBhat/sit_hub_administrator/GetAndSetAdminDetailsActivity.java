package com.SakshmBhat.sit_hub_administrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetAndSetAdminDetailsActivity extends AppCompatActivity {

    private Spinner  adminUserType;
    private LinearLayout circleImageViewsContainer, userImageContainer, clubLogoImageContainer;
    private EditText userName,ClubName,clubDescription;
    private Button submitButton;
    private CircleImageView userImage, clubLogoImage;
    private ImageView userIdProof;
    String userTypeSelected,userImageDownloadUrl,clubLogoImageDownloadUrl,idProofDownloadUrl;
    private final int userImageRequestCode=101,clubLogoImageRequestCode=102,idProofImageRequestCode=103;
    private Bitmap userImageBitmap,clubImageBitmap,idProofBitmap;
    private ProgressDialog pd;
    private TextView idProofText,userImageUrlTV,clubLogoImageUrlTV,idProofImageUrlTV;
    private ClubData clubData;
    private StudentClubUserData studentClubUserData;
    private SitOfficialAdminUserData sitOfficialAdminUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_and_set_admin_details);

        initialize();
        setSpinner();
        //Set item selected listener on spinner
        adminUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userTypeSelected = adminUserType.getSelectedItem().toString();
                showLayoutAccordingToTheUsertype(userTypeSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userTypeSelected.equals("SIT Faculty")) {
                    getDataAsSitOfficialAdmin();
                }
                if(userTypeSelected.equals("Std. Club Representative")){
                    getDataAsStudentClubUser();
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(userImageRequestCode);
            }
        });
        clubLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(clubLogoImageRequestCode);
            }
        });
        userIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(idProofImageRequestCode);
            }
        });

    }


    private void getDataAsStudentClubUser() {

        if(userImageBitmap==null){

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    GetAndSetAdminDetailsActivity.this);
            builder.setTitle("Missing Image");
            builder.setMessage("Please select a user Image");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            builder.show();
        }else if(clubImageBitmap==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    GetAndSetAdminDetailsActivity.this);
            builder.setTitle("Missing Image");
            builder.setMessage("Please select a Club Logo.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            builder.show();
        }
        else if (userName.getText().toString().trim().isEmpty()){

            userName.setError("Field Required!");
            userName.requestFocus();
        }
        else if(userName.getText().toString().trim().length()>20){
            userName.setError("Max char: 20");
            userName.requestFocus();
        }
        else if(ClubName.getText().toString().trim().isEmpty()){
            ClubName.setError("Field Required!");
            ClubName.requestFocus();
        }else if(ClubName.getText().toString().trim().length()>15){

            ClubName.setError("Max char: 15");
            ClubName.requestFocus();

        }else if(clubDescription.getText().toString().trim().isEmpty()){
            clubDescription.setError("Required Field");
            clubDescription.requestFocus();
        }
        else if(idProofBitmap==null){

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    GetAndSetAdminDetailsActivity.this);
            builder.setTitle("Missing Image");
            builder.setMessage("Please select an ID proof(College ID)");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            builder.show();

        }
        else {

            pd.setMessage("Uploading...");
            pd.setCancelable(false);
            pd.show();

             uploadUserImage(userImageBitmap,idProofBitmap,clubImageBitmap);
//            StudentClubUserData studentClubUserData = new StudentClubUserData("0", userName.getText().toString().trim(), ClubName.getText().toString().trim(), userImageDownloadUrl, "Student Club", idProofDownloadUrl,clubLogoImageDownloadUrl,phoneNumber);
//
//            FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").child(phoneNumber).setValue(studentClubUserData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "User Data upload: Success", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull  Exception e) {
//                    pd.dismiss();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(
//                            GetAndSetAdminDetailsActivity.this);
//                    builder.setTitle("Upload Failure");
//                    builder.setMessage("Failed to upload details.\nCheck data connection.\nContact Sys-Admin.");
//                    builder.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                }
//                            });
//                    builder.show();
//                }
//            });
//
//            ClubData clubData = new ClubData(ClubName.getText().toString().trim(),clubDescription.getText().toString().trim(),clubLogoImageDownloadUrl,"0");
//
//            FirebaseDatabase.getInstance().getReference().child("ClubsOfSIT").child(ClubName.getText().toString().trim()).setValue(clubData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Club Data upload: Success", Toast.LENGTH_SHORT).show();
//
//                    Handler h =new Handler() ;
//                    h.postDelayed(new Runnable() {
//                        public void run() {
//                            //put your code here
//                            pd.dismiss();
//                            Intent intent= new Intent(GetAndSetAdminDetailsActivity.this,MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                    }, 4000);
//
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull  Exception e) {
//                    pd.dismiss();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(
//                            GetAndSetAdminDetailsActivity.this);
//                    builder.setTitle("Upload Failure");
//                    builder.setMessage("Failed to upload details.\nCheck data connection.\nContact Sys-Admin.");
//                    builder.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                }
//                            });
//                    builder.show();
//                }
//            });
        }

    }

    private void getDataAsSitOfficialAdmin() {

        if(userImageBitmap==null){

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    GetAndSetAdminDetailsActivity.this);
            builder.setTitle("Missing Image");
            builder.setMessage("Please select a user Image");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            builder.show();
        }
        else if (userName.getText().toString().trim().isEmpty()){

             userName.setError("Field Required!");
             userName.requestFocus();
        }else if(userName.getText().toString().trim().length()>20){
            userName.setError("Max char: 20");
            userName.requestFocus();
        }
        else if(idProofBitmap==null){

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    GetAndSetAdminDetailsActivity.this);
            builder.setTitle("Missing Image");
            builder.setMessage("Please select an ID proof(College ID)");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            builder.show();

        }
        else {

            pd.setMessage("Uploading...");
            pd.setCancelable(false);
            pd.show();
            uploadUserImage(userImageBitmap,idProofBitmap,clubImageBitmap);

//            sitOfficialAdminUserData = new SitOfficialAdminUserData("0", userName.getText().toString().trim(), userImageUrl, "SIT Faculty",  idProofUrl,phoneNumber);
//
//            FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").child(phoneNumber).setValue(sitOfficialAdminUserData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Handler h =new Handler() ;
//                    h.postDelayed(new Runnable() {
//                        public void run() {
//                            //put your code here
//                            pd.dismiss();
//                            Toast.makeText(GetAndSetAdminDetailsActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                            Intent intent= new Intent(GetAndSetAdminDetailsActivity.this,MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                    }, 4000);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull  Exception e) {
//                    pd.dismiss();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(
//                            GetAndSetAdminDetailsActivity.this);
//                    builder.setTitle("Upload Failure");
//                    builder.setMessage("Failed to upload details.\nCheck data connection.\nContact Sys-Admin.");
//                    builder.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                }
//                            });
//                    builder.show();
//                }
//            });
        }

    }


    private void uploadUserImage(Bitmap userImage,Bitmap idProofImage,Bitmap clubImage) {

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        userImage.compress(Bitmap.CompressFormat.WEBP, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference  imageFilePath= FirebaseStorage.getInstance().getReference().child("AdminProfile").child("userImage").child(userName.getText().toString() + "webp");

        //Creating task to upload image
        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get (URL) of image and store it in database
        uploadTask.addOnCompleteListener(GetAndSetAdminDetailsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadIdProofImage(idProofImage,clubImage,String.valueOf(uri));

                                    }

                            });

                        }
                    });

                } else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void uploadIdProofImage(Bitmap idProofImage, Bitmap clubImage, String userImageUrl){


        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        idProofImage.compress(Bitmap.CompressFormat.WEBP, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference imageFilePath= FirebaseStorage.getInstance().getReference().child("AdminProfile").child("idProof").child(userName.getText().toString() + "webp");

        //Creating task to upload image
        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get (URL) of image and store it in database
        uploadTask.addOnCompleteListener(GetAndSetAdminDetailsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    if(clubImage!=null) {
                                        uploadClubLogoImage(clubImage, userImageUrl, String.valueOf(uri));
                                    }else{

                                        uploadSitOfficialAdminUserData(userImageUrl,String.valueOf(uri));

                                    }

                                }

                            });

                        }
                    });

                } else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }


    private void uploadClubLogoImage(Bitmap clubImage,String userImageUrl,String idProofImageUrl){

        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

        //Compress image before upload
        clubImage.compress(Bitmap.CompressFormat.WEBP, 50, BAOS);
        byte[] finalImageForUpload =BAOS.toByteArray();

        //Creating file path
        final StorageReference  imageFilePath= FirebaseStorage.getInstance().getReference().child("AdminProfile").child("clubLogo").child(ClubName.getText().toString() + "webp");

        //Creating task to upload image
        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        //add complete task listener to get (URL) of image and store it in database
        uploadTask.addOnCompleteListener(GetAndSetAdminDetailsActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                        uploadStudentClubUserAndClubData(userImageUrl,idProofImageUrl,String.valueOf(uri));

                                    }

                            });

                        }
                    });

                } else{

                    //Stop showing upload progress with progress dialog and show failure toast

                    pd.dismiss();

                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(GetAndSetAdminDetailsActivity.this, "Try again!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void uploadStudentClubUserAndClubData(String userImageUrl, String idProofImageUrl, String clubLogoImageUrl) {

        String phoneNumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        uploadUserImage(userImageBitmap,idProofBitmap,clubImageBitmap);

        StudentClubUserData studentClubUserData = new StudentClubUserData("0", userName.getText().toString().trim(), ClubName.getText().toString().trim(), userImageUrl, "Student Club", idProofImageUrl,clubLogoImageUrl,phoneNumber);

        FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").child(phoneNumber).setValue(studentClubUserData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(GetAndSetAdminDetailsActivity.this, "User Data upload: Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                pd.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        GetAndSetAdminDetailsActivity.this);
                builder.setTitle("Upload Failure");
                builder.setMessage("Failed to upload details.\nCheck data connection.\nContact Sys-Admin.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.show();
            }
        });

        ClubData clubData = new ClubData(ClubName.getText().toString().trim(),clubDescription.getText().toString().trim(),clubLogoImageUrl,"0");

        FirebaseDatabase.getInstance().getReference().child("ClubsOfSIT").child(ClubName.getText().toString().trim()).setValue(clubData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(GetAndSetAdminDetailsActivity.this, "Club Data upload: Success", Toast.LENGTH_SHORT).show();

                Handler h =new Handler() ;
                h.postDelayed(new Runnable() {
                    public void run() {
                        //put your code here
                        pd.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GetAndSetAdminDetailsActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle("Upload Success");
                        builder.setMessage("Reopen app now.");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }

                }, 4000);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                pd.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        GetAndSetAdminDetailsActivity.this);
                builder.setTitle("Upload Failure");
                builder.setMessage("Failed to upload details.\nCheck data connection.\nContact Sys-Admin.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.show();
            }
        });

    }


    private void uploadSitOfficialAdminUserData(String userImageUrl, String idProofUrl) {

        String phoneNumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

           sitOfficialAdminUserData = new SitOfficialAdminUserData("0", userName.getText().toString().trim(), userImageUrl, "SIT Faculty",  idProofUrl,phoneNumber);


            FirebaseDatabase.getInstance().getReference().child("AdminAppAccess").child(phoneNumber).setValue(sitOfficialAdminUserData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Handler h =new Handler() ;
                    h.postDelayed(new Runnable() {
                        public void run() {
                            //put your code here
                            pd.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    GetAndSetAdminDetailsActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle("Upload Success");
                            builder.setMessage("Reopen app now.");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            finish();
                                        }
                                    });
                            builder.show();

                        }

                    }, 4000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    pd.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GetAndSetAdminDetailsActivity.this);
                    builder.setTitle("Upload Failure");
                    builder.setMessage("Failed to upload details.\nCheck data connection.\nContact Sys-Admin.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    builder.show();
                }
            });

    }


    private void showLayoutAccordingToTheUsertype(String userTypeSelected) {

      if(!userTypeSelected.isEmpty()){

           if(userTypeSelected.equals("SIT Faculty")){

                circleImageViewsContainer.setVisibility(View.VISIBLE);
                clubLogoImageContainer.setVisibility(View.GONE);
                userName.setVisibility(View.VISIBLE);
                userIdProof.setVisibility(View.VISIBLE);
                idProofText.setVisibility(View.VISIBLE);

               ClubName.setVisibility(View.GONE);
               clubDescription.setVisibility(View.GONE);

                submitButton.setVisibility(View.VISIBLE);

          }
           else if(userTypeSelected.equals("Std. Club Representative")){

               circleImageViewsContainer.setVisibility(View.VISIBLE);
               clubLogoImageContainer.setVisibility(View.VISIBLE);
               userName.setVisibility(View.VISIBLE);
               userIdProof.setVisibility(View.VISIBLE);
               idProofText.setVisibility(View.VISIBLE);

               ClubName.setVisibility(View.VISIBLE);
               clubDescription.setVisibility(View.VISIBLE);

               submitButton.setVisibility(View.VISIBLE);

           }else{

               circleImageViewsContainer.setVisibility(View.GONE);
               clubLogoImageContainer.setVisibility(View.GONE);
               userName.setVisibility(View.GONE);
               userIdProof.setVisibility(View.GONE);
               idProofText.setVisibility(View.GONE);

               ClubName.setVisibility(View.GONE);
               clubDescription.setVisibility(View.GONE);

               submitButton.setVisibility(View.GONE);

           }

       }

    }

    private void setSpinner() {

        String[] spinnerItems = new String[]{"Select User Type","SIT Faculty","Std. Club Representative"};
        adminUserType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerItems));

    }

    private void initialize() {

        adminUserType=findViewById(R.id.adminTypeSelectorSpinner);

        circleImageViewsContainer=findViewById(R.id.circleImageContainer);
        circleImageViewsContainer.setVisibility(View.GONE);

        userImageContainer=findViewById(R.id.userImageContainer);
        clubLogoImageContainer=findViewById(R.id.clubLogoImageContainer);

        userImage=findViewById(R.id.adminImage);
        clubLogoImage=findViewById(R.id.clubLogo);

        userIdProof=findViewById(R.id.idProofImage);
        userIdProof.setVisibility(View.GONE);

        idProofText=findViewById(R.id.idProofText);
        idProofText.setVisibility(View.GONE);

        userName=findViewById(R.id.userFullName);
        userName.setVisibility(View.GONE);

        ClubName=findViewById(R.id.clubName);
        ClubName.setVisibility(View.GONE);

        clubDescription=findViewById(R.id.clubDescription);
        clubDescription.setVisibility(View.GONE);

        submitButton=findViewById(R.id.submitBtn);
        submitButton.setVisibility(View.GONE);

        userImageUrlTV=findViewById(R.id.userImageUrlTV);
        clubLogoImageUrlTV=findViewById(R.id.clubLogoImageUrlTV);
        idProofImageUrlTV=findViewById(R.id.idProofImageUrlTV);

        pd= new ProgressDialog(GetAndSetAdminDetailsActivity.this);



    }
    private void openGallery(int RequestCode) {

        Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(getImage, RequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== userImageRequestCode && resultCode ==RESULT_OK){

            Uri uri = data.getData();

            try {
                userImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Set bitmap to show image preview
            userImage.setImageBitmap(userImageBitmap);
        }
        if(requestCode== clubLogoImageRequestCode && resultCode ==RESULT_OK){

            Uri uri = data.getData();

            try {
                clubImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Set bitmap to show image preview
            clubLogoImage.setImageBitmap(clubImageBitmap);
        }
        if(requestCode== idProofImageRequestCode && resultCode ==RESULT_OK){

            Uri uri = data.getData();

            try {
                idProofBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Set bitmap to show image preview
            userIdProof.setImageBitmap(idProofBitmap);
        }

    }

}