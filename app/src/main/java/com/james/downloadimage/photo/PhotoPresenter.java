package com.james.downloadimage.photo;

import android.view.View;

import com.james.downloadimage.data.Photo;
import com.james.downloadimage.data.source.JsonPlaceholderRepository;

import androidx.paging.PagedList;
import io.reactivex.functions.Consumer;

public class PhotoPresenter implements PhotoContract.Presenter {

    private final PhotoContract.View mView;
    private final JsonPlaceholderRepository mJsonPlaceholderRepository;

    PhotoPresenter(PhotoContract.View view, JsonPlaceholderRepository jsonPlaceholderRepository) {
        mView = view;
        mJsonPlaceholderRepository = jsonPlaceholderRepository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPhotos();
    }

    private void loadPhotos() {
        mView.addDisposable(mJsonPlaceholderRepository.getPhotosObservable()
                .subscribe(new Consumer<PagedList<Photo>>() {
                    @Override
                    public void accept(PagedList<Photo> photos) {
                        mView.setProgressBarVisibility(View.GONE);
                        mView.showPhotos(photos);
                    }
                }));
    }
}
