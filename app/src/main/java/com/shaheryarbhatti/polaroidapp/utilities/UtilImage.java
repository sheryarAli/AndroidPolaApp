package com.shaheryarbhatti.polaroidapp.utilities;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class UtilImage {
    private final String TAG = "UtilImage";
    private static UtilImage utilImage = null;

    private UtilImage() {
    }

    public static UtilImage getInstance() {
        if (utilImage == null) {
            utilImage = new UtilImage();
        }
        return utilImage;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public Bitmap getBitmap(Context context, String imageName) {
        int drawableResId = context.getResources().getIdentifier(imageName,
                "drawable", context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("Unsupported drawable type");
        }
    }

    public String generateDownloadPath(String fileName) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File newFile = new File(file, fileName);
        return newFile.getAbsolutePath();
    }

    public Bitmap createVideoThumbNail(String videoName) {

        return ThumbnailUtils.createVideoThumbnail(generateDownloadPath(videoName), MediaStore.Video.Thumbnails.MINI_KIND);
    }

    public int getDrawableId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName,
                "drawable", context.getPackageName());
    }

    public void loadImageWithPicasso(Context context, ImageView imageView, String imageName) {
        int drawableId = getDrawableId(context, imageName);
        Picasso.with(context).load(drawableId).into(imageView);
    }

    public void loadThumbnailWithPicasso(Context context, ImageView imageView, String thumbnailUrl) {

        Picasso.with(context).load(thumbnailUrl).into(imageView);
    }

    public Bitmap getImageBitmapFromGallery(Context context, Uri selectedImage) {
        ContentResolver resolver = context.getContentResolver();
        List uriPath = selectedImage.getPathSegments();
        long imageId = Long.parseLong(uriPath.get(uriPath.size() - 1) + "");
        Bitmap thumb = MediaStore.Images.Thumbnails.getThumbnail(resolver, imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
        return thumb;
    }


    public String getImagePath(Context context, Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;

    }


    public Bitmap getImageBitmapFromCamera(Bundle data) {
        return (Bitmap) data.get("data");

    }

    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }

    public Bitmap byteToBitmap(byte[] bitmapData) {
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
    }

    public Bitmap getBitmapFromImageView(ImageView imageView) {

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    public void setPicFromDrawableResuource(Context context, ImageView imageView, int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        float aspectRatio = (float) options.outWidth / (float) options.outHeight;
        int height = Math.round(context.getResources().getDisplayMetrics().widthPixels / aspectRatio);
        options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, context.getResources().getDisplayMetrics().widthPixels, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, options);

        imageView.setImageBitmap(bitmap);

    }

    public Bitmap getBitmapFromDrawableResuource(Context context, int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        float aspectRatio = (float) options.outWidth / (float) options.outHeight;
        int height = Math.round(context.getResources().getDisplayMetrics().widthPixels / aspectRatio);
        options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, context.getResources().getDisplayMetrics().widthPixels, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, options);

        return bitmap;

    }

    public void setPicFromPath(Context context, ImageView imageView, String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        float aspectRatio = (float) options.outWidth / (float) options.outHeight;
        int height = Math.round(context.getResources().getDisplayMetrics().widthPixels / aspectRatio);
        options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, context.getResources().getDisplayMetrics().widthPixels, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        Log.d("UtilImage", "setPicFromPath: For Debugging");

        imageView.setImageBitmap(getRotatedBitmap(context, imagePath, bitmap));

    }

    public Bitmap getRotatedBitmap(Context context, String filePath, Bitmap orignalBitmap) {
        try {
           /* File file = new File(filePath);
            Uri imageUri = Uri.fromFile(file);
            Cursor cursor = context.getContentResolver().query(imageUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

            cursor.moveToFirst();
            Log.d(TAG, "getRotatedBitmap: rotation from cursor: "+ cursor.getInt(0));*/
            ExifInterface exif = new ExifInterface(filePath);
            Log.d(TAG, "getRotatedBitmap: orientation in String: " + exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            Log.d(TAG, "getRotatedBitmap: orientation in Double: " + exif.getAttributeDouble(ExifInterface.TAG_ORIENTATION, 0));
            float rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d(TAG, "getRotatedBitmap: rotation: " + rotation);
            float rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationInDegrees);
            return Bitmap.createBitmap(orignalBitmap, 0, 0, orignalBitmap.getWidth(), orignalBitmap.getHeight(), matrix, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private float exifToDegrees(float exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private double getScale(int originalWidth, int originalHeight, int requiredWidth, int requiredHeight) {
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

    public int CalculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        float height = options.outHeight;
        float width = options.outWidth;
        double inSampleSize = 1D;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = (int) (height / 2);
            int halfWidth = (int) (width / 2);

            // Calculate a inSampleSize that is a power of 2 - the decoder will use a value that is a power of two anyway.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

        }

        return (int) inSampleSize;
    }

    public Bitmap loadScaledDownBitmapForDisplay(Context context, int drawableId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        // Calculate inSampleSize
        options.inSampleSize = CalculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), drawableId, options);
    }


}
