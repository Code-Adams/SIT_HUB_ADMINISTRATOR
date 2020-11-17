package com.SakshmBhat.sit_hub_administrator.faculty_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SakshmBhat.sit_hub_administrator.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
//import android.content.Context;

public class FacultyInfoAdapter extends  RecyclerView.Adapter<FacultyInfoAdapter.facultyInfoViewAdapter> {

    private List<FacultyAttributes> list;
    private Context context;
    private String category;

    //Constructor for list and context
    public FacultyInfoAdapter(List<FacultyAttributes> passedList, Context passedContext, String passedCategory) {
        this.list = passedList;
        this.context = passedContext;
        this.category=passedCategory;
    }

    @NonNull
    @Override
    public facultyInfoViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_info_layout_card,parent,false);

        //Return object of facultyInfoViewAdapter
        return new facultyInfoViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull facultyInfoViewAdapter holder, int position) {

        final FacultyAttributes facultyAttributes = list.get(position);
        holder.name.setText(facultyAttributes.getName());
        holder.post.setText(facultyAttributes.getEmail());
        holder.email.setText(facultyAttributes.getPost());

        try {
            Picasso.get().load(facultyAttributes.getImageUrl()).into(holder.facultyCircleImageview);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateFacultyActivity.class);
                intent.putExtra("name",facultyAttributes.getName());
                intent.putExtra("post",facultyAttributes.getPost());
                intent.putExtra("email",facultyAttributes.getEmail());
                intent.putExtra("imageUrl",facultyAttributes.getImageUrl());
                intent.putExtra("key",facultyAttributes.getKey());
                intent.putExtra("category",category);
                context.startActivity(intent);
                
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class facultyInfoViewAdapter extends RecyclerView.ViewHolder {

         private TextView  name,email,post;
         private Button updateInfoBtn;
         private CircleImageView facultyCircleImageview;

        public facultyInfoViewAdapter(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.facultyNameDisplay);
            email = itemView.findViewById(R.id.facultyEmailDisplay);
            post = itemView.findViewById(R.id.facultyPostDisplay);
            updateInfoBtn = itemView.findViewById(R.id.updateFacultyInfBtn);
            facultyCircleImageview = itemView.findViewById(R.id.facultyImageDisplay);


        }
    }

}
