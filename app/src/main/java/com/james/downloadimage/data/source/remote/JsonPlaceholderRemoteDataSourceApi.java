package com.james.downloadimage.data.source.remote;


import com.james.downloadimage.data.Photo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface JsonPlaceholderRemoteDataSourceApi {

    @GET("photos")
    Observable<Response<List<Photo>>> rxGetPhotos(@QueryMap Map<String, String> options);
}
