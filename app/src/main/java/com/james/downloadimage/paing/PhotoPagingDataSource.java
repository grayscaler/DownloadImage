package com.james.downloadimage.paing;

import com.james.downloadimage.data.Photo;
import com.james.downloadimage.data.source.remote.JsonPlaceholderRemoteDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

import static com.james.downloadimage.Constants.Constants.API_PAGE_INIT;
import static com.james.downloadimage.Constants.Constants.API_PAGE_SIZE;
import static com.james.downloadimage.Constants.Constants.API_QUERY_KEY_PAGE;
import static com.james.downloadimage.Constants.Constants.API_QUERY_KEY_PAGE_SIZE;

public class PhotoPagingDataSource extends PageKeyedDataSource<String, Photo> {

    private final JsonPlaceholderRemoteDataSource mJsonPlaceholderRemoteDataSource;
    private Map<String, String> mOptions;
    private final CompositeDisposable mDisposable;

    PhotoPagingDataSource() {
        mJsonPlaceholderRemoteDataSource = JsonPlaceholderRemoteDataSource.getInstance();

        mOptions = new HashMap<>();
        mOptions.put(API_QUERY_KEY_PAGE_SIZE, String.valueOf(API_PAGE_SIZE));

        mDisposable = new CompositeDisposable();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Photo> callback) {
        mOptions.put(API_QUERY_KEY_PAGE, String.valueOf(API_PAGE_INIT));
        mDisposable.add(mJsonPlaceholderRemoteDataSource.rxGetPhotos(mOptions)
                .subscribe(new Consumer<Response<List<Photo>>>() {
                    @Override
                    public void accept(Response<List<Photo>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            List<Photo> photos = listResponse.body();
                            String nextPage = String.valueOf(Integer.valueOf(mOptions.get(API_QUERY_KEY_PAGE)) + 1);
                            callback.onResult(photos, null, nextPage);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Photo> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull final LoadCallback<String, Photo> callback) {
        String page = params.key;
        if (page != null && !page.isEmpty()) {
            mOptions.put(API_QUERY_KEY_PAGE, page);
            mDisposable.add(mJsonPlaceholderRemoteDataSource.rxGetPhotos(mOptions)
                    .subscribe(new Consumer<Response<List<Photo>>>() {
                        @Override
                        public void accept(Response<List<Photo>> listResponse) {
                            if (listResponse.isSuccessful()) {
                                List<Photo> photos = listResponse.body();
                                String nextPage = String.valueOf(Integer.valueOf(mOptions.get(API_QUERY_KEY_PAGE)) + 1);
                                callback.onResult(photos, nextPage);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {

                        }
                    }));
        }
    }


}