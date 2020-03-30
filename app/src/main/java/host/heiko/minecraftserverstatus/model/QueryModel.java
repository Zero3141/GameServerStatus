package host.heiko.minecraftserverstatus.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class QueryModel implements Serializable {

    private boolean online;
    private String ip;
    private String hostname;

    private Map<String, String[]> motd;
    private int onlinePlayers;
    private int maxPlayers;
    private List<PlayerModel> playerList;

    private String version;
    private String software;
    private Bitmap icon;

    private int position;

    public QueryModel setOnline(boolean online) {
        this.online = online;
        return this;
    }

    public QueryModel setIp(String ip) {
        this.ip = ip;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public QueryModel setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public QueryModel setMotd(Map<String, String[]> motd) {
        this.motd = motd;
        return this;
    }

    public QueryModel setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public QueryModel setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public QueryModel setPlayerList(List<PlayerModel> playerList) {
        this.playerList = playerList;
        return this;
    }

    public QueryModel setVersion(String version) {
        this.version = version;
        return this;
    }

    public QueryModel setSoftware(String software) {
        this.software = software;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public QueryModel setPosition(int position) {
        this.position = position;
        return this;
    }

    public QueryModel setIcon(Bitmap icon) {
        this.icon = icon;
        return this;
    }

    public boolean isOnline() {
        return online;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public Map<String, String[]> getMotd() {
        return motd;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<PlayerModel> getPlayerList() {
        return playerList;
    }

    public String getVersion() {
        return version;
    }

    public String getSoftware() {
        return software;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public int getPosition() {
        return position;
    }

}
