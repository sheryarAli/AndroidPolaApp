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


/**
 * /**
 * Created by Shahzore on 12/17/2017.
 */


public class DrawingBoardActivity extends AppCompatActivity implements View.OnClickListener, OnScaleChangedListener {
    private final String TAG = "DrawingBoardActivity";

    private ImageView imageView;
    private PhotoViewAttacher attacher;
    private ImageView locked, unlocked, home, rainbow;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;
    private TextView zoomText;
    private String filePath;
    private Uri imageURI;
    Bitmap bm, bitmapScalled, bmScalled, cs, grid;
    Canvas comboImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_board);


        imageView = (ImageView) findViewById(R.id.drawingBoardView);

        zoomInButton = (ImageButton) findViewById(R.id.ZoomInButton);
        zoomOutButton = (ImageButton) findViewById(R.id.ZoomOutButton);
        zoomText = (TextView) findViewById(R.id.ZoomText);

        locked = (ImageView) findViewById(R.id.btn_Locked);
        unlocked = (ImageView) findViewById(R.id.btn_unLocked);

        home = (ImageView) findViewById(R.id.btn_Home);
        rainbow = (ImageView) findViewById(R.id.RainbowBottom);


//        filePath = get.GetString("imageURI");
//        imageURI = Uri.parse(filePath);
//        File f = new File(filePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);

        //System.IO.FileStream fs = new System.IO.FileStream(f.Path, System.IO.FileMode.Open);
        BitmapFactory.decodeResource(getResources(), R.drawable.sample_image, options);
        //fs.Close();


        float aspectRatio = (float) options.outWidth / (float) options.outHeight;
        int height = Math.round(getResources().getDisplayMetrics().widthPixels / aspectRatio);


        options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, getResources().getDisplayMetrics().widthPixels, height);

        options.inJustDecodeBounds = false;
        Drawable d = getResources().getDrawable(R.drawable.sample_image);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image, options);
        bm = drawableToBitmap(d);

//        bm = BitmapFactory.decodeFile(filePath, options);

        bmScalled = getResizedBitmap(bm, (int) ((float) getResources().getDisplayMetrics().widthPixels), height);//Resources.DisplayMetrics.HeightPixels);

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
        locked.setOnClickListener(this);
        unlocked.setOnClickListener(this);
        home.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ZoomInButton:
                Log.d(TAG, "ZoomInButton: " + attacher.getScale());
                if (attacher.getScale() >= 1 && attacher.getScale() < 6) {
                    if ((attacher.getScale() % 1) != 0) {
                        attacher.setScale((float) Math.ceil(attacher.getScale()));
                    } else
                        attacher.setScale(attacher.getScale() + 1f);
                    zoomText.setText(attacher.getScale() + "x");

                }
                break;
            case R.id.ZoomOutButton:
                Log.d(TAG, "ZoomOutButton: " + attacher.getScale());

                if (attacher.getScale() > 1) {
                    if ((attacher.getScale() % 1) != 0) {
                        attacher.setScale((float) Math.floor(attacher.getScale()));
                    } else
                        attacher.setScale(attacher.getScale() - 1f);
                    zoomText.setText(attacher.getScale() + "x");
                }
                break;
            case R.id.btn_Locked:
                unlocked.setVisibility(View.VISIBLE);
                locked.setVisibility(View.INVISIBLE);
                imageView.setEnabled(true);
                zoomInButton.setVisibility(View.VISIBLE);
                zoomOutButton.setVisibility(View.VISIBLE);
                zoomText.setVisibility(View.VISIBLE);
                home.setVisibility(View.VISIBLE);
                //zoomInButton.Enabled = true;
                //zoomOutButton.Enabled = true;
                break;
            case R.id.btn_unLocked:
                unlocked.setVisibility(View.INVISIBLE);
                locked.setVisibility(View.VISIBLE);
                imageView.setEnabled(false);
                zoomInButton.setVisibility(View.INVISIBLE);
                zoomOutButton.setVisibility(View.INVISIBLE);
                zoomText.setVisibility(View.INVISIBLE);
                home.setVisibility(View.INVISIBLE);
                //zoomInButton.Enabled = false;
                //zoomOutButton.Enabled = false;
                break;
            case R.id.btn_Home:
                if (getResources().getDisplayMetrics().widthPixels < 1100)
                    rainbow.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        float scale = (float) Math.round(attacher.getScale());
        if (scale > 6) {
            attacher.setScale(6f);
            zoomText.setText(6f + "x");
            return 6f;

        } else
            zoomText.setText(scale + "x");
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
        Log.d(TAG, "onScaleChange: " + scaleFactor);
    }
}
