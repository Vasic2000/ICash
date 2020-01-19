package cz.vasic2000.icash.mvp.model.cash;

import java.util.ArrayList;
import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import cz.vasic2000.icash.mvp.model.entity.realm.RealmRepository;
import cz.vasic2000.icash.mvp.model.entity.realm.RealmUser;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class RealmCashe implements ICashe {

    @Override
    public Single<User> getUser(String username) {
        return Single.create(new SingleOnSubscribe<Object>() {
            @Override
            public void subscribe(SingleEmitter<Object> emitter) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
                if (realmUser == null) {
                    emitter.onError(new RuntimeException("No such user in cache"));
                } else {
                    emitter.onSuccess(new User(realmUser.getLogin(), realmUser.getAvatarUrl(), realmUser.getReposUrl()));
                }
                realm.close();
            }
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public Completable putUser(String username, User user) {
        return Completable.fromAction(new Action() {
                                          @Override
                                          public void run() throws Exception {
                                              Realm realm = Realm.getDefaultInstance();
                                              RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
                                              if (realmUser == null) {
                                                  realm.executeTransaction(new Realm.Transaction() {
                                                      @Override
                                                      public void execute(Realm innerRealm) {
                                                          RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, username);
                                                          newRealmUser.setAvatarUrl(user.getAvatarUrl());
                                                          newRealmUser.setReposUrl(user.getReposUrl());
                                                      }
                                                  });
                                              } else {
                                                  realm.executeTransaction(new Realm.Transaction() {
                                                      @Override
                                                      public void execute(Realm innerRealm) {
                                                          realmUser.setAvatarUrl(user.getAvatarUrl());
                                                          realmUser.setReposUrl(user.getReposUrl());
                                                      }
                                                  });
                                              }
                                              realm.close();
                                          }
                                      }
        );
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) {
        return Single.create(new SingleOnSubscribe<Object>() {
            @Override
            public void subscribe(SingleEmitter<Object> emitter) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

                if (realmUser == null) {
                    emitter.onError(new RuntimeException("No such user in cache"));
                } else {
                    List<RealmRepository> realmRepositories = realmUser.getRepos();
                    List<Repository> repos = new ArrayList<>();
                    for (RealmRepository roomRepository : realmRepositories) {
                        repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                    }

                    emitter.onSuccess(repos);
                }
            }
        }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>) (Class) List.class);
    }

    @Override
    public Completable putUserRepos(User user, List<Repository> repositories) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

                if (realmUser == null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm innerRealm) {
                            RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, user.getLogin());
                            newRealmUser.setAvatarUrl(user.getAvatarUrl());
                            newRealmUser.setReposUrl(user.getReposUrl());
                        }
                    });
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm innerRealm) {
                        realmUser.getRepos().deleteAllFromRealm();
                        for (Repository repository : repositories) {
                            RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, repository.getId());
                            realmRepository.setName(repository.getName());
                            realmUser.getRepos().add(realmRepository);
                        }
                    }
                });
                realm.close();
            }
        });
    }
}
