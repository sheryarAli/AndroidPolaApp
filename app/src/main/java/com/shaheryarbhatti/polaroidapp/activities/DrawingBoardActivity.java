package com.shaheryarbhatti.polaroidapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    Bitmap bm, bitmapScalled, bmScalled, cs, grid;
    Canvas comboImage;
    private Toolbar toolbar;
    private int drawableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((ImageView) toolbar.findViewById(R.id.logoImageView)).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Draw");
        }
        imageView = (ImageView) findViewById(R.id.drawingBoardImageView);

        zoomInButton = (ImageButton) findViewById(R.id.zoomInButton);
        zoomOutButton = (ImageButton) findViewById(R.id.zoomOutButton);
        lockBtn = (ImageButton) findViewById(R.id.lockBtn);
        zoomText = (TextView) findViewById(R.id.zoomText);


        String imagePath = getIntent().getStringExtra("image");

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

        imageView.setImageBitmap(cs);


        attacher = new PhotoViewAttacher(imageView);
        attacher.setMaximumScale(6f);
        attacher.setOnScaleChangeListener(this);
        attacher.update();

        zoomInButton.setOnClickListener(this);
        zoomOutButton.setOnClickListener(this);

        lockBtn.setOnClickListener(this);

    }

    private void setBm(int imageSourceType, BitmapFactory.Options options, String imagePath) {
        if (imageSourceType == 1) {
            bm = BitmapFactory.decodeFile(imagePath, options);

        } else {
            Log.d(TAG, "onCreate: else code");
            drawableId = UtilImage.getDrawableId(this, imagePath);
            bm = BitmapFactory.decodeResource(getResources(), drawableId, options);
        }

    }

    private void updateLockButton() {
        if (isLocked) {
            lockBtn.setImageResource(R.drawable.ic_image_unlock);
            imageView.setEnabled(true);
            zoomInButton.setVisibility(View.VISIBLE);
            zoomOutButton.setVisibility(View.VISIBLE);
            zoomText.setVisibility(View.VISIBLE);
        } else {
            lockBtn.setImageResource(R.drawable.ic_image_lock);
            imageView.setEnabled(false);
            zoomInButton.setVisibility(View.INVISIBLE);
            zoomOutButton.setVisibility(View.INVISIBLE);
            zoomText.setVisibility(View.INVISIBLE);
        }
        isLocked = !isLocked;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zoomInButton:
                Log.d(TAG, "ZoomInButton: " + attacher.getScale());
                if (attacher.getScale() >= 1 && attacher.getScale() < 6) {
                    if ((attacher.getScale() % 1) != 0) {
                        attacher.setScale((float) Math.ceil(attacher.getScale()));
                    } else
                        attacher.setScale(attacher.getScale() + 1f);

                    zoomText.setText(String.format(Locale.ENGLISH, "%.1fx", attacher.getScale()));

                }
                break;
            case R.id.zoomOutButton:
                Log.d(TAG, "ZoomOutButton: " + attacher.getScale());

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
