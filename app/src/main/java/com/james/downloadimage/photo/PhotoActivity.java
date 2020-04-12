package com.james.downloadimage.photo;

import android.os.Bundle;

import com.james.downloadimage.R;
import com.james.downloadimage.util.ActivityUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PhotoActivity extends AppCompatActivity {

    private PhotoPresenter mPhotoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

        PhotoFragment photoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (photoFragment == null) {
            photoFragment = PhotoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), photoFragment, R.id.contentFrame);
        }

        mPhotoPresenter = new PhotoPresenter(photoFragment);
    }
}
