package com.james.downloadimage.data.source;

import com.james.downloadimage.data.Photo;

import androidx.paging.PagedList;
import io.reactivex.Observable;

public interface JsonPlaceholderDataSource {

    Observable<PagedList<Photo>> getPhotosObservable();
}
