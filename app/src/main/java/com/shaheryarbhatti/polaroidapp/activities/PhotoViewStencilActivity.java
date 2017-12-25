
package com.shaheryarbhatti.polaroidapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.shaheryarbhatti.polaroidapp.R;

public class PhotoViewStencilActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private ImageView imageView;
    private Bitmap bMapScaled, originalBmp;
    private SeekBar stencilSeekBar;
    private Canvas canvas;
    private Toolbar toolbar;
    private Button letDrawBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_stencil);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        letDrawBtn = (Button) findViewById(R.id.letDrawBtn);
        stencilSeekBar = (SeekBar) findViewById(R.id.stencilSeekBar);
        stencilSeekBar.setMax(255);
        stencilSeekBar.setProgress(200);
        stencilSeekBar.incrementProgressBy(1);


        letDrawBtn.setOnClickListener(this);
        stencilSeekBar.setOnSeekBarChangeListener(this);


        setSupportActionBar(toolbar);

// TODO: create Bitmap in java
        /*
        Xamrin code to get & generate bimtap from intent

        Android.Net.Uri imageURI = (Android.Net.Uri)Intent.Extras.Get("imageURI");
        Bitmap bmp = MediaStore.Images.Media.GetBitmap(this.ContentResolver, imageURI);*/

        ;

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image2);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        double viewWidthToBitmapWidthRatio = (double) metrics.widthPixels / (double) bmp.getWidth();

        bMapScaled = Bitmap.createScaledBitmap(bmp, metrics.widthPixels, (int) (bmp.getHeight() * viewWidthToBitmapWidthRatio), true);
//        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(bMapScaled);
        originalBmp = bMapScaled.copy(bMapScaled.getConfig(), true);


    }


    @Override
    public void onClick(View v) {
        if (v == letDrawBtn) {
            canvas = new Canvas(bMapScaled);
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(getColorMatrix()));
            canvas.drawBitmap(bMapScaled, 0, 0, paint);

            imageView.setImageBitmap(bMapScaled);

        }
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        bMapScaled = originalBmp.copy(originalBmp.getConfig(), true);
        canvas = new Canvas(bMapScaled);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(getColorMatrix(progress)));
        canvas.drawBitmap(bMapScaled, 0, 0, paint);
        imageView.setImageBitmap(bMapScaled);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //TODO: changeOrientation
    /*
    Xamrin code to chagneOrientaionFile
    *         Bitmap changeOrientation(File mediafile, Bitmap bitmap, Android.Net.Uri imageUri)
        {
            var cursor = this.ContentResolver.Query(imageUri, new string[] { MediaStore.Images.ImageColumns.Orientation }, null, null, null);

            cursor.MoveToFirst();

            var exifInterface = new ExifInterface(imageUri.Path);
            int orientation = exifInterface.GetAttributeInt(ExifInterface.TagOrientation, 1);
            var matrix = new Matrix();
            matrix.SetRotate(cursor.GetInt(0));

            try
            {
                Bitmap oriented = Bitmap.CreateBitmap(bitmap, 0, 0, bitmap.Width, bitmap.Height, matrix, true);
                bitmap.Recycle();
                return oriented;
            }
            catch (OutOfMemoryError e)
            {
                e.PrintStackTrace();
                return bitmap;
            }
            catch (System.Exception e)
            {
                System.Diagnostics.Debug.WriteLine(e.Message);
                return bitmap;
            }
        }
        //TODO: CreateDirectoryForPictures in Java

        Xamrin code creating files

        private File CreateDirectoryForPictures(string dirName)
        {
            File file = new File(
                 Android.OS.Environment.GetExternalStoragePublicDirectory(
                     Android.OS.Environment.DirectoryPictures), dirName);
            if (!file.Exists())
            {
                file.Mkdirs();
            }

            return file;
        }

    * */

    private ColorMatrix getColorMatrix() {
        return getColorMatrix(128);
    }

    private ColorMatrix getColorMatrix(int progress) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        float m = 255f;
        float t = -128f * progress;
        ColorMatrix threshold = new ColorMatrix(new float[]{
                m, 0, 0, 1, t,
                0, m, 0, 1, t,
                0, 0, m, 1, t,
                0, 0, 0, 1, 0
        });

        colorMatrix.postConcat(threshold);
        return colorMatrix;

    }
}
