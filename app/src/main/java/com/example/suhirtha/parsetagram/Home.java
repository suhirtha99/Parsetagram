package com.example.suhirtha.parsetagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suhirtha.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    PostAdapter postAdapter;
    List<Post> posts;

    RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(true);
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        posts = new ArrayList<Post>();
        postAdapter = new PostAdapter(posts);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        // RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        loadTopPosts();
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i += 1) {
                        Log.d("HomeFragment", "Post[" + i + "] = "
                                + objects.get(i).getCaption()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        // checking if grabbing right info and post object unwraps the user
                        posts.add(0, objects.get(i));
                        postAdapter.notifyItemInserted(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        postAdapter.clear();
        loadTopPosts();
        swipeContainer.setRefreshing(false);
    }



}


