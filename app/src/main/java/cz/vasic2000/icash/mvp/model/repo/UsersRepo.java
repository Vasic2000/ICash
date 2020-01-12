package cz.vasic2000.icash.mvp.model.repo;

import cz.vasic2000.icash.mvp.model.api.ApiHolder;
import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


import java.util.List;

public class UsersRepo {

    public Single<User> getUser(String username) {
        return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io());
    }

    public Single<List<Repository>> getUserRepos(String url) {
        return ApiHolder.getApi().getUserRepos(url).subscribeOn(Schedulers.io());
    }
}
