package com.SakshmBhat.sit_hub_administrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadEbookActivity extends AppCompatActivity {

    private CardView selectEbook;
    private  final int REQ=1;

    private Uri ebookUri;
    private EditText ebookTitle;
    private Button uploadEbookBtn;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    String downloadUrl = "",phoneNumber;

    private ProgressDialog pd;

    private TextView nameOfTheFileSelected;

    private String ebookName, ebookTitleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ebook);

        phoneNumber=getIntent().getStringExtra("phoneNumber");

        nameOfTheFileSelected = findViewById(R.id.selectedFileName);

        pd = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        ebookTitle =findViewById(R.id.ebookTitleField);

        uploadEbookBtn = findViewById(R.id.uploadEbookButton);

        selectEbook= findViewById(R.id.uploadEbookCard);


        selectEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });

        uploadEbookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ebookTitleText= ebookTitle.getText().toString();


            if(ebookTitleText.isEmpty()){

                ebookTitle.setError("Error: Title Required!");
                ebookTitle.requestFocus();


            }else if(ebookUri == null){

                Toast.makeText(UploadEbookActivity.this,"Please select an ebook.",Toast.LENGTH_SHORT).show();

                }else{

                uploadEbookMethod();

            }

            }
        });

    }

    private void uploadEbookMethod() {

        pd.setTitle("Please wait.");
        pd.setMessage("Uploading...");
        pd.setCancelable(false);
        pd.show();

        StorageReference storageReferencetwo = storageReference.child("Ebook/"+ebookName+"-"+ System.currentTimeMillis()+".pdf");

        storageReferencetwo.putFile(ebookUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Get the download Url now as the pdf is uploaded and save that in database
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                //Stop program execution till download url is not fetched
                while(!uriTask.isComplete());

                Uri uri = uriTask.getResult();
                //New that we have download url. Save it in database

                uploadDataMethod(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(UploadEbookActivity.this, "Opps! Something went wrong.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void uploadDataMethod(String eBookDownloadUrl) {

        String uniqueKey = databaseReference.child("Ebook").push().getKey();

        //Hashmap to store data
        HashMap ebook = new HashMap();
        ebook.put("ebookTitle",ebookTitleText);
        ebook.put("ebookUrl", eBookDownloadUrl);
        ebook.put("uploader",phoneNumber);

        //Uploading ebook Url along with its Title
        databaseReference.child("Ebook").child(uniqueKey).setValue(ebook).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                pd.dismiss();

                Toast.makeText(UploadEbookActivity.this, "Ebook upload successful", Toast.LENGTH_SHORT).show();
                nameOfTheFileSelected.setText("");
                ebookTitle.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();

                Toast.makeText(UploadEbookActivity.this, "Ebook upload failed.", Toast.LENGTH_SHORT).show();
                Toast.makeText(UploadEbookActivity.this, "Try again.", Toast.LENGTH_SHORT).show();
                
            }
        });
    }

    private void openGallery() {

          Intent intent = new Intent();
//
//        intent.setType("*/*");// or intent.setType("*"); for some devices
//
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select an Ebook(pdf,ppt,docs)"),REQ);

        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select Your .pdf File"), REQ);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(UploadEbookActivity.this, "Please Install a File Manager",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQ && resultCode ==RESULT_OK){

           ebookUri = data.getData();

            if(ebookUri.toString().startsWith("content://")){

                Cursor cursor = null;
                try {
                    cursor = UploadEbookActivity.this.getContentResolver().query(ebookUri,null,null,null,null);

                    if (cursor!=null && cursor.moveToFirst()){

                        ebookName =cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (ebookUri.toString().startsWith("file://")){

                ebookName= new File(ebookUri.toString()).getName();

            }

            nameOfTheFileSelected.setText(ebookName);

        }

    }
}
