package com.james.downloadimage.colors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.downloadimage.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ColorsFragment extends Fragment implements ColorsContract.View {

    private ColorsContract.Presenter mPresenter;

    public ColorsFragment() {
    }

    public static ColorsFragment newInstance() {
        return new ColorsFragment();
    }

    @Override
    public void setPresenter(ColorsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.colors_frag, container, false);

        return root;
    }
}
