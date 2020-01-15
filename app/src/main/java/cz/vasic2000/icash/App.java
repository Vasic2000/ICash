package cz.vasic2000.icash;

import android.app.Application;

import cz.vasic2000.icash.di.AppComponent;
import cz.vasic2000.icash.di.DaggerAppComponent;
import cz.vasic2000.icash.mvp.model.entity.room.db.Database;
import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class App extends Application {

    private static App instance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        Paper.init(this);
        Database.create(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        appComponent = DaggerAppComponent.builder()
                .build();
    }

    public static App getInstance(){
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
