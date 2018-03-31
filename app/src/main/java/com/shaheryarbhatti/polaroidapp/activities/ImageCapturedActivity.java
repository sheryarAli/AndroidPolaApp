package com.shaheryarbhatti.polaroidapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

public class ImageCapturedActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "ImageCapturedActivity";
    private ImageView imageView;
    private Button stencilizeBtn, drawBtn;
    private Toolbar toolbar;
    private Bitmap bitmap;
    private String imagePath;
    private UtilImage utilImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_captured);
        initViews();
        utilImage = UtilImage.getInstance();
        ((ImageView) toolbar.findViewById(R.id.logoImageView)).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        prepareViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        drawBtn = (Button) findViewById(R.id.drawBtn);
        stencilizeBtn = (Button) findViewById(R.id.stencilizeBtn);
        drawBtn.setOnClickListener(this);
        stencilizeBtn.setOnClickListener(this);
    }

    private void prepareViews() {

        imagePath = getIntent().getStringExtra("image");
//        Picasso.with(this).load(imagePath).fit().into(imageView);
        utilImage.setPicFromPath(this, imageView, imagePath);


    }

    @Override
    public void onClick(View v) {
        if (v == stencilizeBtn) {
            moveToSetencilizeActivity();
        }
        if (v == drawBtn) {
            moveToDrawingBoardActivity();
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

    private void moveToSetencilizeActivity() {

        Intent intent = new Intent(ImageCapturedActivity.this, PhotoViewStencilActivity.class);
        intent.putExtra("imageType", 1);
        intent.putExtra("image", imagePath);
        startActivity(intent);

    }

    private void moveToDrawingBoardActivity() {

        Intent intent = new Intent(ImageCapturedActivity.this, DrawingBoardActivity.class);
        intent.putExtra("imageType", 1);
        intent.putExtra("image", imagePath);
        startActivity(intent);

    }
}
