package com.james.downloadimage.photo;

import com.james.downloadimage.BasePresenter;
import com.james.downloadimage.BaseView;
import com.james.downloadimage.data.Photo;

import androidx.paging.PagedList;

public interface PhotoContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<PhotoContract.Presenter> {

        void setProgressBarVisibility(int visibility);

        void showPhotos(PagedList<Photo> photos);
    }
}
