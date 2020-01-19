package cz.vasic2000.icash.mvp.model.entity.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.room.RoomImage;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface ImageDAO {
    @Insert(onConflict = REPLACE)
    void insert(RoomImage image);

    @Insert(onConflict = REPLACE)
    void insert(RoomImage... images);

    @Insert(onConflict = REPLACE)
    void insert(List<RoomImage> images);

    @Update
    void update(RoomImage image);

    @Update
    void update(RoomImage... images);

    @Update
    void update(List<RoomImage> images);

    @Delete
    void delete(RoomImage image);

    @Delete
    void delete(RoomImage... images);

    @Delete
    void delete(List<RoomImage> images);

    @Query("DELETE FROM roomimage")
    void clear();

    @Query("SELECT * FROM roomimage")
    List<RoomImage> getAll();

    @Query("SELECT * FROM roomimage WHERE url = :url LIMIT 1")
    RoomImage findByUrl(String url);
}
