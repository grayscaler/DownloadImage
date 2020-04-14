package com.james.downloadimage.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.james.downloadimage.R;
import com.james.downloadimage.data.Photo;
import com.james.downloadimage.paing.PhotoDiffUtils;
import com.james.downloadimage.util.ScreenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhotoAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private Context mContext;
    private CompositeDisposable mCompositeDisposable;
    private SparseArray<Disposable> mRequestDisposable = new SparseArray<>();
    private SparseArray<WeakReference<Drawable>> mPhotoThumbnailsCache = new SparseArray<>();

    public PhotoAdapter(PhotoDiffUtils photoDiffUtils, CompositeDisposable compositeDisposable) {
        super(photoDiffUtils);
        mCompositeDisposable = compositeDisposable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = getView(parent);
        return new PhotoViewHolder(view);
    }

    private View getView(@NonNull ViewGroup parent) {
        DisplayMetrics displaymetrics = ScreenUtils.getDisplayMetrics(mContext);
        int deviceWidth = displaymetrics.widthPixels / 4;
        int deviceHeight = displaymetrics.widthPixels / 4;

        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = deviceWidth;
        params.height = deviceHeight;
        view.setLayoutParams(params);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Photo photo = getItem(position);
        final PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;

        // cancel running request
        if (holder.itemView.getTag(R.id.image) != null) {
            int id = (int) holder.itemView.getTag(R.id.image);
            if (mRequestDisposable.get(id) != null) {
                mRequestDisposable.get(id).dispose();
            }
        }

        int photoId = 0;
        String photoTitle = "";
        if (photo != null) {
            photoId = photo.getId();
            photoTitle = photo.getTitle();
        }

        holder.itemView.setTag(R.id.image, photoId);

        photoViewHolder.mImage.setImageDrawable(null);

        final int finalPhotoId = photoId;
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(final ObservableEmitter<Drawable> e) throws Exception {
                WeakReference<Drawable> drawableSoftReference = mPhotoThumbnailsCache.get(finalPhotoId);
                if (drawableSoftReference == null || drawableSoftReference.get() == null) {
                    if (photo != null) {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.newCall(new Request.Builder().url(photo.getThumbnailUrl()).build())
                                .enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    }

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {
                                                InputStream inputStream = response.body().byteStream();
                                                BitmapFactory.Options bitmapLoadingOptions = new BitmapFactory.Options();
                                                bitmapLoadingOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                                                Drawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeStream(inputStream, null, bitmapLoadingOptions));
                                                e.onNext(drawable);
                                            }
                                        }
                                    }
                                });
                    }
                } else {
                    e.onNext(drawableSoftReference.get());
                }
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Drawable>() {
                    @Override
                    public void accept(Drawable drawable) throws Exception {
                        photoViewHolder.mImage.setImageDrawable(drawable);
                    }
                });

        mCompositeDisposable.add(disposable);
        mRequestDisposable.put(photo.getId(), disposable);

        photoViewHolder.mId.setText(String.valueOf(photoId));
        photoViewHolder.mTitle.setText(photoTitle);
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mId;
        TextView mTitle;

        PhotoViewHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.image);
            mId = view.findViewById(R.id.tv_id);
            mTitle = view.findViewById(R.id.tv_title);
            TextViewCompat.setAutoSizeTextTypeWithDefaults(mTitle, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
    }
}
