package cz.vasic2000.icash.mvp.model.repo;

import java.util.ArrayList;
import java.util.List;

import cz.vasic2000.icash.mvp.model.api.ApiHolder;
import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import cz.vasic2000.icash.mvp.model.entity.room.RoomRepository;
import cz.vasic2000.icash.mvp.model.entity.room.RoomUser;
import cz.vasic2000.icash.mvp.model.entity.room.db.Database;
import cz.vasic2000.icash.ui.NetworkStatus;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class RoomUsersRepo {

    INetworkStatus networkStatus = new NetworkStatus();

    public Single<User> getUser(String username) {
        if (networkStatus.isOnline()) {
            return ApiHolder.getApi().getUser(username).subscribeOn(Schedulers.io()).map(user -> {

                RoomUser roomUser = Database.getInstance().getUserDao().findByLogin(user.getLogin());
                if (roomUser == null) {
                    roomUser = new RoomUser(user.getLogin());
                }

                roomUser.setAvatarUrl(user.getAvatarUrl());
                roomUser.setReposUrl(user.getReposUrl());
                roomUser.setName(user.getName());

                Database.getInstance().getUserDao().insert(roomUser);

                return user;
            });
        } else {
            return Single.create(emitter -> {
                RoomUser roomUser = Database.getInstance().getUserDao().findByLogin(username);
                if (roomUser == null) {
                    emitter.onError(new RuntimeException("No such user in cache"));
                } else {
                    emitter.onSuccess(new User(roomUser.getLogin(), roomUser.getAvatarUrl(), roomUser.getReposUrl()));
                }
            }).subscribeOn(Schedulers.io()).cast(User.class);
        }
    }

    public Single<List<Repository>> getUserRepos(User user) {
        if (networkStatus.isOnline()) {
            return ApiHolder.getApi().getUserRepos(user.getReposUrl())
                    .map(repos -> {
                        RoomUser roomUser = Database.getInstance().getUserDao().findByLogin(user.getLogin());
                        if (roomUser == null) {
                            roomUser = new RoomUser(user.getLogin(), user.getAvatarUrl(), user.getReposUrl());
                            Database.getInstance().getUserDao().insert(roomUser);
                        }

                        if (!repos.isEmpty()) {
                            List<RoomRepository> roomRepositories = new ArrayList<>();
                            for (Repository repository : repos) {
                                RoomRepository roomRepository = new RoomRepository(repository.getId(), repository.getName(), user.getLogin());
                                roomRepositories.add(roomRepository);
                            }

                            Database.getInstance()
                                    .getRepositoryDao()
                                    .insert(roomRepositories);
                        }

                        return repos;
                    })
                    .subscribeOn(Schedulers.io());
        } else {
            return Single.create(emitter -> {
                RoomUser roomUser = Database.getInstance().getUserDao().findByLogin(user.getLogin());
                if (roomUser == null) {
                    emitter.onError(new RuntimeException("No such user in cache"));
                } else {
                    List<RoomRepository> roomRepositories = Database.getInstance().getRepositoryDao().findForUser(user.getLogin());
                    List<Repository> repos = new ArrayList<>();
                    for (RoomRepository roomRepository : roomRepositories) {
                        repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                    }
                    emitter.onSuccess(repos);
                }
            }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>) (Class) List.class);
        }
    }
}
