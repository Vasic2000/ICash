package cz.vasic2000.icash.mvp.presenter;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import cz.vasic2000.icash.mvp.model.repo.PaperUsersRepo;
import cz.vasic2000.icash.mvp.model.repo.RealmUsersRepo;
import cz.vasic2000.icash.mvp.model.repo.RoomUsersRepo;
import cz.vasic2000.icash.mvp.model.repo.UsersRepo;
import cz.vasic2000.icash.mvp.presenter.list.IRepositoryListPresenter;
import cz.vasic2000.icash.mvp.view.MainView;
import cz.vasic2000.icash.mvp.view.list.ReposotoryItemView;
import io.reactivex.Scheduler;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    class RepositoryListPresenter implements IRepositoryListPresenter {

        PublishSubject<ReposotoryItemView> clickSubject = PublishSubject.create();
        List<Repository> repositories = new ArrayList<>();

        @Override
        public void bind(ReposotoryItemView view) {
            view.setTitle(repositories.get(view.getPos()).getName());
        }

        @Override
        public int getCount() {
            return repositories.size();
        }

        @Override
        public PublishSubject<ReposotoryItemView> getClickSubject() {
            return clickSubject;
        }
    }
    private RoomUsersRepo usersRepo;
//    private RealmUsersRepo usersRepo;
//    private PaperUsersRepo usersRepo;
    private Scheduler mainThreadScheduler;
    private RepositoryListPresenter repositoryListPresenter;

    public MainPresenter(Scheduler mainThreadScheduler) {
//        this.usersRepo = new PaperUsersRepo();
        this.usersRepo = new RoomUsersRepo();
//        this.usersRepo = new PaperUsersRepo();
        this.mainThreadScheduler = mainThreadScheduler;
        repositoryListPresenter = new RepositoryListPresenter();
    }

    public IRepositoryListPresenter getCountriesListPresenter() {
        return repositoryListPresenter;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
        loadData();

        repositoryListPresenter.getClickSubject().subscribe(countryRowView -> {
            getViewState().showMessage(repositoryListPresenter.repositories.get(countryRowView.getPos()).getName());
        });
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        getViewState().showLoading();
        usersRepo.getUser("vasic2000")
                .observeOn(mainThreadScheduler)
                .flatMap(new Function<User, SingleSource<? extends List<Repository>>>() {
                    @Override
                    public SingleSource<? extends List<Repository>> apply(User user) throws Exception {
                        MainPresenter.this.getViewState().setUsername(user.getLogin());
                        MainPresenter.this.getViewState().loadImage(user.getAvatarUrl());
                        return usersRepo.getUserRepos(user)
                                .observeOn(mainThreadScheduler);
                    }
                })
                .subscribe(new Consumer<List<Repository>>() {
                    @Override
                    public void accept(List<Repository> repositories) throws Exception {
                        repositoryListPresenter.repositories.clear();
                        repositoryListPresenter.repositories.addAll(repositories);
                        MainPresenter.this.getViewState().updateList();
                        MainPresenter.this.getViewState().hideLoading();
                    }
                }, t -> {
                    getViewState().hideLoading();
                    Timber.e(t);
                });
    }
}
