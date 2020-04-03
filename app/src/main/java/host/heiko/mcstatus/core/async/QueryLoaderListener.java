package host.heiko.mcstatus.core.async;

import java.util.List;

import host.heiko.mcstatus.model.QueryModel;

public interface QueryLoaderListener {

    void onQueryModelsReceived(List<QueryModel> queryModels);

}
