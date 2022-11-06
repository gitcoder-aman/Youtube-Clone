package com.tech.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tech.youtubeclone.databinding.ActivityVideoPageBinding;

import java.util.HashMap;


public class VideoPageActivity extends AppCompatActivity {

    ActivityVideoPageBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        String path = "android.resource://" + getPackageName() + "/raw/song1";

        Uri videoURI = Uri.parse(path);

        //binding.videoView.setVideoPath(path);
        binding.videoView.setVideoURI(videoURI);
        binding.videoView.start();


//       final String timeStamp = ""+System.currentTimeMillis();
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Videos");
//
//        storageReference.putFile(videoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri>uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while(!uriTask.isSuccessful());
//                Uri downloadUri = uriTask.getResult();
//
//                if(uriTask.isSuccessful()){
//                    HashMap<String,Object>hashMap = new HashMap<>();
//                    hashMap.put("title",""+"Emran Hsashmi");
//                    hashMap.put("timeStamp",""+timeStamp);
//                    hashMap.put("videoUrl",""+downloadUri);
//
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");
//                    reference.child(timeStamp)
//                            .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(VideoPageActivity.this, "Video uploaded...", Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(VideoPageActivity.this, "R"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(VideoPageActivity.this, "S"+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}