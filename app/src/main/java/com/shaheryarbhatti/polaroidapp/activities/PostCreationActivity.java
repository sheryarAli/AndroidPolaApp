package com.shaheryarbhatti.polaroidapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.shaheryarbhatti.polaroidapp.R;

import java.io.File;

public class PostCreationActivity extends AppCompatActivity {

    private String imageUrl, base64Image, fileUploadUrl;

    private final int URL_IMAGE = 1, BASE64_IMAGE = 2;
    private final String TAG = "PostCreationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
        fileUploadUrl = getResources().getString(R.string.file_upload_test_url);
    }


    private void handlePostCreation() {

    }

    private void handleFileUpload() {

        File imagefile = new File("");
        String authorization = "";
        AndroidNetworking.upload(fileUploadUrl)
                .addHeaders("Authorization", authorization)
                .addMultipartFile("file", imagefile)
                .addMultipartParameter("type", "FILE")
                .addMultipartParameter("fileType", "USER")
//                .addBodyParameter("")
//                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {

                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        Log.d(TAG, "onProgress: totalBytes: " + totalBytes + " bytesUploaded: " + bytesUploaded);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

    private void handleFileUpload(int i) {

        String authorization = "";
        ANRequest.PostRequestBuilder postRequestBuilder = new ANRequest.PostRequestBuilder(fileUploadUrl);
        switch (i) {
            case URL_IMAGE:
                String imageUrl = "";
                postRequestBuilder.addBodyParameter("type", "URI");
                postRequestBuilder.addBodyParameter("URI", imageUrl);
                break;
            case BASE64_IMAGE:
                String base64Str = "";
                postRequestBuilder.addBodyParameter("type", "BASE64");
                postRequestBuilder.addBodyParameter("fileb64", base64Str);
                break;
        }
        postRequestBuilder.addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", authorization)
                .addBodyParameter("fileType", "USER")
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.d(TAG, "onProgress: totalBytes: " + totalBytes + " bytesUploaded: " + bytesUploaded);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }
}
