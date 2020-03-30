package host.heiko.minecraftserverstatus.core.async;

import java.util.List;

import host.heiko.minecraftserverstatus.model.QueryModel;

public interface QueryLoaderListener {

    void onQueryModelsReceived(List<QueryModel> queryModels);

}
