package com.james.downloadimage.photo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.james.downloadimage.R;
import com.james.downloadimage.adapter.PhotoAdapter;
import com.james.downloadimage.data.Photo;
import com.james.downloadimage.paing.PhotoDiffUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.james.downloadimage.Constants.Constants.GRID_LAYOUT_SPAN_COUNT;

public class PhotoFragment extends Fragment implements PhotoContract.View {

    private PhotoContract.Presenter mPresenter;
    private Context mContext;
    private PhotoAdapter mPhotoAdapter;
    private ProgressBar mProgressBar;

    public PhotoFragment() {
    }

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    @Override
    public void setPresenter(PhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoAdapter = new PhotoAdapter(new PhotoDiffUtils());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_frag, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, GRID_LAYOUT_SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mPhotoAdapter);

        mProgressBar = root.findViewById(R.id.progressBar);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    @Override
    public void showPhotos(PagedList<Photo> photos) {
        mPhotoAdapter.submitList(photos);
    }
}
