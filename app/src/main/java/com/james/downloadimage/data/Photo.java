package com.james.downloadimage.data;

import androidx.annotation.Nullable;

public class Photo {

    /**
     * albumId : 1
     * id : 21
     * title : ad et natus qui
     * url : https://via.placeholder.com/600/5e12c6
     * thumbnailUrl : https://via.placeholder.com/150/5e12c6
     */

    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        Photo photo = (Photo) obj;
        return photo.id == this.id;
    }
}
