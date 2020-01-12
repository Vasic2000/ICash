package cz.vasic2000.icash.mvp.model.repo;

import java.util.List;
import java.util.concurrent.Callable;

import cz.vasic2000.icash.mvp.model.api.ApiHolder;
import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import cz.vasic2000.icash.ui.NetworkStatus;
import io.paperdb.Paper;
import io.reactivex.Single;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PaperUsersRepo {

    INetworkStatus networkStatus = new NetworkStatus();

    public Single<User> getUser(String username) {
        if (networkStatus.isOnline()) {
            return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io())
                    .map(new Function<User, User>() {
                @Override
                public User apply(User user) throws Exception {
                    Paper.book("users").write(username, user);
                    return user;
                }
            });
        } else {
            if (!Paper.book("users").contains(username)) {
                return Single.error(new RuntimeException("no such user in cache"));
            }

            return Single.fromCallable(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return Paper.book("users")
                            .read(username);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .cast(User.class);
        }
    }

    public Single<List<Repository>> getUserRepos(User user) {
        if (networkStatus.isOnline()) {
            return ApiHolder.getApi().getUserRepos(user.getReposUrl())
                    .map(new Function<List<Repository>, List<Repository>>() {
                        @Override
                        public List<Repository> apply(List<Repository> repos) throws Exception {
                            Paper.book("repos").write(user.getLogin(), repos);
                            return repos;
                        }
                    })
                    .subscribeOn(Schedulers.io());
        } else {
            if (!Paper.book("repos").contains(user.getLogin())) {
                return Single.error(new RuntimeException("no repos for such user in cache"));
            }
            return Single.fromCallable(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return Paper.book("repos")
                            .read(user.getLogin());
                }
            }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>) (Class) List.class);
        }
    }
}
