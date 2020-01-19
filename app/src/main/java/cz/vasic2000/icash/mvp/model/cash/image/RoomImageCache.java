package cz.vasic2000.icash.mvp.model.cash.image;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.vasic2000.icash.App;
import cz.vasic2000.icash.mvp.model.entity.room.RoomImage;
import cz.vasic2000.icash.mvp.model.entity.room.db.DataBase;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RoomImageCache implements IImageCache {
    App app;
    private DataBase database;

    public RoomImageCache(App app, DataBase database) {
        this.app = app;
        this.database = database;
    }

    private final String IMAGE_FOLDER_NAME = "image";

    public Maybe<File> getFile(String url) {
        return Maybe.fromCallable(() -> {
            RoomImage cachedImage = database.getImageDao().findByUrl(url);
            if (cachedImage != null) {
                return new File(cachedImage.getPath());
            }
            return null;
        }).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> contains(String url) {
        return Single.fromCallable(() -> database.getImageDao().findByUrl(url) != null).subscribeOn(Schedulers.io());
    }

    public void clear() {
        database.getImageDao().clear();
        deleteFileOrDirRecursive(getImageDir());
    }

    public Maybe<File> saveImage(final String url, Bitmap bitmap) {
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + getImageDir().toString());
        }

        final String fileFormat = url.contains(".jpg") ? ".jpg" : ".png";
        final File imageFile = new File(getImageDir(), SHA1(url) + fileFormat);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(fileFormat.equals("jpg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Timber.d("Failed to save image");
            return null;
        }

        return Maybe.create(emitter -> {
            RoomImage image = new RoomImage(url, imageFile.toString());
            database.getImageDao().insert(image);
            emitter.onSuccess(imageFile);
        }).subscribeOn(Schedulers.io()).cast(File.class);
    }

    public File getImageDir() {
        return new File(app.getExternalFilesDir(null) + "/" + IMAGE_FOLDER_NAME);
    }

    public String SHA1(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public float getSizeKb() {
        return getFileOrDirSize(getImageDir()) / 1024f;
    }

    public void deleteFileOrDirRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteFileOrDirRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public long getFileOrDirSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFileOrDirSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }
}
