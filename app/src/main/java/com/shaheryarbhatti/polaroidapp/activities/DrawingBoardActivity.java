package com.shaheryarbhatti.polaroidapp.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


/**
 * /**
 * Created by Shahzore on 12/17/2017.
 */


public class DrawingBoardActivity extends AppCompatActivity implements View.OnClickListener, OnScaleChangedListener {
    private final String TAG = "DrawingBoardActivity";

    private ImageView imageView;
    private PhotoViewAttacher attacher;
    //    private ImageView locked, unlocked, home, rainbow;
    //    private ImageButton  ;
    private ImageButton zoomInButton, zoomOutButton, lockBtn;
    private TextView zoomText;
    private String filePath;
    private Uri imageURI;
    private boolean isLocked = false;
    private View zoomContorlContainer, toolbarContainer;
    Bitmap bm, bitmapScalled, bmScalled, cs, grid;
    Canvas comboImage;
    private Toolbar toolbar;
    private int drawableId;
    private float restoredScale;
    private UtilImage utilImage;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        utilImage = UtilImage.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((ImageView) toolbar.findViewById(R.id.logoImageView)).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Draw");
        }
        imageView = findViewById(R.id.drawingBoardImageView);
        toolbarContainer = findViewById(R.id.toolbarContainer);
        zoomContorlContainer = findViewById(R.id.zoomContorlContainer);

        zoomInButton = findViewById(R.id.zoomInButton);
        zoomOutButton = findViewById(R.id.zoomOutButton);
        lockBtn = findViewById(R.id.lockBtn);
        zoomText = findViewById(R.id.zoomText);
        imageUrl = getIntent().getStringExtra("imageUrl");
        new LoadImageTask().execute();
//        handleImageLoad();

        /*String imagePath = getIntent().getStringExtra("image");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int imageSourceType = getIntent().getIntExtra("imageType", 0);

        setBm(imageSourceType, options, imagePath);


        float aspectRatio = (float) options.outWidth / (float) options.outHeight;

        int height = Math.round(getResources().getDisplayMetrics().widthPixels / aspectRatio);

        options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, getResources().getDisplayMetrics().widthPixels, height);
        options.inJustDecodeBounds = false;


        setBm(imageSourceType, options, imagePath);

        bmScalled = getResizedBitmap(bm, getResources().getDisplayMetrics().widthPixels, height);//Resources.DisplayMetrics.HeightPixels);

        grid = BitmapFactory.decodeResource(getResources(), R.drawable.grid4_grey);
        bitmapScalled = Bitmap.createScaledBitmap(grid, getResources().getDisplayMetrics().widthPixels, height, true);
        cs = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels, height, Bitmap.Config.ARGB_8888);
        comboImage = new Canvas(cs);

        comboImage.drawBitmap(bmScalled, 0f, 0f, null);
        comboImage.drawBitmap(bitmapScalled, 0f, 0f, null);

        imageView.setImageBitmap(cs);*/


        attacher = new PhotoViewAttacher(imageView);
        attacher.setMaximumScale(6f);
        attacher.setOnScaleChangeListener(this);
        attacher.update();

        zoomInButton.setOnClickListener(this);
        zoomOutButton.setOnClickListener(this);

        lockBtn.setOnClickListener(this);

    }


    private class LoadImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... aVoid) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
//                connection.getHeaderField()
                connection.connect();
                BufferedInputStream input = new BufferedInputStream(connection.getInputStream());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;


                bm = BitmapFactory.decodeStream(input, null, options);
                input.close();
                connection.disconnect();
                Log.d(TAG, "doInBackground: for debugging");


                float aspectRatio = (float) options.outWidth / (float) options.outHeight;

                int height = Math.round(getResources().getDisplayMetrics().widthPixels / aspectRatio);

                options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, getResources().getDisplayMetrics().widthPixels, height);
                options.inJustDecodeBounds = false;

