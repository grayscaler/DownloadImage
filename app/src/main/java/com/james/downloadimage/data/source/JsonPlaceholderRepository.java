package com.james.downloadimage.data.source;


import com.james.downloadimage.data.Photo;
import com.james.downloadimage.paing.PhotoPagingDataSourceFactory;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.james.downloadimage.Constants.Constants.API_PAGE_SIZE;
import static com.james.downloadimage.Constants.Constants.API_PREFETCH_DISTANCE;

public class JsonPlaceholderRepository implements JsonPlaceholderDataSource {

    private volatile static JsonPlaceholderRepository INSTANCE = null;
    private final PhotoPagingDataSourceFactory mPhotoPagingDataSourceFactory;
    private final PagedList.Config mPagedListConfig;

    private JsonPlaceholderRepository(PhotoPagingDataSourceFactory photoPagingDataSourceFactory) {
        mPhotoPagingDataSourceFactory = photoPagingDataSourceFactory;

        mPagedListConfig = new PagedList.Config.Builder()
                .setPageSize(API_PAGE_SIZE)
                .setPrefetchDistance(API_PREFETCH_DISTANCE)
                .build();
    }

    public static JsonPlaceholderRepository getInstance(PhotoPagingDataSourceFactory photoPagingDataSourceFactory) {
        if (INSTANCE == null) {
            synchronized (JsonPlaceholderRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JsonPlaceholderRepository(photoPagingDataSourceFactory);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<PagedList<Photo>> getPhotosObservable() {
        return new RxPagedListBuilder(mPhotoPagingDataSourceFactory, mPagedListConfig)
                .setFetchScheduler(Schedulers.io())
                .setNotifyScheduler(AndroidSchedulers.mainThread())
                .buildObservable();
    }
}
