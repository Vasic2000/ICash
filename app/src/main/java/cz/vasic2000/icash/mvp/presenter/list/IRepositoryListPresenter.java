package cz.vasic2000.icash.mvp.presenter.list;


import cz.vasic2000.icash.mvp.view.list.ReposotoryItemView;
import io.reactivex.subjects.PublishSubject;

public interface IRepositoryListPresenter {
    void bind(ReposotoryItemView view);
    int getCount();
    PublishSubject<ReposotoryItemView> getClickSubject();
}
