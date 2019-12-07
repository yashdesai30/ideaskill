package com.example.ideaskill;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder>{

    Context context;
    List<ModelPost> postList;

    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String pId=postList.get(position).getpId();
        String uEmail=postList.get(position).getuEmail();
        String uAim=postList.get(position).getuAim();
        String uType=postList.get(position).getuType();
        String uId=postList.get(position).getuId();
        String uTimeOfPost=postList.get(position).getuTimeOfPost();
        String uName=postList.get(position).getuName();
        String uProjectProgress=postList.get(position).getProgress();
        String uProfileImage=postList.get(position).getProfileImage();
        Context mcontext=holder.postPictureSmall.getContext();
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(uTimeOfPost));
        String uTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
        try {
            Picasso.with(mcontext).load(uProfileImage).fit().centerCrop().into(holder.postPictureSmall);
        }catch (Exception e){
            Picasso.with(mcontext).load(R.drawable.combined).into(holder.postPictureSmall);
        }

        holder.postNameSmall.setText(uName);
        holder.postTimeSmall.setText(uTime);
        holder.postAim.setText(uAim);
        holder.postType.setText(uType);
        holder.projectProgress.setText(uProjectProgress);
//        holder.postLikes.setText(uName);
//        holder.postNameSmall.setText(uName);
//        holder.postNameSmall.setText(uName);

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "More Button", Toast.LENGTH_SHORT).show();
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView postNameSmall,postTimeSmall,postAim,postType,postLikes,projectProgress;
        ImageButton moreButton;
        CircleImageView postPictureSmall;
        Button likeButton,commentButton,shareButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            projectProgress=itemView.findViewById(R.id.projectProgress);
            postNameSmall=itemView.findViewById(R.id.postNameSmall);
            postTimeSmall=itemView.findViewById(R.id.postTimeSmall);
            postAim=itemView.findViewById(R.id.postAim);
            postType=itemView.findViewById(R.id.postType);
            postLikes=itemView.findViewById(R.id.postLikes);
            moreButton=itemView.findViewById(R.id.moreButton);
            likeButton=itemView.findViewById(R.id.likeButton);
            shareButton=itemView.findViewById(R.id.shareButton);
            postPictureSmall=itemView.findViewById(R.id.postPictureSmall);

        }
    }
}
