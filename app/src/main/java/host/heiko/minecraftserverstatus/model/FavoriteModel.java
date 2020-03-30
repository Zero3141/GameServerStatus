package host.heiko.minecraftserverstatus.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "favorites")
public class FavoriteModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ip")
    private String ip;

    @ColumnInfo(name = "position")
    private int position;

    public FavoriteModel(@NonNull String ip, int position) {
        this.ip = ip;
        this.position = position;
    }

    @NotNull
    public String getIp() {
        return ip;
    }

    public int getPosition() {
        return position;
    }

}
