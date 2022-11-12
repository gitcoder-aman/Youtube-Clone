package com.tech.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.tech.youtubeclone.Adapter.PostAdapter;
import com.tech.youtubeclone.Model.FollowModel;
import com.tech.youtubeclone.Model.PostModel;
import com.tech.youtubeclone.Model.UserModel;
import com.tech.youtubeclone.databinding.ActivityVideoPageBinding;

import java.util.ArrayList;
import java.util.Date;


public class VideoPageActivity extends AppCompatActivity {

    ActivityVideoPageBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Intent intent;
    String postId;
    String postedBy;
    RecyclerView dashRecommendRV;
    ArrayList<PostModel> postList;

    //For video variable
    private TextView videoTimeTV;
    private ImageButton previousIB, nextIB, playPauseIB;
    private SeekBar videoSeekBar;
    private RelativeLayout controlsRL;
    private LinearLayoutCompat videoLinearLayout;
    boolean isOpen = true;

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

//        Toast.makeText(this, "Post ID: " + postId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "User ID: " + postedBy, Toast.LENGTH_SHORT).show();

        videoTimeTV = findViewById(R.id.videoTime);
        previousIB = findViewById(R.id.previousBtn);
        playPauseIB = findViewById(R.id.playBtn);
        nextIB = findViewById(R.id.nextBtn);
        videoSeekBar = findViewById(R.id.seekbarId);
        controlsRL = findViewById(R.id.RLCustomControl);
        videoLinearLayout = findViewById(R.id.videoLinearLayout);


        //all data set
        database.getReference().child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostModel postModel = snapshot.getValue(PostModel.class);

                        String time = TimeAgo.using(postModel.getPostedAt()); // using time ago
                        binding.time.setText(time);

                        binding.views.setText(String.valueOf(postModel.getViews()) + " " + getString(R.string.views));
                        binding.title.setText(postModel.getPostTitle());
                        binding.videoView.setVideoURI(Uri.parse(postModel.getPostVideo()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoSeekBar.setMax(binding.videoView.getDuration());
                binding.videoView.start();
            }
        });
        previousIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.videoView.seekTo(binding.videoView.getCurrentPosition() - 10000);
            }
        });
        nextIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.videoView.seekTo(binding.videoView.getCurrentPosition() + 10000);
            }
        });
        playPauseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.videoView.isPlaying()) {
                    binding.videoView.pause();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                } else {
                    binding.videoView.start();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
            }
        });
        videoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    hideControls();
                    isOpen = false;
                } else {
                    showControl();
                    isOpen = true;
                }
            }
        });
        setHandler();
        initializeSeekBar();

        //Name, follow set
        database.getReference().child("Users")
                .child(postedBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        binding.channelName.setText(userModel.getName());
                        binding.follower.setText(String.valueOf(userModel.getFollowCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //already subscriber
        database.getReference().child("Users")
                .child(postedBy).child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.subscribeBtn.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.rounded_box));
                            binding.subscribeBtn.setText("SUBSCRIBED");
                            binding.subscribeBtn.setTextColor(getResources().getColor(R.color.black));
                            binding.subscribeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bell, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.getReference().child("Users")
                        .child(postedBy).child("followers")
                        .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    //database subscribe update
                                    database.getReference().child("Users")
                                            .child(postedBy)
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(VideoPageActivity.this, "Unsubscribed the channel", Toast.LENGTH_SHORT).show();
                                                    binding.subscribeBtn.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.subscribe_box));
                                                    binding.subscribeBtn.setText("SUBSCRIBE");
                                                    binding.subscribeBtn.setTextColor(getResources().getColor(R.color.white));
                                                }
                                            });

                                } else {

                                    //database subscribe update
                                    FollowModel followModel = new FollowModel();
                                    followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    followModel.setFollowedAt(new Date().getTime());

                                    database.getReference().child("Users")
                                            .child(postedBy).child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(followModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("Users")
                                                            .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    UserModel userModel = snapshot.getValue(UserModel.class);

                                                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                                                            .child(postedBy).child("followCount")
                                                                            .setValue(userModel.getFollowCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    binding.subscribeBtn.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.rounded_box));
                                                                                    binding.subscribeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bell, 0, 0, 0);
                                                                                    binding.subscribeBtn.setText("SUBSCRIBED");
                                                                                    binding.subscribeBtn.setTextColor(getResources().getColor(R.color.black));

                                                                                    Toast.makeText(VideoPageActivity.this, "Thanks for Subscribe the channel.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        dashRecommendRV = findViewById(R.id.dashRecommendRV);

        postList = new ArrayList<>();
        PostAdapter postAdapter = new PostAdapter(postList, VideoPageActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(VideoPageActivity.this);
        dashRecommendRV.setLayoutManager(layoutManager);
        dashRecommendRV.setAdapter(postAdapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    postList.add(postModel);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeSeekBar() {
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (videoSeekBar.getId() == R.id.seekbarId) {
                    if (fromUser) {
                        binding.videoView.seekTo(progress);
                        binding.videoView.start();
                        int currPos = binding.videoView.getCurrentPosition();
                        videoTimeTV.setText("" + convertTime(binding.videoView.getDuration() - currPos));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setHandler() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (binding.videoView.getDuration() > 0) {
                    int currPos = binding.videoView.getCurrentPosition();
                    videoSeekBar.setProgress(currPos);
                    videoTimeTV.setText("" + convertTime(binding.videoView.getDuration() - currPos));
                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private String convertTime(int ms) {
        String time;

        int x, seconds, minutes, hours;
        x = ms / 1000;
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;
        if (hours != 0) {
            time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        } else {
            time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }
        return time;
    }

    private void showControl() {

        controlsRL.setVisibility(View.VISIBLE);

        final Window window = this.getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();

        if (decorView != null) {
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void hideControls() {

        controlsRL.setVisibility(View.GONE);

        final Window window = this.getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();

        if (decorView != null) {
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }
}