package com.example.suhirtha.parsetagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";


    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public String getUser() {
        return getString(KEY_USER);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public void ParseUser(String user) {
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


}
