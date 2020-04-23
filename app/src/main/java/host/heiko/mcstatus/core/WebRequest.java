package host.heiko.mcstatus.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

class WebRequest {

    private static final String TAG = WebRequest.class.getSimpleName();


    /**
     * The volley request queue.
     */
    private RequestQueue requestQueue;


    /**
     * An image loader on top of the request queue.
     */
    private ImageLoader imageLoader;


    /**
     * Constructor.
     *
     * @param context The context.
     */
    WebRequest(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(10);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * Performs a json get request.
     *
     * @param url The target url.
     * @return A future of a json object.
     */
    JSONObject performJson(String url) {

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        requestQueue.add(request);

        try {
            return future.get();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

    }


    /**
     * Returns the image loader.
     *
     * @return ImageLoader.
     */
    ImageLoader getImageLoader() {
        return imageLoader;
    }

}

