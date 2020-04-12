package com.james.downloadimage.photo;

public class PhotoPresenter implements PhotoContract.Presenter {

    private final PhotoContract.View mView;

    public PhotoPresenter(PhotoContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }
}
