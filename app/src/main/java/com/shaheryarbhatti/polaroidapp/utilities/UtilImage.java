package com.shaheryarbhatti.polaroidapp.utilities;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class UtilImage {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(Context context, String imageName) {
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

    public static int getDrawableId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName,
                "drawable", context.getPackageName());
    }

    public static void loadImageWithPicasso(Context context, ImageView imageView, String imageName) {
        int drawableId = getDrawableId(context, imageName);
        Picasso.with(context).load(drawableId).fit().into(imageView);
    }

    public static void loadThumbnailWithPicasso(Context context, ImageView imageView, String thumbnailUrl) {

        Picasso.with(context).load(thumbnailUrl).fit().into(imageView);
    }

    public static Bitmap getImageBitmapFromGallery(Context context, Uri selectedImage) {
        ContentResolver resolver = context.getContentResolver();
        List uriPath = selectedImage.getPathSegments();
        long imageId = Long.parseLong(uriPath.get(uriPath.size() - 1) + "");
        Bitmap thumb = MediaStore.Images.Thumbnails.getThumbnail(resolver, imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
        return thumb;
    }


    public static String getImagePath(Context context, Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;

    }


    public static Bitmap getImageBitmapFromCamera(Bundle data) {
        return (Bitmap) data.get("data");

    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] bitmapData) {
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
    }

    public static Bitmap getBitmapFromImageView(ImageView imageView) {

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    public static void setPicFromPath(Context context, ImageView imageView, String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        float aspectRatio = (float) options.outWidth / (float) options.outHeight;
        int height = Math.round(context.getResources().getDisplayMetrics().widthPixels / aspectRatio);
        options.inSampleSize = (int) getScale(options.outWidth, options.outHeight, context.getResources().getDisplayMetrics().widthPixels, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        Log.d("UtilImage", "setPicFromPath: For Debugging");
        imageView.setImageBitmap(bitmap);

    }

    private static double getScale(int originalWidth, int originalHeight, int requiredWidth, int requiredHeight) {
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


}
