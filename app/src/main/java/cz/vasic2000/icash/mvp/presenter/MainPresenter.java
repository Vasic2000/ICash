package cz.vasic2000.icash.mvp.presenter;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import cz.vasic2000.icash.mvp.model.repo.IUsersRepo;

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

    @Inject
    IUsersRepo usersRepo;

    private Scheduler mainThreadScheduler;
    private RepositoryListPresenter repositoryListPresenter;

    public MainPresenter(Scheduler mainThreadScheduler) {
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

        repositoryListPresenter.getClickSubject().subscribe(new Consumer<ReposotoryItemView>() {
            @Override
            public void accept(ReposotoryItemView countryRowView) throws Exception {
                MainPresenter.this.getViewState().showMessage(repositoryListPresenter.repositories.get(countryRowView.getPos()).getName());
            }
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
                        getViewState().setUsername(user.getLogin());
                        getViewState().loadImage(user.getAvatarUrl());
                        return usersRepo.getUserRepos(user)
                                .observeOn(mainThreadScheduler);
                    }
                })
                .subscribe(new Consumer<List<Repository>>() {
                    @Override
                    public void accept(List<Repository> repositories) throws Exception {
                        repositoryListPresenter.repositories.clear();
                        repositoryListPresenter.repositories.addAll(repositories);
                        getViewState().updateList();
                        getViewState().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        MainPresenter.this.getViewState().hideLoading();
                        Timber.e(t);
                    }
                });
    }
}
