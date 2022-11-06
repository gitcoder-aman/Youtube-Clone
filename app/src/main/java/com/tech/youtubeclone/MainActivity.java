package com.tech.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tech.youtubeclone.Fragment.addFragment;
import com.tech.youtubeclone.Fragment.HomeFragment;
import com.tech.youtubeclone.Fragment.LibraryFragment;
import com.tech.youtubeclone.Fragment.shortsFragment;
import com.tech.youtubeclone.Fragment.subscriptionFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.tech.youtubeclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);
//        MainActivity.this.setTitle("Youtube");
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_youtube);// set drawable icon
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_id,new HomeFragment());
        transaction.commit();

        binding.bottomNevigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                   case R.id.navigation_home:
                       binding.toolbar.setVisibility(View.VISIBLE);
                       transaction.replace(R.id.container_id,new HomeFragment());
                       Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                       break;

                    case R.id.navigation_shorts:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container_id,new shortsFragment());
                        Toast.makeText(MainActivity.this, "Shorts", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_add:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container_id,new addFragment());
                        Toast.makeText(MainActivity.this, "Create Shorts", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_subscriptions:
                        binding.toolbar.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.container_id,new subscriptionFragment());
                        Toast.makeText(MainActivity.this, "Subscription", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_library:
                        binding.toolbar.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.container_id,new LibraryFragment());
                        Toast.makeText(MainActivity.this, "Library", Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction.commit();
                return true;
            }
        });
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                Toast.makeText(MainActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //new AlertDialog.Builder(MainActivity.this)
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit this App?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //end the app
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
}