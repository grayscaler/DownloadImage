package com.james.downloadimage.data.source.remote;

import com.james.downloadimage.BuildConfig;
import com.james.downloadimage.data.Photo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class JsonPlaceholderRemoteDataSource {

    private volatile static JsonPlaceholderRemoteDataSource INSTANCE;
    private JsonPlaceholderRemoteDataSourceApi mApi;

    public static JsonPlaceholderRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (JsonPlaceholderRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JsonPlaceholderRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private JsonPlaceholderRemoteDataSource() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        mApi = retrofit.create(JsonPlaceholderRemoteDataSourceApi.class);
    }

    public Observable<Response<List<Photo>>> rxGetPhotos(Map<String, String> options) {
        return mApi.rxGetPhotos(options);
    }
}
