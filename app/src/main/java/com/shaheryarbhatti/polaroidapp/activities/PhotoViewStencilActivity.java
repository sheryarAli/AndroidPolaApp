
package com.shaheryarbhatti.polaroidapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoViewStencilActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private final String TAG = "PhotoViewStencilAct";
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
        setSupportActionBar(toolbar);
        ((ImageView) toolbar.findViewById(R.id.logoImageView)).setVisibility(View.GONE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Stencil");
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        letDrawBtn = (Button) findViewById(R.id.letDrawBtn);
        stencilSeekBar = (SeekBar) findViewById(R.id.stencilSeekBar);
        stencilSeekBar.setMax(255);
        stencilSeekBar.setProgress(200);
        stencilSeekBar.incrementProgressBy(1);


        letDrawBtn.setOnClickListener(this);
        stencilSeekBar.setOnSeekBarChangeListener(this);


// TODO: create Bitmap in java
        /*
        Xamrin code to get & generate bimtap from intent

        Android.Net.Uri imageURI = (Android.Net.Uri)Intent.Extras.Get("imageURI");
        Bitmap bmp = MediaStore.Images.Media.GetBitmap(this.ContentResolver, imageURI);*/

        ;

        String imagePath = getIntent().getStringExtra("image");

        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        double viewWidthToBitmapWidthRatio = (double) metrics.widthPixels / (double) bmp.getWidth();

        bMapScaled = Bitmap.createScaledBitmap(bmp, metrics.widthPixels, (int) (bmp.getHeight() * viewWidthToBitmapWidthRatio), true);


        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(bMapScaled);
        originalBmp = bMapScaled.copy(bMapScaled.getConfig(), true);


    }


    @Override
    public void onClick(View v) {
        if (v == letDrawBtn) {
            Bitmap bitmap = UtilImage.getBitmapFromImageView(imageView);
            String imagePath = saveToInternalStorage(bitmap);
            Intent intent = new Intent(this, DrawingBoardActivity.class);
            intent.putExtra("imageType", 1);
            intent.putExtra("image", imagePath);
            startActivity(intent);


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



    * */


    private String saveToInternalStorage(Bitmap bitmapImage) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "stencil_" + timeStamp + ".png";
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return directory.getAbsolutePath() + "/" + fileName;
    }

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
