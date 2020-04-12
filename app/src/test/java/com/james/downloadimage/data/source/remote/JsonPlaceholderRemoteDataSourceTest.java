package com.james.downloadimage.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.james.downloadimage.data.Photo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import retrofit2.Response;

import static com.james.downloadimage.Constants.Constants.API_PAGE_INIT;
import static com.james.downloadimage.Constants.Constants.API_PAGE_SIZE;
import static com.james.downloadimage.Constants.Constants.API_QUERY_KEY_PAGE;
import static com.james.downloadimage.Constants.Constants.API_QUERY_KEY_PAGE_SIZE;
import static com.james.downloadimage.SetUp.setUpRxSchedulers;

public class JsonPlaceholderRemoteDataSourceTest {

    private JsonPlaceholderRemoteDataSource mJsonPlaceholderRemoteDataSource;
    private Gson mGson;

    @Before
    public void setupGitHubRepository() {
        mJsonPlaceholderRemoteDataSource = JsonPlaceholderRemoteDataSource.getInstance();
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }

    @BeforeClass
    public static void setUp() {
        setUpRxSchedulers();
    }

    @Test
    public void rxGetPhotos() {
        Map<String, String> options = new HashMap<>();
        options.put(API_QUERY_KEY_PAGE, String.valueOf(API_PAGE_INIT));
        options.put(API_QUERY_KEY_PAGE_SIZE, String.valueOf(API_PAGE_SIZE));
        mJsonPlaceholderRemoteDataSource.rxGetPhotos(options)
                .subscribe(new Consumer<Response<List<Photo>>>() {
                    @Override
                    public void accept(Response<List<Photo>> listResponse) {
                        System.out.println(mGson.toJson(listResponse));
                        System.out.println(mGson.toJson(listResponse.body()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        System.out.println(mGson.toJson(throwable));
                    }
                });
    }
}