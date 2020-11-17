package com.SakshmBhat.sit_hub_administrator.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteFeedActivity extends AppCompatActivity {

    private RecyclerView  feedRecycler;
    private ProgressBar progressBar;
    private ArrayList<FeedData> list;
    private FeedAdapter feedAdapter;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_feed);

        feedRecycler=findViewById(R.id.deleteFeedRecycler);
        progressBar=findViewById(R.id.progressBar);
        databaseReference=FirebaseDatabase.getInstance().getReference();

        feedRecycler.setLayoutManager(new LinearLayoutManager(this));
        feedRecycler.setHasFixedSize(true);

        getFeed();
    }

    private void getFeed() {

        databaseReference.child("Feed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    FeedData feedData= dataSnapshot.getValue(FeedData.class);
                    list.add(feedData);

                }

                feedAdapter = new FeedAdapter(DeleteFeedActivity.this,list);
                //notify the adapter that new data is available so that adapter can reset it
                feedAdapter.notifyDataSetChanged();
               //As recycler is reset disable progress bar
                progressBar.setVisibility(View.GONE);
                feedRecycler.setAdapter(feedAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //If Deletion fails first stop progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteFeedActivity.this, "Operation Failed: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}