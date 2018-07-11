package com.example.suhirtha.parsetagram;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suhirtha.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

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

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    static final int REQUEST_IMAGE_CAPTURE = 1;

    //private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180710_153941.jpg";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
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

        /*
        mCreate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String caption = mCaption.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(imagePath);
                final ParseFile parseFile = new ParseFile(file);
                createPost(caption, parseFile, user);
            }
        });
        */

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTopPosts();
            }
        });

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void loadTopPosts() {

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getCaption()
                                + "\nusername =" + objects.get(i).getUser());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ParseQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = " + objects.get(i).getCaption());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createPost(String caption, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setCaption(caption);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity.java", "Post successful.!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * FROM ANDROID DOCUMENTATION
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * FROM OTHER SHADY WEBSITE
     * --------------------------------------------------------------------
     * https://androidkennel.org/android-camera-access-tutorial/
     */

    public void takePicture(View view) {

        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile()); //TODO


        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        // we call startActivityForResult instead of just startActivity because we need a result
        // to be returned
        startActivityForResult(takePictureIntent, 100); //100 is some integer constant

        */

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            photoFile = getPhotoFileUri(photoFileName);

            Uri fileProvider = FileProvider.getUriForFile(this, "com.example.suhirtha.parsetagram", photoFile); //TODO

            //imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
                    //String.valueOf(System.currentTimeMillis()) + ".jpg"));

            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileProvider);

            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            //Log.d("HomeActivity", imageUri.toString());
        }


    }

    /**
     * Now that we've sent the intent to the system, we have to handle the result.
     *
     * @param requestCode - hard-coded integer constant? TODO - what are all these random integers
     * @param resultCode  - another random integer constant?
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        /*
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                //mCapture.setImageURI(file);

                -- Android tutorial?
                Bitmap takenImage = BitmapFactory.decodeFile(file.getPath());
                mCapture.setImage;

            }
        }*/

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // Bitmap photo = (Bitmap) data.getExtras().get("data");


            //use imageUri here to access the image
//
//            Bundle extras = data.getExtras();
//
//            Log.e("URI",imageUri.toString());
//
//            Bitmap bmp = (Bitmap) extras.get("data");

            // here you will get the image as bitmap

            Bitmap bmp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            mCapture.setImageBitmap(bmp);


        } else {
            Toast.makeText(this, "Oops. Something went wrong.", Toast.LENGTH_LONG).show();
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "ParsetagramCam");

        if (mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    /**
     * StackOverflow code - https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void post(View view) {
        final String caption = mCaption.getText().toString();
        final ParseUser user = ParseUser.getCurrentUser();
        final File file = getPhotoFileUri(photoFileName);
        final ParseFile parseFile = new ParseFile(file);

        createPost(caption, parseFile, user);
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "HomeActivity"); //TODO - getContext for fragment

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("HomeActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

}
