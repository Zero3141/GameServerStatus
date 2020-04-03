package host.heiko.mcstatus.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import host.heiko.mcstatus.R;
import host.heiko.mcstatus.core.Provider;
import host.heiko.mcstatus.model.PlayerModel;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerItemViewHolder> {

    private Context context;
    private List<PlayerModel> playerModels;


    /**
     * Constructor.
     *
     * @param context A context.
     */
    PlayerAdapter(Context context) {
        this.context = context;
        this.playerModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlayerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_player, parent, false);
        return new PlayerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerItemViewHolder holder, int position) {

        PlayerModel playerModel = playerModels.get(position);

        String url = String.format(context.getString(R.string.data_url_player), playerModel.getName());

        holder.icon.setImageUrl(url, Provider.getInstance().getImageLoader());
        holder.name.setText(playerModel.getName());

    }

    @Override
    public int getItemCount() {
        return playerModels.size();
    }

    /**
     * Sets the player models.
     *
     * @param playerModels List of PlayerModel.
     */
    void setPlayerModels(List<PlayerModel> playerModels) {
        this.playerModels = playerModels;
    }

    static class PlayerItemViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView icon;
        TextView name;

        PlayerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
        }

    }

}
