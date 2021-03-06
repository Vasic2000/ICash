package cz.vasic2000.icash.mvp.model.repo;

import java.util.List;

import cz.vasic2000.icash.mvp.model.api.IDataSource;
import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.mvp.model.cash.ICashe;
import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UsersRepo implements IUsersRepo{

    private INetworkStatus networkStatus;
    private IDataSource api;
    private ICashe iCashe;

    public UsersRepo(INetworkStatus networkStatus, IDataSource api, ICashe iCashe) {
        this.networkStatus = networkStatus;
        this.iCashe = iCashe;
        this.api = api;
    }

    public Single<User> getUser(String userName) {
        if (networkStatus.isOnline()) {
            return api.getUser(userName)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<User, User>() {
                        @Override
                        public User apply(User user) throws Exception {
                            iCashe.putUser(userName, user)
                                    .subscribe();
                            return user;
                        }
                    }

            );
        } else {
            return iCashe.getUser(userName);
        }
    }

    public Single<List<Repository>> getUserRepos(User user)  {
        if (networkStatus.isOnline()) {
            return api.getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(new Function<List<Repository>, List<Repository>>() {
                             @Override
                             public List<Repository> apply(List<Repository> repos) throws Exception {
                                 iCashe.putUserRepos(user, repos)
                                         .subscribe();
                                 return repos;
                             }
                         }

                    );
        } else {
            return iCashe.getUserRepos(user);
        }
    }
}
