package cz.vasic2000.icash.mvp.model.entity.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomUser {

    @NonNull
    @PrimaryKey
    private String login;
    private String avatarUrl;
    private String reposUrl;

    public RoomUser() {
    }

    public RoomUser(@NonNull String login, String avatarUrl, String reposUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.reposUrl = reposUrl;
    }

  public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }
}
