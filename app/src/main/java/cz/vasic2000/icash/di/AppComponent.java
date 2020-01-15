package cz.vasic2000.icash.di;

import cz.vasic2000.icash.di.module.RepoModule;
import cz.vasic2000.icash.mvp.presenter.MainPresenter;
import dagger.Component;

@Component(modules = RepoModule.class)
public interface AppComponent {
    void inject(MainPresenter presenter);
}
