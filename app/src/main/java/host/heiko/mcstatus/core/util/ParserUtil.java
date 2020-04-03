package host.heiko.mcstatus.core.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import host.heiko.mcstatus.R;
import host.heiko.mcstatus.model.PlayerModel;
import host.heiko.mcstatus.model.QueryModel;

public class ParserUtil {


    private static final String TAG = ParserUtil.class.getSimpleName();


    /**
     * Parses the query json object into a query model.
     *
     * @param context A context.
     * @param jsonObject The json object.
     * @return QueryModel.
     */
    public static QueryModel parseQuery(Context context, JSONObject jsonObject) {

        QueryModel queryModel = new QueryModel();

        try {

            // Fetch standard attributes
            queryModel.setOnline(jsonObject.getBoolean("online"))
                    .setIp(jsonObject.optString("ip"))
                    .setHostname(jsonObject.has("hostname") ? jsonObject.getString("hostname") : "");

            // Return if not online
            if (!queryModel.isOnline()) {
                return queryModel;
            }

            // Fetch motd
            HashMap<String, String[]> motd = new HashMap<>();
            motd.put("raw", JsonUtil.parseArray(jsonObject.getJSONObject("motd").getJSONArray("raw")));
            motd.put("clean", JsonUtil.parseArray(jsonObject.getJSONObject("motd").getJSONArray("clean")));
            motd.put("html", JsonUtil.parseArray(jsonObject.getJSONObject("motd").getJSONArray("html")));
            queryModel.setMotd(motd);

            // Fetch players
            queryModel.setOnlinePlayers(jsonObject.getJSONObject("players").has("online") ? jsonObject.getJSONObject("players").getInt("online") : 0)
                    .setMaxPlayers(jsonObject.getJSONObject("players").has("max") ? jsonObject.getJSONObject("players").getInt("max") : 0);

            if (jsonObject.getJSONObject("players").has("list")) {
                List<PlayerModel> playerModels = new ArrayList<>();
                String[] players = JsonUtil.parseArray(jsonObject.getJSONObject("players").getJSONArray("list"));
                for (String player : players) {
                    playerModels.add(new PlayerModel().setName(player));
                }
                queryModel.setPlayerList(playerModels);
            }


            // Fetch misc data
            queryModel.setVersion(jsonObject.has("version") ? jsonObject.getString("version") : "")
                    .setSoftware(jsonObject.has("software") ? jsonObject.getString("software") : "")
                    .setIcon(BitmapUtil.parseBase64(jsonObject.has("icon") ? jsonObject.getString("icon") : context.getString(R.string.data_icon_standard)));

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }


        return queryModel;
    }


}
