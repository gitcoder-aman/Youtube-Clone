package com.tech.youtubeclone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.youtubeclone.Model.PostModel;
import com.tech.youtubeclone.R;
import com.tech.youtubeclone.databinding.DashboardRvSampleBinding;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    ArrayList<PostModel> list;
    Context context;

    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel postModel = list.get(position);

        String path = "android.resource://"+ context.getPackageName()+"/raw/song";
//        holder.binding.videoView.setVideoPath(path);
        Uri videoURI = Uri.parse(path);

        holder.binding.videoView.setVideoURI(videoURI);
        holder.binding.videoView.start();

//        MediaController mediaController = new MediaController(context);
//        holder.binding.videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(holder.binding.videoView);

        holder.binding.postDescription.setText(postModel.getPostDescription());
        holder.binding.postedBy.setText(postModel.getPostedBy());
//        holder.binding.time.setText((int) new Date().getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        DashboardRvSampleBinding binding;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvSampleBinding.bind(itemView);
        }
    }
}
