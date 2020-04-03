package host.heiko.mcstatus.core;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import host.heiko.mcstatus.model.FavoriteModel;
import host.heiko.mcstatus.model.dao.FavoriteDao;

@Database(entities = {FavoriteModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();

}
