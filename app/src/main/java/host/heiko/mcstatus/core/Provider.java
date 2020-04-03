package host.heiko.mcstatus.core;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONObject;

import host.heiko.mcstatus.R;
import host.heiko.mcstatus.core.util.ParserUtil;
import host.heiko.mcstatus.model.QueryModel;

public class Provider {

    private Context context;
    private WebRequest webRequest;
    private AppDatabase database;

    private ProviderErrorCallback errorCallback;

    @SuppressLint("StaticFieldLeak")
    private static Provider instance;


    /**
     * Constructor.
     *
     * @param context A context.
     */
    private Provider(Context context) {
        this.context = context;
        this.webRequest = new WebRequest(context);
        this.database = Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.data_db_name)).build();
    }

    /**
     * Creates a new instance.
     *
     * @param context A context.
     */
    public static Provider startInstance(Context context) {
        instance = new Provider(context);
        return instance;
    }

    /**
     * Returns the running instance.
     *
     * @return The new provider instance.
     */
    public static Provider getInstance() {
        return instance;
    }

    /**
     * Requests the server query.
     *
     * @param ip The server ip.
     * @return QueryModel.
     */
    public void requestServerQuery(String ip, int position, ProviderCallback callback) {

        // Parse url
        String url = String.format(context.getString(R.string.data_url_query), ip);

        // Perform request
        JSONObject jsonObject = webRequest.performJson(url);

        // Check for error
        if (jsonObject == null) {
            return;
        }

        // Parse the request
        QueryModel queryModel = ParserUtil.parseQuery(context, jsonObject);

        // Check for error
        if (queryModel == null) {
            if (errorCallback != null) {
                errorCallback.onError(context.getString(R.string.provider_request_fail));
            }
            return;
        }

        queryModel.setPosition(position);

        // Fire received event
        callback.onDataReceived(queryModel);

    }


    /**
     * Returns the instance of the database.
     *
     * @return AppDatabase.
     */
    public AppDatabase getDatabase() {
        return database;
    }

    /**
     * Returns the image loader from web request instance.
     *
     * @return ImageLoader.
     */
    public ImageLoader getImageLoader() {
        return webRequest.getImageLoader();
    }


    /**
     * Sets the error listener.
     *
     * @param errorCallback Fired when error occurs.
     */
    public void setErrorCallback(ProviderErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    public interface ProviderCallback {
        void onDataReceived(QueryModel queryModel);
    }

    public interface ProviderErrorCallback {
        void onError(String message);
    }

}
