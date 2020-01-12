package cz.vasic2000.icash.ui.image;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cz.vasic2000.icash.mvp.model.image.IImageLoader;


public class PicasoImageLoader implements IImageLoader<ImageView> {

    @Override
    public void loadInto(String url, ImageView container) {
        Picasso.get().load(url).into(container);
    }
}
