package com.tech.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tech.youtubeclone.Model.PostModel;
import com.tech.youtubeclone.Model.UserModel;
import com.tech.youtubeclone.databinding.ActivityVideoPageBinding;


public class VideoPageActivity extends AppCompatActivity {

    ActivityVideoPageBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Intent intent;
    String postId;
    String postedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

//        String path = "android.resource://" + getPackageName() + "/raw/song1";
//
//        Uri videoURI = Uri.parse(path);
//
//        //binding.videoView.setVideoPath(path);
//        binding.videoView.setVideoURI(videoURI);
//        binding.videoView.start();

        intent = getIntent();

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

               Toast.makeText(this, "Post ID: " + postId, Toast.LENGTH_SHORT).show();
               Toast.makeText(this, "User ID: " + postedBy, Toast.LENGTH_SHORT).show();

               //all data set
               database.getReference().child("posts")
                       .child(postId).addValueEventListener(new ValueEventListener() {
                           @SuppressLint("SetTextI18n")
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               PostModel postModel = snapshot.getValue(PostModel.class);

                               String time = TimeAgo.using(postModel.getPostedAt()); // using time ago
                               binding.time.setText(time);

                               binding.views.setText(String.valueOf(postModel.getViews())+" "+ getString(R.string.views));
                               binding.title.setText(postModel.getPostTitle());
                               binding.videoView.setVideoURI(Uri.parse(postModel.getPostVideo()));
                               binding.videoView.start();

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
               //Name, follow set
               database.getReference().child("Users")
                       .child(FirebaseAuth.getInstance().getUid())
                       .addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               UserModel userModel = snapshot.getValue(UserModel.class);

                               binding.channelName.setText(userModel.getName());
//                               binding.follower.setText(userModel.getFollowCount());
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });


    }
}