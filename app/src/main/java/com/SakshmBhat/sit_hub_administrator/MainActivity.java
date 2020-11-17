 package com.SakshmBhat.sit_hub_administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.faculty_list.UpdateAndAddFacultyActivity;
import com.SakshmBhat.sit_hub_administrator.feed.DeleteFeedActivity;
import com.SakshmBhat.sit_hub_administrator.feed.UploadFeedActivity;

 public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     CardView uploadFeed, uploadGalleryImage, uploadEbook, editFacultydetails,deleteFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadFeed=findViewById(R.id.addFeedCard);
        uploadGalleryImage=findViewById(R.id.addGalleryImageCard);
        uploadEbook=findViewById(R.id.addEbookCard);
        editFacultydetails=findViewById(R.id.editFacultyDetailsCard);
        deleteFeed=findViewById(R.id.deleteFeedCard);

        uploadFeed.setOnClickListener(this);
        uploadGalleryImage.setOnClickListener(this);
        uploadEbook.setOnClickListener(this);
        editFacultydetails.setOnClickListener(this);
        deleteFeed.setOnClickListener(this);


    }

     @Override
     public void onClick(View v) {

        Intent intent;

        switch(v.getId()){

            case R.id.addFeedCard:

                        intent =new Intent(MainActivity.this, UploadFeedActivity.class);
                        startActivity(intent);
                        break;

            case R.id.addGalleryImageCard:

                        intent =new Intent(MainActivity.this, UploadGalleryImageActivity.class);
                        startActivity(intent);
                        break;

            case R.id.addEbookCard:

                       intent = new Intent(MainActivity.this, UploadEbookActivity.class);
                       startActivity(intent);
                       break;

            case R.id.editFacultyDetailsCard:

                        intent = new Intent(MainActivity.this, UpdateAndAddFacultyActivity.class);
                        startActivity(intent);
                        break;

            case R.id.deleteFeedCard:

                intent = new Intent(MainActivity.this, DeleteFeedActivity.class);
                startActivity(intent);
                break;
        }

     }
 }