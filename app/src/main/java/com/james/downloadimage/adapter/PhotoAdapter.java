package com.james.downloadimage.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.lang.ref.SoftReference;

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

    public static final String TAG = PhotoAdapter.class.getSimpleName();

    private Context mContext;
    private CompositeDisposable mCompositeDisposable;
    private SparseArray<Disposable> mRequestDisposable = new SparseArray<>();
    private SparseArray<SoftReference<Drawable>> mPhotoThumbnailsCache = new SparseArray<>();

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

//        Observable.just(photoId)
//                .map(new Function<Integer, SoftReference<Drawable>>() {
//                    @Override
//                    public SoftReference<Drawable> apply(Integer integer){
//                        SoftReference<Drawable> drawableSoftReference = mPhotoThumbnailsCache.get(integer);
//                        if (drawableSoftReference == null || drawableSoftReference.get() == null) {
//                            if (photo != null) {
//                                loadDrawableFromUrl(photo, photoViewHolder);
//                            }
//                            return null;
//                        } else {
//                            return mPhotoThumbnailsCache.get(integer);
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<SoftReference<Drawable>>() {
//                    @Override
//                    public void accept(SoftReference<Drawable> drawableSoftReference){
//                        if (drawableSoftReference != null) {
//                            photoViewHolder.mImage.setImageDrawable(drawableSoftReference.get());
//                        }
//                    }
//                }).dispose();

        SoftReference<Drawable> drawableWeakReference = mPhotoThumbnailsCache.get(photoId);
        if (drawableWeakReference == null || drawableWeakReference.get() == null) {
            Log.d(TAG, "onBindViewHolder: drawableWeakReference == null id:" + photoId);
            if (photo != null) {
                loadDrawableFromUrl(photo, photoViewHolder);
            }
        } else {
            Log.d(TAG, "onBindViewHolder: drawableWeakReference != null id:" + photoId);
            photoViewHolder.mImage.setImageDrawable(drawableWeakReference.get());
        }

        photoViewHolder.mId.setText(String.valueOf(photoId));
        photoViewHolder.mTitle.setText(photoTitle);
    }

    private void loadDrawableFromUrl(final Photo photo, final PhotoViewHolder photoViewHolder) {
        Disposable disposable = drawableFromUrl(photo.getThumbnailUrl())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Drawable>() {
                    @Override
                    public void accept(Drawable drawable) {
                        Log.d(TAG, "accept: drawable:" + drawable + " photo.getId():" + photo.getId());
                        photoViewHolder.mImage.setImageDrawable(drawable);
                        mPhotoThumbnailsCache.put(photo.getId(), new SoftReference<>(drawable));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, "accept: throwable:" + throwable);
                    }
                });
        mCompositeDisposable.add(disposable);
        mRequestDisposable.put(photo.getId(), disposable);
    }

    private Observable<Drawable> drawableFromUrl(final String url) {
        return Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(final ObservableEmitter<Drawable> e) {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(new Request.Builder().url(url).build())
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e(TAG, "onFailure: e:" + e);
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        InputStream inputStream = response.body().byteStream();
                                        Drawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeStream(inputStream));
                                        Log.d(TAG, "onResponse: response.toString():" + response.toString());
                                        e.onNext(drawable);
                                    }
                                }
                            }
                        });
            }
        });
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
