package com.example.suhirtha.parsetagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suhirtha.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Fragment {

    RecyclerView rvPosts;
    PostAdapter postAdapter;
    List<Post> posts;
    int numPosts = 0;


    Button mLogout;
    Button mAddBioBtn;
    Button mEditBioBtn;
    EditText mAddBioText;
    TextView mCurrentText;
    TextView mUsernameText;
    ImageView mCamBut;
    ImageView mProfPic;
    //TextView mTopUsernameText;
    TextView mNumPosts;


    ParseUser user;

    private static final String KEY_USER_BIO = "bio";
    private static final String KEY_USER_PICTURE = "profileImage";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    //File photoFile;

    public Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }


     public void onViewCreated(View view, Bundle savedInstanceState) {

        mLogout = view.findViewById(R.id.btnLogout);

         mAddBioBtn = view.findViewById(R.id.addBio_btn);
         mAddBioText = view.findViewById(R.id.addBio_et);

         mEditBioBtn = view.findViewById(R.id.editBio_btn);
         mCurrentText = view.findViewById(R.id.bio_tv);

         //mTopUsernameText = view.findViewById(R.id.tvTopUsername);
         mUsernameText = view.findViewById(R.id.username_tv);
         mCamBut = view.findViewById(R.id.camera_iv);
         mProfPic = view.findViewById(R.id.ivProfilePic);
         mNumPosts = view.findViewById(R.id.tvNumPosts);

         rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

         mLogout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ParseUser.logOut();
                 Intent i = new Intent(getActivity(), LoginActivity.class);
                 startActivity(i);
                 getActivity().finish();
             }
         });

         user = ParseUser.getCurrentUser();

         mUsernameText.setText(user.getUsername());
         //mTopUsernameText.setText(user.getUsername());



         if(user.get(KEY_USER_PICTURE) != null) {
             Log.d("Profile", "Prof pic!");
             ParseFile prof = user.getParseFile(KEY_USER_PICTURE);
             GlideApp.with(getContext()).load(prof.getUrl()).circleCrop()
                     .into(mProfPic);
         }

         if (user.get(KEY_USER_BIO) == null) {
             mEditBioBtn.setVisibility(View.INVISIBLE);
             mCurrentText.setVisibility(View.INVISIBLE);
             Log.d("ProfileFragment","There is no bio we have saved");
         } else {
             Log.d("ProfileFragment","We have saved a bio from your profile");
             String bio = user.get(KEY_USER_BIO).toString();
             mAddBioBtn.setVisibility(View.INVISIBLE);
             mAddBioText.setVisibility(View.INVISIBLE);
             mCurrentText.setText(bio);
         }

         mAddBioBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String bio = mAddBioText.getText().toString();
                 mAddBioBtn.setVisibility(View.INVISIBLE);
                 mAddBioText.setVisibility(View.INVISIBLE);
                 mEditBioBtn.setVisibility(View.VISIBLE);
                 mCurrentText.setVisibility(View.VISIBLE);
                 mCurrentText.setText(bio);
                 user.put(KEY_USER_BIO, bio);
                 user.saveInBackground();
             }
         });

         mEditBioBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mAddBioBtn.setVisibility(View.VISIBLE);
                 mAddBioText.setVisibility(View.VISIBLE);
                 mEditBioBtn.setVisibility(View.INVISIBLE);
                 mCurrentText.setVisibility(View.INVISIBLE);
             }
         });

         posts = new ArrayList<Post>();
         postAdapter = new PostAdapter(posts);
         // RecyclerView setup (layout manager, use adapter)
         rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
         // set the adapter
         rvPosts.setAdapter(postAdapter);

         loadUserPosts();

         //numPostText.setText("" + numPosts);

     }

    private void loadUserPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                numPosts = 0;
                if (e == null) {
                    for (int i = 0; i < objects.size(); i += 1) {
                        Log.d("HomeFragment", "Post[" + i + "] = "
                                + objects.get(i).getCaption()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        // checking if grabbing right info and post object unwraps the user
                        if (objects.get(i).getUser().getUsername().equals(user.getUsername())) {
                            posts.add(0, objects.get(i));
                            postAdapter.notifyItemInserted(posts.size() - 1);
                            numPosts++;
                            mNumPosts.setText(numPosts + " posts");
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
