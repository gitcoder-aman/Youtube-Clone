package com.tech.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.youtubeclone.Model.UserModel;
import com.tech.youtubeclone.databinding.ActivitySignupBinding;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating a user...");
        dialog.setCancelable(false);
        dialog.setTitle("Please wait");

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = Objects.requireNonNull(binding.nameBox.getText()).toString().trim();
                String email = Objects.requireNonNull(binding.emailBox.getText()).toString().trim();
                String pass = Objects.requireNonNull(binding.passwordBox.getText()).toString().trim();

                if (TextUtils.isEmpty(name)) {
                    binding.nameBox.setError("*");
                    return;
                } else {
                    binding.nameBox.setError(null);
                    binding.nameBox.clearFocus();
                }

                if (TextUtils.isEmpty(email)) {
                    binding.emailBox.setError("*");
                    return;
                } else {
                    binding.emailBox.setError(null);
                    binding.emailBox.clearFocus();
                }
                if (TextUtils.isEmpty(pass)) {
                    binding.passwordBox.setError("*");
                    return;
                } else {
                    binding.passwordBox.setError(null);
                    binding.passwordBox.clearFocus();
                }
                dialog.show();
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            dialog.dismiss();
                            UserModel userModel = new UserModel(name, email, pass);
                            String uid = task.getResult().getUser().getUid();
                            database.getReference().child("Users")
                                    .child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                                finish();
                                                Toast.makeText(SignupActivity.this, "Creating user successful", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(SignupActivity.this , Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            dialog.dismiss();
                            Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}