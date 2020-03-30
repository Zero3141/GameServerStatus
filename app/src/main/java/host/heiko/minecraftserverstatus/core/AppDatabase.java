package host.heiko.minecraftserverstatus.core;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import host.heiko.minecraftserverstatus.model.FavoriteModel;
import host.heiko.minecraftserverstatus.model.dao.FavoriteDao;

@Database(entities = {FavoriteModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();

}
