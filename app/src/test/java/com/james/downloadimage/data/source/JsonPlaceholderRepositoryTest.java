package com.james.downloadimage.data.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.james.downloadimage.data.Photo;
import com.james.downloadimage.paing.PhotoPagingDataSourceFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import androidx.paging.PagedList;
import io.reactivex.functions.Consumer;

import static com.james.downloadimage.SetUp.setUpRxSchedulers;

public class JsonPlaceholderRepositoryTest {

    private JsonPlaceholderRepository mJsonPlaceholderRepository;
    private Gson mGson;

    @Before
    public void setupGitHubRepository() {
        mJsonPlaceholderRepository = JsonPlaceholderRepository.getInstance(new PhotoPagingDataSourceFactory());
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }

    @BeforeClass
    public static void setUp() {
        setUpRxSchedulers();
    }

    @Test
    public void getPhotosObservable() {
        mJsonPlaceholderRepository.getPhotosObservable()
                .subscribe(new Consumer<PagedList<Photo>>() {
                    @Override
                    public void accept(PagedList<Photo> photos) {
                        System.out.println(mGson.toJson(photos));
                        System.out.println(mGson.toJson(photos.size()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        System.out.println(mGson.toJson(throwable));
                    }
                });
    }
}