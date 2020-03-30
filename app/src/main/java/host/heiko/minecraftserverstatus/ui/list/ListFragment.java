package host.heiko.minecraftserverstatus.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import host.heiko.minecraftserverstatus.AddActivity;
import host.heiko.minecraftserverstatus.R;
import host.heiko.minecraftserverstatus.core.Provider;
import host.heiko.minecraftserverstatus.core.async.QueryLoader;
import host.heiko.minecraftserverstatus.model.FavoriteModel;
import host.heiko.minecraftserverstatus.model.QueryModel;


public class ListFragment extends Fragment {

    private final static int REQUEST_ADD_ACTIVITY = 100;

    private ListViewModel listViewModel;
    private ListAdapter listAdapter;
    private QueryLoader queryLoader;

    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init the view model
        assert getActivity() != null;
        listViewModel = new ViewModelProvider(getActivity()).get(ListViewModel.class);

        // Enable options menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate view
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        // Assert non empty activity
        assert getActivity() != null;

        // Init empty view
        emptyView = root.findViewById(R.id.emptyView);

        // Init add button
        FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddActivity.class);
            startActivityForResult(intent, REQUEST_ADD_ACTIVITY);
        });

        // Init recycler view
        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Init adapter
        listAdapter = new ListAdapter(getContext());
        listAdapter.setClickListener(queryModel -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("queryModel", queryModel);
            Navigation.findNavController(root).navigate(R.id.action_view_detail, bundle);
        });
        recyclerView.setAdapter(listAdapter);

        // Init swipe refresh
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this::loadList);

        // Observe view model
        listViewModel.getQueryModels().observe(requireActivity(), queryModels -> {
            listAdapter.clearQueryModels();
            listAdapter.addQueryModels(queryModels);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reset title
        assert getActivity() != null;
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setTitle(Objects.requireNonNull(getContext()).getString(R.string.app_name));

        // Load / refresh list
        loadList();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_ADD_ACTIVITY && resultCode == Activity.RESULT_OK) {
            assert data != null;
            String hostname = data.getStringExtra("hostname");
            addServer(hostname);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    /**
     * Refreshes the list.
     */
    private void loadList() {

        // Assert non empty activity
        if(getActivity() == null) {
            return;
        }

        // Set refreshing true
        swipeRefresh.setRefreshing(true);

        // Run in new thread
        new Thread(() -> {

            // Load favorites
            List<FavoriteModel> favoriteModels = Provider.getInstance().getDatabase().favoriteDao().getAll();

            // Stop refreshing if no favorites exist
            if (favoriteModels.size() == 0) {
                getActivity().runOnUiThread(() -> {
                    listAdapter.clearQueryModels();
                    emptyView.setVisibility(View.VISIBLE);
                    swipeRefresh.setRefreshing(false);
                });
                return;
            }

            // Cancel previous query loader, if exists
            if(queryLoader != null) {
                queryLoader.cancel(true);
            }

            // Init loader
            queryLoader = new QueryLoader();

            // Execute query loader
            queryLoader.setListener(queryModels -> getActivity().runOnUiThread(() -> {
                listViewModel.setQueryModels(queryModels);
                emptyView.setVisibility(View.INVISIBLE);
                swipeRefresh.setRefreshing(false);
            }));
            queryLoader.execute(favoriteModels.toArray(new FavoriteModel[]{}));

        }).start();

    }

    /**
     * Adds a new server.
     *
     * @param hostname The hostname of the server.
     */
    private void addServer(String hostname) {

        swipeRefresh.setRefreshing(true);

        QueryLoader queryLoader = new QueryLoader();
        queryLoader.setListener(queryModels -> {
            QueryModel queryModel = queryModels.get(0);

            // Check if server is offline
            if (!queryModel.isOnline()) {
                if(getActivity() != null) {
                    swipeRefresh.setRefreshing(false);
                    assert getActivity() != null;
                    Toast.makeText(getActivity().getApplicationContext(), R.string.add_cannot_offline, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // Add to database
            new Thread(() -> {

                if (Provider.getInstance().getDatabase().favoriteDao().getAll().size() == 0) {
                    Provider.getInstance().getDatabase().favoriteDao().insert(new FavoriteModel(hostname, 0));
                } else {
                    Provider.getInstance().getDatabase().favoriteDao().insert(hostname);
                }

                // Refresh the list
                assert getActivity() != null;
                getActivity().runOnUiThread(this::loadList);

            }).start();

        });
        queryLoader.execute(new FavoriteModel(hostname, -1));

    }

}
