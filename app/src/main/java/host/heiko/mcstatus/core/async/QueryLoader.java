package host.heiko.mcstatus.core.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import host.heiko.mcstatus.core.Provider;
import host.heiko.mcstatus.model.FavoriteModel;
import host.heiko.mcstatus.model.QueryModel;

public class QueryLoader extends AsyncTask<FavoriteModel, QueryModel, List<QueryModel>> {

    private static final String TAG = QueryLoader.class.getSimpleName();

    private QueryLoaderListener listener;

    @Override
    protected List<QueryModel> doInBackground(FavoriteModel... list) {

        Log.d(TAG, String.format("Start progressing %d favorites.", list.length));

        List<QueryModel> queryModels = new ArrayList<>();

        // Loop through favorites
        for(FavoriteModel favoriteModel : list) {

            // Check if cancelled
            if(isCancelled()) {
                return queryModels;
            }

            // Request query from server
            Provider.getInstance().requestServerQuery(favoriteModel.getIp(), favoriteModel.getPosition(), queryModel ->  {
                queryModels.add(queryModel);
                publishProgress(queryModel);
            });
        }

        return queryModels;
    }

    @Override
    protected void onProgressUpdate(QueryModel... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, String.format("Loaded query: %s", values[0].toString()));
    }

    @Override
    protected void onPostExecute(List<QueryModel> queryModels) {
        super.onPostExecute(queryModels);
        Log.d(TAG, String.format("Finished: Loaded %d queries.", queryModels.size()));

        if(listener != null) {
            listener.onQueryModelsReceived(queryModels);
        }

    }

    /**
     * Sets the finished listener.
     *
     * @param listener QueryLoaderListener.
     */
    public void setListener(QueryLoaderListener listener) {
        this.listener = listener;
    }

}
