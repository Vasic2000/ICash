package cz.vasic2000.icash.mvp.model.entity.realm;

import io.realm.RealmObject;

public class RealmImage extends RealmObject {
    String url;
    String path;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}