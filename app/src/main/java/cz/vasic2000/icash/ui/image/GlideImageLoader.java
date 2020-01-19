package cz.vasic2000.icash.ui.image;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.mvp.model.cash.image.IImageCache;
import cz.vasic2000.icash.mvp.model.image.IImageLoader;

public class GlideImageLoader implements IImageLoader<ImageView> {

    IImageCache imageCache;
    private INetworkStatus networkStatus;

    public GlideImageLoader(IImageCache imageCache, INetworkStatus networkStatus) {
        this.imageCache = imageCache;
        this.networkStatus = networkStatus;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInto(String url, ImageView container) {
        Glide.with(container.getContext())
                .asBitmap()
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(container);
    }
}
