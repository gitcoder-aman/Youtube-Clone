package com.tech.youtubeclone.Adapter;

import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaController;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.youtubeclone.Model.PostModel;
import com.tech.youtubeclone.Model.UserModel;
import com.tech.youtubeclone.R;
import com.tech.youtubeclone.VideoPageActivity;
import com.tech.youtubeclone.databinding.DashboardRvSampleBinding;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<PostModel> list;
    Context context;

    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel postModel = list.get(position);
        Picasso.get()
                .load(postModel.getPostImage())
                .placeholder(R.drawable.loading)
                .into(holder.binding.thumbnail);

        holder.binding.postTitle.setText(postModel.getPostTitle());
        holder.binding.views.setText(String.valueOf(postModel.getViews())+" "+ context.getString(R.string.views));

        String time = TimeAgo.using(postModel.getPostedAt()); // using time ago
        holder.binding.time.setText(time);

        //set name,profile
        FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(postModel.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        holder.binding.username.setText(userModel.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, VideoPageActivity.class);
                intent.putExtra("postId", postModel.getPostId());
                intent.putExtra("postedBy", postModel.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                //views updated
                FirebaseDatabase.getInstance().getReference()
                        .child("posts").child(postModel.getPostId()).child("views").setValue(postModel.getViews()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("play","Thanks for watching");
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        DashboardRvSampleBinding binding;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvSampleBinding.bind(itemView);
        }
    }
}
