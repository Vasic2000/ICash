package cz.vasic2000.icash.mvp.model.entity.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.room.RoomUser;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void insert(RoomUser user);

    @Insert(onConflict = REPLACE)
    void insert(RoomUser... user);

    @Insert(onConflict = REPLACE)
    void insert(List<RoomUser> user);

    @Update
    void update(RoomUser user);

    @Insert
    void update(RoomUser... user);

    @Insert
    void update(List<RoomUser> user);

    @Delete
    void delete(RoomUser user);

    @Delete
    void delete(RoomUser... user);

    @Delete
    void delete(List<RoomUser> user);

    @Query("SELECT * FROM roomuser")
    List<RoomUser> getAll();

    @Query("SELECT * FROM roomuser WHERE login = :login LIMIT 1")
    RoomUser findByLogin(String login);
}
