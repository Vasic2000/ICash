package cz.vasic2000.icash.mvp.model.cash;

import java.util.List;
import java.util.concurrent.Callable;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

public class PaperCashe implements ICashe {
    @Override
    public Single<User> getUser(String userName) {
        if (!Paper.book("users").contains(userName)) {
            return Single.error(new RuntimeException("no such user in cache"));
        }
        return Single.fromCallable(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return Paper.book("users")
                        .read(userName);
            }
        });
    }

    @Override
    public Completable putUser(String userName, User user) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Paper.book("users")
                        .write(userName, user);
            }
        });
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) {
        if (!Paper.book("repos").contains(user.getLogin())) {
            return Single.error(new RuntimeException("no such user in cache"));
        }
        return Single.fromCallable(() -> Paper.book("repos")
                .read(user.getLogin()));
    }

    @Override
    public Completable putUserRepos(User user, List<Repository> repos) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Paper.book("repos")
                        .write(user.getLogin(), repos);
            }
        });
    }
}
