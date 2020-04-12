package com.james.downloadimage.colors;

import android.os.Bundle;

import com.james.downloadimage.R;
import com.james.downloadimage.util.ActivityUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ColorsActivity extends AppCompatActivity {

    private ColorsPresenter mColorsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colors_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

        ColorsFragment colorsFragment = (ColorsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (colorsFragment == null) {
            colorsFragment = ColorsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), colorsFragment, R.id.contentFrame);
        }

        mColorsPresenter = new ColorsPresenter(colorsFragment);
    }
}
