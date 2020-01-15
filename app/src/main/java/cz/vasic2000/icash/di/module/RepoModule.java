package cz.vasic2000.icash.di.module;

import cz.vasic2000.icash.mvp.model.cash.RoomCashe;
import cz.vasic2000.icash.mvp.model.repo.UsersRepo;
import cz.vasic2000.icash.ui.NetworkStatus;
import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {
    @Provides
    public UsersRepo usersRepo() {
        return new UsersRepo(new NetworkStatus(), new RoomCashe());
    }
}
