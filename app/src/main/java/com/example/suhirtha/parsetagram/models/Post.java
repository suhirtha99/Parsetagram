package com.example.suhirtha.parsetagram.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";


    public String getCaption() {
        return getString(KEY_CAPTION);
    }
    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post> {

        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }

    }

    public static Post newInstance(ParseUser user, ParseFile file, String description) {
        final Post newPost = new Post();
        newPost.setCaption(description);
        newPost.setUser(user);
        newPost.setImage(file);
        newPost.put("comments", new ArrayList<String>());

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) { // if there is no error
                    Log.d("Post", "Post save successful!");
                } else {
                    Log.e("Post", "Post save failure.");
                    e.printStackTrace();
                }
            }
        });

        return newPost;
    }

    public String getRelativeTimeAgo() {
        long dateMillis = getCreatedAt().getTime();
        return DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

}
