package cz.vasic2000.icash.di;

import android.widget.ImageView;

import javax.inject.Named;
import cz.vasic2000.icash.App;
import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.mvp.model.cash.image.IImageCache;
import cz.vasic2000.icash.mvp.model.cash.image.ReralmImageCache;
import cz.vasic2000.icash.mvp.model.cash.image.RoomImageCache;
import cz.vasic2000.icash.mvp.model.entity.room.db.DataBase;
import cz.vasic2000.icash.mvp.model.image.IImageLoader;
import cz.vasic2000.icash.ui.image.GlideImageLoader;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModule.class, CacheModule.class})
public class ImageModule {

    @Named("realm")
    @Provides
    public IImageCache realmImageCache() {
        return new ReralmImageCache();
    }

    @Named("room")
    @Provides
    public IImageCache roomImageCache(App app, DataBase database) {
        return new RoomImageCache(app, database);
    }

    @Provides
    public IImageLoader<ImageView> imageLoader(@Named("realm") IImageCache cache, INetworkStatus networkStatus) {
        return new GlideImageLoader(cache, networkStatus);
    }
}
