package com.example.suhirtha.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suhirtha.parsetagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    private List<Post> mPosts;
    private Context context;

    private static final String KEY_USER_PICTURE = "profileImage";

    // pass in the Post array in the constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache reference into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }


    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // get the data according to position
        Post post = mPosts.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvTopUsername.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getCaption());
        // String date = getRelativeTimeAgo(tweet.createAt);
        holder.tvTimestamp.setText(post.getRelativeTimeAgo().toUpperCase());

        ParseUser postUser = post.getUser();
        if(postUser.get(KEY_USER_PICTURE) != null) {
            ParseFile photoFile = postUser.getParseFile(KEY_USER_PICTURE);
            GlideApp.with(context).load(photoFile.getUrl()).circleCrop()
                    .into(holder.ivUserImage);
        }

        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivPostImage;
        public TextView tvUsername;
        public TextView tvTopUsername;
        public TextView tvCaption;
        public TextView tvTimestamp;
        public ImageView ibHeart;
        public ImageView ibComment;
        public ImageView ibShare;
        public EditText etComment;
        public ImageView ivUserImage;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvTopUsername = (TextView) itemView.findViewById(R.id.tvTopUsername);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            ibHeart = (ImageView) itemView.findViewById(R.id.heart_btn);
            ibComment = (ImageView) itemView.findViewById(R.id.comment_btn);
            ibShare = (ImageView) itemView.findViewById(R.id.share_btn);
            //etComment = (EditText) itemView.findViewById(R.id.comment_et); - TODO// add comments
            ivUserImage = (ImageView) itemView.findViewById(R.id.profile_iv);

            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String comment = etComment.getText().toString();
                    String id = ParseUser.getCurrentUser().getObjectId();
                    String text = id + " " + comment;

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // get the post at the position, this won't work if the class is static
                        Post post = mPosts.get(position);
                        post.add("comments", text);
                        post.saveInBackground();
                        etComment.getText().clear();
                    }
                }
            });

            itemView.setOnClickListener(this);
        }


        // when the user clicks on a row, show PostDetailsFragments the selected tweet
        @Override
        public void onClick(View view) {
            // gets item position
            Log.d("PostAdapter", String.format("A post was clicked"));
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                Log.d("PostAdapter", String.format("Got the tweet: " + post.getCaption()));
                ((HomeActivity) context).openDetails(post);
            }
        }

    }




    }
