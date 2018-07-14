package com.example.suhirtha.parsetagram;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.suhirtha.parsetagram.models.Post;
import java.io.File;

public class HomeActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();

    // define fragments here
    // TODO - should these have the prefix 'm'?
    final Fragment homeFragment = new Home();
    final Fragment postFragment = new CreatePost();
    final Fragment profileFragment = new Profile();
    final Fragment detailsFragment = new Details();

    //---------------------------------------------------------------------

    private TextView mTextMessage;
    private EditText mCaption;
    private Button mCreate;
    private Button mRefresh;



    boolean posted;

    private ImageView mCapture;

    private Uri file;
    private Uri imageUri;

    File photoFile;
    public String photoFileName = "photo.jpg";

    private static final int CAMERA_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Fragment currentFragment;

    private FragmentTransaction fragmentTransaction1;
    private FragmentTransaction fragmentTransaction2;
    private FragmentTransaction fragmentTransaction3;
    private FragmentTransaction fragmentTransaction4;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180710_153941.jpg";

    //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                  //  mTextMessage.setText(R.string.title_dashboard);
                    fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.my_fragment, homeFragment).commit();
                    return true;
                case R.id.post:
                 //   mTextMessage.setText(R.string.title_dashboard);
                    fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.my_fragment, postFragment).commit();
                    return true;
                case R.id.profile:
                    //mTextMessage.setText(R.string.title_notifications);
                    fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.my_fragment, profileFragment).commit();
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mCaption = findViewById(R.id.etCaption);
        mCreate = findViewById(R.id.btnCreate);
        mRefresh = findViewById(R.id.btnRefresh);
        mCapture = findViewById(R.id.ivCapture);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }




    protected void openDetails(Post post) {

        // communicating from fragment to activity
        Bundle args = new Bundle();
        args.putString("PostId", post.getObjectId());
        detailsFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction4 = fragmentManager.beginTransaction();
        fragmentTransaction4.replace(R.id.my_fragment, detailsFragment).commit();
    }


}