//                input.reset();
                URL url2 = new URL(imageUrl);
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setDoInput(true);
//                connection.getHeaderField()
                connection2.connect();
                BufferedInputStream input2 = new BufferedInputStream(connection2.getInputStream());
                bm = BitmapFactory.decodeStream(input2, null, options);

                Log.d(TAG, "doInBackground: for debugging");

                bmScalled = getResizedBitmap(bm, getResources().getDisplayMetrics().widthPixels, height);//Resources.DisplayMetrics.HeightPixels);

                grid = BitmapFactory.decodeResource(getResources(), R.drawable.grid4_grey);
                bitmapScalled = Bitmap.createScaledBitmap(grid, getResources().getDisplayMetrics().widthPixels, height, true);
                cs = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels, height, Bitmap.Config.ARGB_8888);
                comboImage = new Canvas(cs);

                comboImage.drawBitmap(bmScalled, 0f, 0f, null);
                comboImage.drawBitmap(bitmapScalled, 0f, 0f, null);
                input2.close();
                connection2.disconnect();


            } catch (IOException e) {
                // Log exception
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageView.setImageBitmap(cs);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: scaleValue: " + attacher.getScale());
        outState.putFloat("scaleValue", attacher.getScale());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoredScale = savedInstanceState.getFloat("scaleValue");
        /*Log.d(TAG, "onRestoreInstanceState: restoreScale: "+ restoredScale);
        restoreScaleValueOnOrientationChange();
        Log.d(TAG, "onRestoreInstanceState: attacher.getScale(): "+ attacher.getScale());*/

    }

    private void restoreScaleValueOnOrientationChange() {
        attacher.setScale(restoredScale);
        zoomText.setText(String.format(Locale.ENGLISH, "%.1fx", restoredScale));

    }

    private void setBm(int imageSourceType, BitmapFactory.Options options, String imagePath) {
        if (imageSourceType == 1) {
//            BitmapFactory.decodeStream()
            bm = BitmapFactory.decodeFile(imagePath, options);

        } else {
            Log.d(TAG, "onCreate: else code");
            drawableId = utilImage.getDrawableId(this, imagePath);
            bm = BitmapFactory.decodeResource(getResources(), drawableId, options);
        }

    }

    private void updateLockButton() {
        if (isLocked) {
            // Code for Unlock
            lockBtn.setImageResource(R.drawable.ic_image_unlock);
            imageView.setEnabled(true);
            zoomContorlContainer.setVisibility(View.VISIBLE);
            /*zoomInButton.setVisibility(View.VISIBLE);
            zoomOutButton.setVisibility(View.VISIBLE);
            zoomText.setVisibility(View.VISIBLE);*/
            toolbarContainer.setVisibility(View.VISIBLE);
            unlockDeviceOrientation();
        } else {
            // Code for Lock
            lockBtn.setImageResource(R.drawable.ic_image_lock);
            imageView.setEnabled(false);
            zoomContorlContainer.setVisibility(View.INVISIBLE);
            /*zoomInButton.setVisibility(View.INVISIBLE);
            zoomOutButton.setVisibility(View.INVISIBLE);
            zoomText.setVisibility(View.INVISIBLE);*/
            toolbarContainer.setVisibility(View.INVISIBLE);
            lockDeviceOrientation();
        }

        isLocked = !isLocked;
    }

    private void unlockDeviceOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private void lockDeviceOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zoomInButton:

                if (attacher.getScale() >= 1 && attacher.getScale() < 6) {
                    if ((attacher.getScale() % 1) != 0) {
                        attacher.setScale((float) Math.ceil(attacher.getScale()));
                    } else
                        attacher.setScale(attacher.getScale() + 1f);

                    zoomText.setText(String.format(Locale.ENGLISH, "%.1fx", attacher.getScale()));

                }
                break;
            case R.id.zoomOutButton:
                if (attacher.getScale() > 1) {
                    if ((attacher.getScale() % 1) != 0) {
                        attacher.setScale((float) Math.floor(attacher.getScale()));
                    } else
                        attacher.setScale(attacher.getScale() - 1f);
                    zoomText.setText(String.format(Locale.ENGLISH, "%.1fx", attacher.getScale()));
                }
                break;
            case R.id.lockBtn:
                updateLockButton();
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public double getScale(int originalWidth, int originalHeight, int requiredWidth, int requiredHeight) {
        //a scale of 1 means the original dimensions
        //of the image are maintained
        double scale = 1;

        //calculate scale only if the height or width of
        //the image exceeds the required value.
        if ((originalWidth > requiredWidth) || (originalHeight > requiredHeight)) {
            //calculate scale with respect to
            //the smaller dimension
            if (originalWidth < originalHeight)
                scale = Math.round((float) originalWidth / requiredWidth);
            else
                scale = Math.round((float) originalHeight / requiredHeight);

        }

        return scale;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.Recycle();
        return resizedBitmap;
    }


    private float attacherScaleChange() {

        float scale = attacher.getScale();
        if (scale > 6) {
            attacher.setScale(6f);
            zoomText.setText(String.format(Locale.ENGLISH, "%.1fx", 6f));
            return 6f;

        } else
            zoomText.setText(String.format(Locale.ENGLISH, "%.1fx", scale));
        return scale;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onScaleChange(float scaleFactor, float focusX, float focusY) {
        attacherScaleChange();

    }
}
