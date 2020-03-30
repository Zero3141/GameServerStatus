package host.heiko.minecraftserverstatus.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

import host.heiko.minecraftserverstatus.R;
import host.heiko.minecraftserverstatus.core.Provider;
import host.heiko.minecraftserverstatus.model.QueryModel;


public class DetailFragment extends Fragment {

    private QueryModel queryModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        if (getArguments() != null) {
            queryModel = (QueryModel) getArguments().getSerializable("queryModel");
        }

        // Enable options menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate view
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        // Update title
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(queryModel.getHostname());

        // Set icon
        ImageView icon = root.findViewById(R.id.icon);
        icon.setImageBitmap(queryModel.getIcon());

        // Set motd
        if (queryModel.isOnline()) {
            TextView motd = root.findViewById(R.id.motd);
            StringBuilder builder = new StringBuilder();
            for (String s : Objects.requireNonNull(queryModel.getMotd().get("clean"))) {
                builder.append(s);
            }
            motd.setText(builder.toString());
        }


        // Set players
        final RecyclerView recyclerView = root.findViewById(R.id.playersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PlayerAdapter playerAdapter = new PlayerAdapter(getContext());

        TextView emptyView = root.findViewById(R.id.emptyView);
        if (queryModel.getPlayerList() == null) {
            emptyView.setVisibility(View.VISIBLE);
        }else{
            playerAdapter.setPlayerModels(queryModel.getPlayerList());
        }
        recyclerView.setAdapter(playerAdapter);


        // Set version
        TextView version = root.findViewById(R.id.version);
        version.setText(String.format(getString(R.string.detail_version_data), queryModel.getVersion(), queryModel.getSoftware()));

        // Init swipe refresh
        SwipeRefreshLayout swipeRefresh = root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> new Thread(() -> Provider.getInstance().requestServerQuery(queryModel.getIp(), queryModel.getPosition(), queryModel -> getActivity().runOnUiThread(() -> {

            this.queryModel = queryModel;

            // Set icon
            icon.setImageBitmap(queryModel.getIcon());

            // Set motd
            if (queryModel.isOnline()) {
                TextView motd = root.findViewById(R.id.motd);
                StringBuilder builder = new StringBuilder();
                for (String s : Objects.requireNonNull(queryModel.getMotd().get("clean"))) {
                    builder.append(s);
                }
                motd.setText(builder.toString());
            }

            // Set players
            if (queryModel.getPlayerList() != null) {
                playerAdapter.setPlayerModels(queryModel.getPlayerList());
                playerAdapter.notifyDataSetChanged();
            }

            // Set version
            version.setText(String.format(getString(R.string.detail_version_data), queryModel.getVersion(), queryModel.getSoftware()));

            swipeRefresh.setRefreshing(false);

        }))).start());

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setMessage(String.format(getString(R.string.detail_alert_really), queryModel.getHostname()))
                    .setPositiveButton("Yes", (dialogInterface, i) -> removeServer(queryModel.getPosition()))
                    .setNegativeButton("No", (dialogInterface, i) -> { }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Removes a server.
     *
     * @param position The position of the server.
     */
    private void removeServer(int position) {

        new Thread(() -> {

            // Delete from database
            Provider.getInstance().getDatabase().favoriteDao().delete(position);

            // Exit fragment
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> getActivity().onBackPressed());
            }

        }).start();

    }

}
