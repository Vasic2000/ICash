package cz.vasic2000.icash.mvp.model.cash.image;

import android.graphics.Bitmap;

import java.io.File;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IImageCache {
    Maybe<File> getFile(String url);
    Maybe<File> saveImage(final String url, Bitmap bitmap);
    Single<Boolean> contains(String url);
    void clear();
    File getImageDir();
    String SHA1(String s);
    float getSizeKb();
    void deleteFileOrDirRecursive(File fileOrDirectory);
    long getFileOrDirSize(File f);
}
