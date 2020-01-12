package cz.vasic2000.icash.mvp.model.entity.room.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import cz.vasic2000.icash.mvp.model.entity.room.RoomRepository;
import cz.vasic2000.icash.mvp.model.entity.room.RoomUser;
import cz.vasic2000.icash.mvp.model.entity.room.dao.RepositoryDao;
import cz.vasic2000.icash.mvp.model.entity.room.dao.UserDao;


@androidx.room.Database(entities = {RoomUser.class, RoomRepository.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "userDatabase.db";
    private static volatile Database instance;

    public static synchronized Database getInstance(){
        if(instance == null){
            throw new RuntimeException("Database has not been created. Please call create()");
        }
        return instance;
    }

    public static void create(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, Database.class, DB_NAME).build();
        }
    }

    public abstract UserDao getUserDao();
    public abstract RepositoryDao getRepositoryDao();

}
