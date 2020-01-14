package cz.vasic2000.icash.mvp.model.cash;

import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface ICashe {
    Single<User> getUser(String userName);
    Completable putUser(String userName, User user);
    Single<List<Repository>> getUserRepos(User user);
    Completable putUserRepos(User user, List<Repository> repos);
}
