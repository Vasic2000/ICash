package cz.vasic2000.icash.mvp.model.entity;

import com.google.gson.annotations.Expose;


public class User {

    @Expose
    private String login;
    @Expose
    private String avatarUrl;
    @Expose
    private String reposUrl;

    public User(String login, String avatarUrl, String reposUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.reposUrl = reposUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }
}
