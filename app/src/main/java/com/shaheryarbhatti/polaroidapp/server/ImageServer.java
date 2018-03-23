package com.shaheryarbhatti.polaroidapp.server;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.shaheryarbhatti.polaroidapp.R;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by shaheryarbhatti on 22/03/2018.
 */

public class ImageServer {
    private void uploadImageFile(Context context, File file) {
        String fileUrl = context.getResources().getString(R.string.file_upload_test_url);
        AndroidNetworking.post(fileUrl)
                .addHeaders("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVhYWU1Y2YxMDJiODI5MjZmODA2NjZlMSIsInByb2ZpbGVfcGljIjoic29tZV91cmwiLCJmaXJzdF9uYW1lIjoiTXVoYW1tYWQiLCJsYXN0X25hbWUiOiJQYXRlbCIsIm1pZGRsZV9uYW1lIjoiQWJkdWxNb2l6IiwibW9iaWxlX251bWJlciI6Iis5MjMzMjM0Nzg4MzIiLCJlbWFpbCI6ImFiZHVsbW9pemVuZysxMkBnbWFpbC5jb20iLCJkb2IiOiIxOTkyLTEwLTE0VDE5OjAwOjAwLjAwMFoiLCJ0eXBlIjoiYWRtaW4iLCJpYXQiOjE1MjEzODg1OTYsImV4cCI6MTU4MTM4ODUzNn0.l6XQcSGN7QvxOnnzkTNQByCAmIoHxtDj6FKxRX-2fUw")
                .addBodyParameter("type", "FILE")
                .addBodyParameter("fileType", "image")
                .addFileBody(file)
//                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

}
