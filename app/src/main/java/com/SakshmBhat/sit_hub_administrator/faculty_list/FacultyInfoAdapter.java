package com.SakshmBhat.sit_hub_administrator.faculty_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    //Constructor for list and context
    public FacultyInfoAdapter(List<FacultyAttributes> list, Context context) {
        this.list = list;
        this.context = context;
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

        FacultyAttributes facultyAttributes = list.get(position);
        holder.name.setText(facultyAttributes.getName());
        holder.post.setText(facultyAttributes.getEmail());
        holder.email.setText(facultyAttributes.getPost());

        Picasso.get().load(facultyAttributes.getImageUrl()).into(holder.facultyCircleImageview);

        holder.updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Update faculty info", Toast.LENGTH_SHORT).show();
                
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
