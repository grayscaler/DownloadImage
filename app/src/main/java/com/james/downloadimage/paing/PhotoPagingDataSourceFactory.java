package com.james.downloadimage.paing;

import com.james.downloadimage.data.Photo;

import androidx.paging.DataSource;

public class PhotoPagingDataSourceFactory extends DataSource.Factory<String, Photo> {

    @Override
    public DataSource<String, Photo> create() {
        return new PhotoPagingDataSource();
    }
}
