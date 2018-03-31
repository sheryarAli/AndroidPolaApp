package com.shaheryarbhatti.polaroidapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

public class MadeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView madeImageView;
    private UtilImage utilImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initViews();
        utilImage = UtilImage.getInstance();
        ((ImageView) toolbar.findViewById(R.id.logoImageView)).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Made");
        }
        prepareView();

    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        madeImageView = (ImageView) findViewById(R.id.madeImageView);
    }

    private void prepareView() {
        String madeSource = getIntent().getStringExtra("made");
        utilImage.loadImageWithPicasso(this, madeImageView, madeSource);
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

}
