package com.SakshmBhat.sit_hub_administrator.feed;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.SakshmBhat.sit_hub_administrator.R;
import com.SakshmBhat.sit_hub_administrator.faculty_list.UpdateFacultyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.feedViewAdapter> {

    private Context context;
    private ArrayList<FeedData> list;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialogBox;

    public FeedAdapter(Context passedContext, ArrayList<FeedData> passedList) {
        this.context = passedContext;
        this.list = passedList;
    }

    @NonNull
    @Override
    public feedViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feed_item_layout_card,parent,false);

        return new feedViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull feedViewAdapter holder, final int position) {

        final FeedData feedData = list.get(position);

        //save title in title text view in card
        holder.feedTitle.setText(feedData.getTitle());
        //Set Image in card
        try {
             if(feedData.getImage()!=null)
            Picasso.get().load(feedData.getImage()).into(holder.feedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If delete button  is clicked, ask user if he/she is sure removing the faculty
                //First build a dialog
                dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle("Are you sure?");
                dialogBuilder.setMessage("Delete feed permanently:");
                //Create negative and positive responses and add click listener to them.
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialogBox.dismiss();

                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Feed").child(feedData.getKey());

                        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(context, "Feed Deletion: Success!", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();

                            }
                        });
                        //Remove the deleted Item from Recycler View
                        notifyItemRemoved(position);


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

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class feedViewAdapter extends RecyclerView.ViewHolder {

        private Button deleteBtn;
        private TextView feedTitle;
        private ImageView feedImage;

        public feedViewAdapter(@NonNull View itemView) {
            super(itemView);

            deleteBtn=itemView.findViewById(R.id.deleteFeedBtn);
            feedTitle=itemView.findViewById(R.id.feedTitleDisplay);
            feedImage=itemView.findViewById(R.id.feedImageDisplay);

        }
    }

}
