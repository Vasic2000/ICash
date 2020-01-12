package cz.vasic2000.icash.mvp.model.entity.room.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.room.RoomRepository;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RepositoryDao {

    @Insert(onConflict = REPLACE)
    void insert(RoomRepository user);

    @Insert(onConflict = REPLACE)
    void insert(RoomRepository... user);

    @Insert(onConflict = REPLACE)
    void insert(List<RoomRepository> user);

    @Update
    void update(RoomRepository user);

    @Insert
    void update(RoomRepository... user);

    @Insert
    void update(List<RoomRepository> user);

    @Delete
    void delete(RoomRepository user);

    @Delete
    void delete(RoomRepository... user);

    @Delete
    void delete(List<RoomRepository> user);

    @Query("SELECT * FROM roomrepository")
    List<RoomRepository> getAll();

    @Query("SELECT * FROM roomrepository WHERE userLogin = :login")
    List<RoomRepository> findForUser(String login);
}
