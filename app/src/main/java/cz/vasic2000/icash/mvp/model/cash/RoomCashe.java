package cz.vasic2000.icash.mvp.model.cash;

import java.util.ArrayList;
import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import cz.vasic2000.icash.mvp.model.entity.room.RoomRepository;
import cz.vasic2000.icash.mvp.model.entity.room.RoomUser;
import cz.vasic2000.icash.mvp.model.entity.room.db.DataBase;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RoomCashe implements ICashe {

    DataBase database;

    public RoomCashe(DataBase database) {
        this.database = database;
    }

    @Override
    public Single<User> getUser(String username) {

        return Single.create(emitter -> {
            RoomUser roomUser = database.getUserDao()
                    .findByLogin(username);

            if (roomUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onSuccess(new User(roomUser.getLogin(), roomUser.getAvatarUrl(), roomUser.getReposUrl()));
            }
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public Completable putUser(String username, User user) {
        return Completable.fromAction(() -> {
            RoomUser roomUser = database.getUserDao()
                    .findByLogin(username);

            if (roomUser == null) {
                roomUser = new RoomUser();
                roomUser.setLogin(username);
            }

            roomUser.setAvatarUrl(user.getAvatarUrl());
            roomUser.setReposUrl(user.getReposUrl());

            database.getUserDao()
                    .insert(roomUser);
        });
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) {

        return Single.create(emitter -> {
            RoomUser roomUser = database.getUserDao()
                    .findByLogin(user.getLogin());
            if (roomUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                List<RoomRepository> roomRepositories = database.getRepositoryDao()
                        .getAll();

                List<Repository> repos = new ArrayList<>();
                for (RoomRepository roomRepository : roomRepositories) {
                    repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                }

                emitter.onSuccess(repos);
            }
        }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>) (Class) List.class);
    }

    @Override
    public Completable putUserRepos(User user, List<Repository> repositories) {

        return Completable.fromAction(() -> {
            RoomUser roomUser = database.getUserDao()
                    .findByLogin(user.getLogin());

            if (roomUser == null) {
                roomUser = new RoomUser();
                roomUser.setLogin(user.getLogin());
                roomUser.setAvatarUrl(user.getAvatarUrl());
                roomUser.setReposUrl(user.getReposUrl());
                database.getUserDao().insert(roomUser);
            }


            if (!repositories.isEmpty()) {
                List<RoomRepository> roomRepositories = new ArrayList<>();
                for (Repository repository : repositories) {
                    RoomRepository roomRepository = new RoomRepository(repository.getId(), repository.getName(), user.getLogin());
                    roomRepositories.add(roomRepository);
                }

                database.getRepositoryDao().insert(roomRepositories);
            }
        });
    }
}
