package com.SakshmBhat.sit_hub_administrator.faculty_list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.SakshmBhat.sit_hub_administrator.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateFaculty extends AppCompatActivity {

    private CircleImageView facultyCircleImageView;
    private EditText  updatedName, updatedPost,updatedEmail;
    private Button proceedUpdationBtn,deleteFacultyBtn;
    private String oldName,oldPost,oldEmail,oldImageUrl;

    private final int REQ=1;
    private Bitmap bitmap=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        facultyCircleImageView=findViewById(R.id.clickToUpdateFacultyImage);

        updatedName=findViewById(R.id.updatedFacultyName);
        updatedPost=findViewById(R.id.updatedFacultyPost);
        updatedEmail=findViewById(R.id.updatedFacultyEmail);

        proceedUpdationBtn=findViewById(R.id.clickToProceedUpdationOfFacultyInfoBtn);
        deleteFacultyBtn=findViewById(R.id.clickToDeleteFacultyBtn);

        //Get old attributes from previous activity(Intent created in FacultyInfoAdapter)
        oldName= getIntent().getStringExtra("name");
        oldEmail= getIntent().getStringExtra("email");
        oldPost= getIntent().getStringExtra("post");
        oldImageUrl= getIntent().getStringExtra("imageUrl");

        //Set old Image
        try {
            Picasso.get().load(oldImageUrl).into(facultyCircleImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //As activity is launched show what older attributes were
        updatedName.setText(oldName);
        updatedEmail.setText(oldEmail);
        updatedPost.setText(oldEmail);

        facultyCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

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