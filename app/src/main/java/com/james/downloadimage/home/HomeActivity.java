package com.james.downloadimage.home;

import android.os.Bundle;

import com.james.downloadimage.R;
import com.james.downloadimage.util.ActivityUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private HomePresenter mHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), homeFragment, R.id.contentFrame);
        }

        mHomePresenter = new HomePresenter(homeFragment);
    }
}
