package cz.vasic2000.icash.di;

import androidx.room.Room;

import javax.inject.Named;
import javax.inject.Singleton;

import cz.vasic2000.icash.App;
import cz.vasic2000.icash.mvp.model.cash.ICashe;
import cz.vasic2000.icash.mvp.model.cash.PaperCashe;
import cz.vasic2000.icash.mvp.model.cash.RealmCashe;
import cz.vasic2000.icash.mvp.model.cash.RoomCashe;

import cz.vasic2000.icash.mvp.model.entity.room.db.DataBase;
import dagger.Module;
import dagger.Provides;

@Module
public class CacheModule {
    @Singleton
    @Provides
    public DataBase roomDatabase(App app){
        return  Room.databaseBuilder(app, DataBase.class, DataBase.DB_NAME).build();
    }

    @Named("room")
    @Singleton
    @Provides
    public ICashe roomCache(DataBase database) {
        return new RoomCashe(database);
    }

    @Named("realm")
    @Singleton
    @Provides
    public ICashe realmCache() {
        return new RealmCashe();
    }

    @Named("paper")
    @Singleton
    @Provides
    public ICashe paperCache() {
        return new PaperCashe();
    }
}
