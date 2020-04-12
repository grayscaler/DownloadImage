package com.james.downloadimage.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.james.downloadimage.R;
import com.james.downloadimage.data.Photo;
import com.james.downloadimage.paing.PhotoDiffUtils;
import com.james.downloadimage.util.ScreenUtils;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private Context mContext;

    public PhotoAdapter(PhotoDiffUtils photoDiffUtils) {
        super(photoDiffUtils);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.photo_item, parent, false);

        DisplayMetrics displaymetrics = ScreenUtils.getDisplayMetrics(mContext);
        int deviceWidth = displaymetrics.widthPixels / 4;
        int deviceHeight = displaymetrics.widthPixels / 4;

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = deviceWidth;
        params.height = deviceHeight;
        view.setLayoutParams(params);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Photo photo = getItem(position);
        PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;

        Glide.with(mContext)
                .load(photo.getThumbnailUrl())
                .into(photoViewHolder.mImage);
        photoViewHolder.mId.setText(String.valueOf(photo.getId()));
        photoViewHolder.mTitle.setText(photo.getTitle());
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mId;
        TextView mTitle;

        public PhotoViewHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.image);
            mId = view.findViewById(R.id.tv_id);
            mTitle = view.findViewById(R.id.tv_title);
            TextViewCompat.setAutoSizeTextTypeWithDefaults(mTitle, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
    }
}
