package cz.vasic2000.icash.mvp.model.image;


public interface IImageLoader<T> {
    void loadInto(String url, T container);
}
