package com.shaheryarbhatti.polaroidapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.shaheryarbhatti.polaroidapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class PostCreationActivity extends AppCompatActivity implements View.OnClickListener, ImagePickerCallback, VideoPickerCallback {

    private String imageUrl, base64Image, fileUploadUrl;

    private final int URL_IMAGE = 1, BASE64_IMAGE = 2;
    private final String TAG = "PostCreationActivity";
    private Button closeBtn, publishBtn;
    private ImageButton addMediaBtn;
    private EditText titleEdt;
    CharSequence mediaMenu[] = new CharSequence[]{"Add Photo From Library",
            "Take A Photo",
            "Add Video From Library",
            "Record A Video"
    };
    private String outputPath;
    private String pickedFilePath;
    private String pickedFileThumbPath;
    private final int THUMBNAIL = 10, FILE = 11;
    private final int GALLERY_PHOTO = 0, CAMERA_PHOTO = 1, GALLERY_VIDEO = 2, CAMERA_VIDEO = 3;

    private CameraImagePicker cameraImagePicker;
    private CameraVideoPicker cameraVideoPicker;
    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private ImageView postImageView;
    private String userToken = "", thumbnailUriStr, fileUriStr, sourceType, fileType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
        fileUploadUrl = getResources().getString(R.string.file_upload_test_url);
        closeBtn = findViewById(R.id.closeBtn);
        publishBtn = findViewById(R.id.publishBtn);
        addMediaBtn = findViewById(R.id.addMediaBtn);
        titleEdt = findViewById(R.id.titleEdt);
        postImageView = findViewById(R.id.postImageView);

        Permissions.check(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                "Camera and storage permissions are required because...", new Permissions.Options()
                        .setSettingsDialogTitle("Warning!").setRationaleDialogTitle("Info"),
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        Log.d(TAG, "Granted");

                    }
                });
        userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVhYWU1Y2YxMDJiODI5MjZmODA2NjZlMSIsInByb2ZpbGVfcGljIjoic29tZV91cmwiLCJmaXJzdF9uYW1lIjoiTXVoYW1tYWQiLCJsYXN0X25hbWUiOiJQYXRlbCIsIm1pZGRsZV9uYW1lIjoiQWJkdWxNb2l6IiwibW9iaWxlX251bWJlciI6Iis5MjMzMjM0Nzg4MzIiLCJlbWFpbCI6ImFiZHVsbW9pemVuZysxMkBnbWFpbC5jb20iLCJkb2IiOiIxOTkyLTEwLTE0VDE5OjAwOjAwLjAwMFoiLCJ0eXBlIjoiYWRtaW4iLCJpYXQiOjE1MjEzODg1OTYsImV4cCI6MTU4MTM4ODUzNn0.l6XQcSGN7QvxOnnzkTNQByCAmIoHxtDj6FKxRX-2fUw";
        closeBtn.setOnClickListener(this);
        publishBtn.setOnClickListener(this);
        addMediaBtn.setOnClickListener(this);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("picker_path", outputPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                outputPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void updateImageView(String path) {
        File image = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        postImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        /*if (v == closeBtn) {

        }*/
        if (v == publishBtn) {
            handleUploading();

        }
        if (v == addMediaBtn) {
            showPopUpMenu();
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        pickedFileThumbPath = list.get(0).getThumbnailPath();
        Log.d(TAG, "onImagesChosen: pickedFileThumbPath:" + pickedFileThumbPath);
        pickedFilePath = list.get(0).getOriginalPath();
        sourceType = "IMAGE";
        fileType = "POST-IMAGE";
        updateImageView(pickedFileThumbPath);

    }

    @Override
    public void onVideosChosen(List<ChosenVideo> list) {
        pickedFileThumbPath = list.get(0).getPreviewImage();
        Log.d(TAG, "onVideosChosen: pickedFileThumbPath:" + pickedFileThumbPath);
        pickedFilePath = list.get(0).getOriginalPath();
        sourceType = "VIDEO";
        fileType = "POST-VIDEO";
        updateImageView(pickedFileThumbPath);
    }

    @Override
    public void onError(String s) {

    }

    private void showPopUpMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Media");
        builder.setItems(mediaMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onMenuItemClick(which);
                    }
                }
        );
        builder.show();
    }

    public boolean onMenuItemClick(int itemPosition) {
        switch (itemPosition) {
            case GALLERY_PHOTO:
                imagePicker = new ImagePicker(this);
                imagePicker.setImagePickerCallback(this);
                imagePicker.pickImage();
                break;
            case CAMERA_PHOTO:
                cameraImagePicker = new CameraImagePicker(this);
                cameraImagePicker.setImagePickerCallback(this);
                outputPath = cameraImagePicker.pickImage();
                break;
            case GALLERY_VIDEO:
                videoPicker = new VideoPicker(this);
                videoPicker.setVideoPickerCallback(this);
                videoPicker.pickVideo();
                break;
            case CAMERA_VIDEO:
                cameraVideoPicker = new CameraVideoPicker(this);
                cameraVideoPicker.setVideoPickerCallback(this);
                outputPath = cameraVideoPicker.pickVideo();
                break;

        }
        return false;
    }

    private void handleUploading() {
//        Start uploading
        handleFileUpload(THUMBNAIL);
    }

    private void handlePostCreation() {
        Log.d(TAG, "handlePostCreation: fileUriStr: "
                + fileUriStr
                + " thumbnailUriStr: " + thumbnailUriStr
        );
        String postCreationUrl = getString(R.string.post_creation_test_url);
        AndroidNetworking.post(postCreationUrl)
                .addBodyParameter("Content-Type", "application/json")
                .addHeaders("Authorization", userToken)
                .addBodyParameter("title", titleEdt.getText().toString())
                .addBodyParameter("type", sourceType)
                .addBodyParameter("source", fileUriStr)
                .addBodyParameter("thumbnail", thumbnailUriStr)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }


    private void handleFileUpload(final int i) {
        String filePath = "";
        switch (i) {
            case THUMBNAIL:
                filePath = pickedFileThumbPath;
                break;
            case FILE:
                filePath = pickedFilePath;
                break;
        }

        File file = new File(filePath);
        AndroidNetworking.upload(fileUploadUrl)
                .addHeaders("Authorization", userToken)
                .addMultipartFile("file", file)
                .addMultipartParameter("type", "FILE")
                .addMultipartParameter("fileType", fileType)
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
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                switch (i) {
                                    case THUMBNAIL:
                                        thumbnailUriStr = dataObject.getString("URI");
                                        handleFileUpload(FILE);
                                        break;
                                    case FILE:
                                        fileUriStr = dataObject.getString("URI");
                                        handlePostCreation();
                                        break;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

    /*private void handleFileUpload(int i) {

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
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: requestCode: " + requestCode);
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            }

            if (requestCode == Picker.PICK_VIDEO_DEVICE) {
                if (videoPicker == null) {
                    videoPicker = new VideoPicker(this);
                    videoPicker.setVideoPickerCallback(this);
                }
                videoPicker.submit(data);
            }

            if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraImagePicker == null) {
                    cameraImagePicker = new CameraImagePicker(this);
                    cameraImagePicker.reinitialize(outputPath);
                    cameraImagePicker.setImagePickerCallback(this);
                }
                cameraImagePicker.submit(data);
            }

            if (requestCode == Picker.PICK_VIDEO_CAMERA) {
                if (cameraVideoPicker == null) {
                    cameraVideoPicker = new CameraVideoPicker(this);
                    cameraVideoPicker.reinitialize(outputPath);
                    cameraVideoPicker.setVideoPickerCallback(this);
                }
                cameraVideoPicker.submit(data);
            }
        }
    }
}
