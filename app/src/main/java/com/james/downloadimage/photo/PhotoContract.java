package com.james.downloadimage.photo;

import com.james.downloadimage.BasePresenter;
import com.james.downloadimage.BaseView;

public interface PhotoContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<PhotoContract.Presenter> {

    }
}
