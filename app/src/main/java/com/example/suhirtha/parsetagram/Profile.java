package com.example.suhirtha.parsetagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.io.File;


public class Profile extends Fragment {


    ParseUser currentUser = ParseUser.getCurrentUser();
    Button mLogout;
    Button mAddBioBtn;
    Button mEditBioBtn;
    EditText mAddBioText;
    TextView mCurrentText;
    TextView mUsernameText;
    ImageView mCamBut;
    ImageView mProfPic;

    ParseUser user;

    private static final String KEY_USER_BIO = "bio";
    private static final String KEY_USER_PICTURE = "profileImage";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

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

         mUsernameText = view.findViewById(R.id.username_tv);
         mCamBut = view.findViewById(R.id.camera_iv);
         mProfPic = view.findViewById(R.id.profile_iv);

         mLogout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ParseUser.logOut();
                 Intent i = new Intent(getActivity(), LoginActivity.class);
                 startActivity(i);
                 getActivity().finish();
             }
         });





     }
}
