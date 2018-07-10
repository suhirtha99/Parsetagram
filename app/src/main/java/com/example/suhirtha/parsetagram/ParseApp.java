package com.example.suhirtha.parsetagram;

import android.app.Application;

import com.example.suhirtha.parsetagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("parsepics")
                .clientKey("allthelove")
                .server("http://suhirtha99-parsetagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }

}
