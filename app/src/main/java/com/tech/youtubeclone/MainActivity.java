package com.tech.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tech.youtubeclone.Fragment.addFragment;
import com.tech.youtubeclone.Fragment.HomeFragment;
import com.tech.youtubeclone.Fragment.LibraryFragment;
import com.tech.youtubeclone.Fragment.shortsFragment;
import com.tech.youtubeclone.Fragment.subscriptionFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.tech.youtubeclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);
//        MainActivity.this.setTitle("Youtube");
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_youtube);// set drawable icon
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                       transaction.replace(R.id.container_id,new HomeFragment());
                       Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                       break;

                    case R.id.navigation_shorts:
                        transaction.replace(R.id.container_id,new shortsFragment());
                        Toast.makeText(MainActivity.this, "Shorts", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_add:
                        transaction.replace(R.id.container_id,new addFragment());
                        Toast.makeText(MainActivity.this, "Create Shorts", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_subscriptions:
                        transaction.replace(R.id.container_id,new subscriptionFragment());
                        Toast.makeText(MainActivity.this, "Subscription", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_library:
                        transaction.replace(R.id.container_id,new LibraryFragment());
                        Toast.makeText(MainActivity.this, "Library", Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction.commit();
                return true;
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
}