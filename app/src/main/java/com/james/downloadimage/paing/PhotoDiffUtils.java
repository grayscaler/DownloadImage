package com.james.downloadimage.paing;

import com.james.downloadimage.data.Photo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class PhotoDiffUtils extends DiffUtil.ItemCallback<Photo> {


    @Override
    public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
        return oldItem.equals(newItem);
    }
}
