package com.shaheryarbhatti.polaroidapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shaheryarbhatti on 19/12/2017.
 */

public class Util {

    public static Bitmap getBitmapFromResource(Context context, String imageName) {

        int resourceId = context.getResources().getIdentifier(imageName,
                "drawable", context.getPackageName());
        return BitmapFactory.decodeResource(context.getResources(), resourceId);

    }
}
