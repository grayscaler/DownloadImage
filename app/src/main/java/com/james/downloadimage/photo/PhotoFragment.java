package com.james.downloadimage.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.downloadimage.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PhotoFragment extends Fragment implements PhotoContract.View {

    private PhotoContract.Presenter mPresenter;

    public PhotoFragment() {
    }

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    @Override
    public void setPresenter(PhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_frag, container, false);

        return root;
    }
}
