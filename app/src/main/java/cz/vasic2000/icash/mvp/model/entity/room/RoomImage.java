package cz.vasic2000.icash.mvp.model.entity.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomImage {

    @NonNull
    @PrimaryKey
    private String url;
    private String path;

    public RoomImage(@NonNull String url, String path) {
        this.url = url;
        this.path = path;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
