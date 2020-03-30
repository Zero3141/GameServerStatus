package host.heiko.minecraftserverstatus.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import host.heiko.minecraftserverstatus.model.QueryModel;

public class ListViewModel extends ViewModel {

    private MutableLiveData<List<QueryModel>> queryModels;


    public ListViewModel() {
        queryModels = new MutableLiveData<>();
    }


    void setQueryModels(List<QueryModel> queryModels) {
        this.queryModels.setValue(queryModels);
    }

    LiveData<List<QueryModel>> getQueryModels() {
        return queryModels;
    }

}
