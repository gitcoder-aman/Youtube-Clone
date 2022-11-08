package com.tech.youtubeclone.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tech.youtubeclone.Model.PostModel;
import com.tech.youtubeclone.R;
import com.tech.youtubeclone.databinding.FragmentAddBinding;

import java.io.IOException;
import java.util.Date;


public class addFragment extends Fragment {

    FragmentAddBinding binding;
    ProgressDialog dialog, dialog1;
    Uri selectImageUri, videoUri, downloadUri;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public addFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Opening gallery...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog1 = new ProgressDialog(getContext());
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("Post Uploading");
        dialog1.setMessage("Please wait...");
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);


        binding.thumbnailUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                launchSomeActivity.launch(intent);
            }
        });
        binding.videoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, 1);
            }
        });

        binding.publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
                String title = binding.titleET.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    binding.titleET.setError("*");
                    return;
                } else {
                    binding.titleET.setError(null);
                    binding.titleET.clearFocus();
                }
                dialog1.show();

                PostModel postModel = new PostModel();

                //Store thumbnail, video , title and description
                final StorageReference reference = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("thumbnail")
                        .child(new Date().getTime() + "");

                //Video upload
                final StorageReference reference1 = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("video")
                        .child(new Date().getTime() + "");

                //thumbnail upload
                reference.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                postModel.setPostImage(uri.toString());
                                postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                                postModel.setPostTitle(binding.titleET.getText().toString());
                                postModel.setPostDescription(binding.descriptionET.getText().toString());
                                postModel.setPostedAt(new Date().getTime());

                                reference1.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful()) ;
                                        downloadUri = uriTask.getResult();
                                        postModel.setPostVideo(downloadUri.toString());

                                        //data load in database
                                        database.getReference().child("posts")
                                                .push()
                                                .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog1.dismiss();
                                                        Toast.makeText(getContext(), "Post uploaded", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "S" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });


        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> launchSomeActivity =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        dialog.dismiss();
                        Intent data = result.getData();

                        if (data != null && data.getData() != null) {
                            selectImageUri = data.getData();
                            Bitmap selectedImageBitmap = null;

                            try {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(addFragment.this.requireContext().getContentResolver(), selectImageUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            binding.thumbnail.setImageBitmap(selectedImageBitmap);


//                            final StorageReference reference = storage.getReference().child("child_photo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
//                            reference.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Toast.makeText(getContext(), "Cover photo saved", Toast.LENGTH_SHORT).show();
//
//                                    //Cover photo store in database
//                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("coverPhoto").setValue(uri.toString());
//                                        }
//                                    });
//                                }
//                            });
                        }
                    }
                    dialog.dismiss();
                }
            });

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            videoUri = data.getData();
            binding.videoview.setVideoURI(videoUri);

            MediaController mediaController = new MediaController(getContext());
            binding.videoview.setMediaController(mediaController);

            binding.publishBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.btn_box));
            binding.publishBtn.setTextColor(requireContext().getResources().getColor(R.color.white)); //requireContext place to getContext
            binding.publishBtn.setEnabled(true);
            dialog.dismiss();
        }
    }
}