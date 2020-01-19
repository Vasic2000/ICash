package cz.vasic2000.icash.di;

import javax.inject.Singleton;

import cz.vasic2000.icash.mvp.presenter.MainPresenter;
import cz.vasic2000.icash.ui.activity.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {
        RepoModule.class,
        AppModule.class,
        ImageModule.class
})

public interface AppComponent {
    void inject(MainPresenter presenter);
    void inject(MainActivity mainActivity);
}
