package com.james.downloadimage.colors;

public class ColorsPresenter implements ColorsContract.Presenter {

    private final ColorsContract.View mView;

    public ColorsPresenter(ColorsContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }
}
