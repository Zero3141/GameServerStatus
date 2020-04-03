package host.heiko.mcstatus.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import host.heiko.mcstatus.model.FavoriteModel;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY position ASC")
    List<FavoriteModel> getAll();

    @Query("INSERT INTO favorites VALUES (:ip, (SELECT MAX(position) FROM favorites) + 1 )")
    void insert(String ip);

    @Insert
    void insert(FavoriteModel favoriteModel);

    @Query("DELETE FROM favorites WHERE position = :position")
    void delete(int position);

}
