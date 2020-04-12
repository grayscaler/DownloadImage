package com.james.downlaodimage.home;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;

    public HomePresenter(HomeContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }
}
