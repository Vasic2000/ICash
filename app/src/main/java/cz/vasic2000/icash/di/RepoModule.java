package cz.vasic2000.icash.di;

import javax.inject.Named;

import cz.vasic2000.icash.mvp.model.api.IDataSource;
import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.mvp.model.cash.ICashe;
import cz.vasic2000.icash.mvp.model.repo.IUsersRepo;
import cz.vasic2000.icash.mvp.model.repo.UsersRepo;
import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {

    @Provides
    public IUsersRepo usersRepo(@Named("room") ICashe cache, INetworkStatus networkStatus, IDataSource api) {
        return new UsersRepo(networkStatus, api, cache);
    }
}
