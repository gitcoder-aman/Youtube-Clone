package com.tech.youtubeclone.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.youtubeclone.Adapter.PostAdapter;
import com.tech.youtubeclone.Model.PostModel;
import com.tech.youtubeclone.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    RecyclerView dashboardRV;
    ArrayList<PostModel>postList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        dashboardRV = view.findViewById(R.id.dashboardRV);

        postList = new ArrayList<>();

//        String path = "android.resource://"+"/raw/song";

//        postList.clear();
        postList.add(new PostModel("3848qefw",R.raw.song,"Aman Kumar","Emraan Hashmi new Full Song Best Actor Full goosebumps song",434328));
        postList.add(new PostModel("3848qefw",R.raw.song,"Aman Kumar","Emraan Hashmi new Full Song Best Actor Full goosebumps song",434328));

        PostAdapter postAdapter = new PostAdapter(postList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setAdapter(postAdapter);

        return view;
    }
}