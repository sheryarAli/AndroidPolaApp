package com.shaheryarbhatti.polaroidapp.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.fragments.DashboardFragment;
import com.shaheryarbhatti.polaroidapp.fragments.FeaturedFragment;
import com.shaheryarbhatti.polaroidapp.fragments.PopularFragment;
import com.shaheryarbhatti.polaroidapp.preferences.LocalStoragePreferences;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;
import com.shaheryarbhatti.polaroidapp.viewpagers.MainFragmentPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener
        , FeaturedFragment.OnFragmentInteractionListener, PopularFragment.OnFragmentInteractionListener {


    private final String TAG = "MainActivity";
    private TabLayout tabs;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private final int CAMERA_AND_WRITE_PERMISSION = 101;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 102;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_PICK = 2;
    private String mCurrentPhotoPath;
    CharSequence takePhotoMenu[] = new CharSequence[]{"Add Photo From Library", "Take A Photo"};
    private UtilImage utilImage;
    boolean doubleBackToExitPressedOnce = false;
    private FloatingActionButton fabAdd;
    private LocalStoragePreferences preferences;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = new LocalStoragePreferences(this);
        utilImage = UtilImage.getInstance();
        tabs = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        fabAdd = findViewById(R.id.fab_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabs.setupWithViewPager(viewPager);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostCreationActivity.class));
            }
        });


    }

    private void dispatchPickPictureIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_IMAGE_PICK);
    }

    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.takePhotoAction:
                showPopUpMenu(item.getActionView());
                break;
            case R.id.logout:
                showLogOutMenu(item.getActionView());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopUpMenu(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Camera");
        builder.setItems(takePhotoMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onMenuItemClick(which);
                    }
                }
        );
        builder.show();
    }

    private void showLogOutMenu(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you Sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handleLogOut(preferences.getPushToken(), preferences.getToken());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
//        builder.setItems(takePhotoMenu, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        onMenuItemClick(which);
//                    }
//                }
//        );
        builder.show();
    }

    private void handleLogOut(String pushToken, String token) {
        String logOutUrl = getResources().getString(R.string.logout_test_url);
        Log.d(TAG, "handleLogOut: for debugging");
        AndroidNetworking.post(logOutUrl)
                .addHeaders("Authorization", token)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("push_token", pushToken)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "onResponse: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");

                            if (isSuccess) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            FirebaseInstanceId.getInstance().deleteInstanceId();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                preferences.clearAll();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

    private boolean requestCameraAndCameraPermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP && checkForCameraPermission() && checkForWritePermission()) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_AND_WRITE_PERMISSION);
            return false;

        }

        return true;
    }

    private boolean requestReadExternalStoragePermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP && checkForReadExternalStoragePermission()) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION);
            return false;

        }

        return true;
    }

    private boolean checkForCameraPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkForWritePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkForReadExternalStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }


    public boolean onMenuItemClick(int itemPosition) {
        switch (itemPosition) {
            case 0:

//                Toast.makeText(this, "Will Add photo from gallery", Toast.LENGTH_SHORT).show();
                if (requestReadExternalStoragePermission()) {
                    dispatchPickPictureIntent();
                }
                break;
            case 1:
                if (requestCameraAndCameraPermission()) {
                    dispatchTakePictureIntent();
                }

                break;

        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashboardFragmentInteraction(Bundle args) {

        Intent intent = new Intent(MainActivity.this, ItemSelelectedActivity.class);
        intent.putExtra("post", args);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkRequestGranted(grantResults)) {
            switch (requestCode) {
                case CAMERA_AND_WRITE_PERMISSION:
                    dispatchTakePictureIntent();
                    break;

                case READ_EXTERNAL_STORAGE_PERMISSION:
                    dispatchPickPictureIntent();
                    break;
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private boolean checkRequestGranted(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, ImageCapturedActivity.class);
            switch (requestCode) {
                /* sourceType camera = 0, gallery = 1*/
                case REQUEST_IMAGE_CAPTURE:
                    Log.d(TAG, "onActivityResult: image capture");
                    break;
                case REQUEST_IMAGE_PICK:
                    mCurrentPhotoPath = utilImage.getImagePath(MainActivity.this, data.getData());
                    break;

            }
            Log.d(TAG, "onActivityResult: for debugging");
            intent.putExtra("image", mCurrentPhotoPath);
            startActivity(intent);
        }
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }*/

    }
}
