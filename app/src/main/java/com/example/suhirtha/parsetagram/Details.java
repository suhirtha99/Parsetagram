package com.example.suhirtha.parsetagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suhirtha.parsetagram.models.Post;
import com.parse.GetCallback;
import com.parse.ParseException;


public class Details extends Fragment {
    public ImageView mPostImage;
    public TextView mUsername;
    public TextView mCaption;
    public TextView mTimestamp;
    public ImageView mHeart;
    public ImageView mComment;
    public ImageView mShare;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View itemView, Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);

        // initiate all the Views in PostDetailsFragment
        // perform findViewById lookups
        //mPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
        mUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        //mCaption = (TextView) itemView.findViewById(R.id.tvCaption);
        //mTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        //mHeart = (ImageView) itemView.findViewById(R.id.heart_btn);
        //mComment = (ImageView) itemView.findViewById(R.id.comment_btn);
        //mShare = (ImageView) itemView.findViewById(R.id.share_btn);



        Bundle args = getArguments();
        String id = args.getString("PostId");

        Post.Query query = new Post.Query().withUser();
        query.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                // assign Views values in PostDetailsFragment
                try {
                    mUsername.setText(post.getUser().fetchIfNeeded().getUsername());

                    //mCaption.setText(post.getCaption());
                    // String date = getRelativeTimeAgo(tweet.createAt);
                    //mTimestamp.setText(post.getRelativeTimeAgo());

//                    GlideApp.with(getContext())
//                            .load(post.getImage().getUrl())
//                            .into(mPostImage);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


}
