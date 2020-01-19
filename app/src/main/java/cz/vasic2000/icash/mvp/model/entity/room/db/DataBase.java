package cz.vasic2000.icash.mvp.model.entity.room.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cz.vasic2000.icash.mvp.model.entity.room.RoomImage;
import cz.vasic2000.icash.mvp.model.entity.room.RoomRepository;
import cz.vasic2000.icash.mvp.model.entity.room.RoomUser;
import cz.vasic2000.icash.mvp.model.entity.room.dao.ImageDAO;
import cz.vasic2000.icash.mvp.model.entity.room.dao.RepositoryDao;
import cz.vasic2000.icash.mvp.model.entity.room.dao.UserDao;

@Database(entities = {RoomUser.class, RoomRepository.class, RoomImage.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    public static final String DB_NAME = "userDatabase.db";
    public abstract UserDao getUserDao();
    public abstract RepositoryDao getRepositoryDao();
    public abstract ImageDAO getImageDao();
}
