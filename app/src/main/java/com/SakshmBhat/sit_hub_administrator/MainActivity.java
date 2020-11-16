 package com.SakshmBhat.sit_hub_administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.SakshmBhat.sit_hub_administrator.faculty_list.UpdateAndAddFaculty;

 public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     CardView uploadFeed, uploadGalleryImage, uploadEbook, editFacultydetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadFeed=findViewById(R.id.addFeedCard);
        uploadGalleryImage=findViewById(R.id.addGalleryImageCard);
        uploadEbook=findViewById(R.id.addEbookCard);
        editFacultydetails=findViewById(R.id.editFacultyDetailsCard);

        uploadFeed.setOnClickListener(this);
        uploadGalleryImage.setOnClickListener(this);
        uploadEbook.setOnClickListener(this);
        editFacultydetails.setOnClickListener(this);

    }

     @Override
     public void onClick(View v) {

        Intent intent;

        switch(v.getId()){

            case R.id.addFeedCard:

                        intent =new Intent(MainActivity.this,UploadFeed.class);
                        startActivity(intent);
                        break;

            case R.id.addGalleryImageCard:

                        intent =new Intent(MainActivity.this,UploadGalleryImage.class);
                        startActivity(intent);
                        break;

            case R.id.addEbookCard:

                       intent = new Intent(MainActivity.this,UploadEbook.class);
                       startActivity(intent);
                       break;

            case R.id.editFacultyDetailsCard:

                        intent = new Intent(MainActivity.this, UpdateAndAddFaculty.class);
                        startActivity(intent);
                        break;
        }

     }
 }