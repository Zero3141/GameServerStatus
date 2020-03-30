package host.heiko.minecraftserverstatus.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.List;
import java.util.Objects;

import host.heiko.minecraftserverstatus.R;
import host.heiko.minecraftserverstatus.model.QueryModel;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder> {

    private Context context;
    private SortedList<QueryModel> queryModels;
    private ClickListener clickListener;

    /**
     * Constructor.
     *
     * @param context A context.
     */
    ListAdapter(Context context) {
        this.context = context;
        this.queryModels = new SortedList<>(QueryModel.class, new SortedList.Callback<QueryModel>() {
            @Override
            public int compare(QueryModel o1, QueryModel o2) {
                return Integer.compare(o1.getPosition(), o2.getPosition());
            }
            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }
            @Override
            public boolean areContentsTheSame(QueryModel oldItem, QueryModel newItem) {
                return oldItem.getHostname().equals(newItem.getHostname());
            }
            @Override
            public boolean areItemsTheSame(QueryModel item1, QueryModel item2) {
                return item1.getHostname().equals(item2.getHostname());
            }
            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }
            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }
            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });

    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        QueryModel queryModel = queryModels.get(position);

        holder.icon.setImageBitmap(queryModel.getIcon());
        holder.ip.setText(queryModel.getHostname().isEmpty() ? queryModel.getIp() : queryModel.getHostname());

        if(queryModel.isOnline()) {

            holder.playerCount.setText(String.format(context.getString(R.string.list_players), queryModel.getOnlinePlayers(), queryModel.getMaxPlayers()));

            StringBuilder builder = new StringBuilder();
            for(String s : Objects.requireNonNull(queryModel.getMotd().get("clean"))) {
                builder.append(s);
            }
            holder.motd.setText(builder.toString());

        }else{
            holder.playerCount.setText(R.string.list_offline);
        }

        holder.item.setOnClickListener(view -> {
            if(clickListener != null) {
                clickListener.onClicked(queryModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return queryModels.size();
    }

    /**
     * Add query models to adapter.
     *
     * @param queryModels List of QueryModel.
     */
    void addQueryModels(List<QueryModel> queryModels) {
        this.queryModels.beginBatchedUpdates();
        for(int i = 0; i < queryModels.size(); i++) {
            this.queryModels.add(queryModels.get(i));
        }
        this.queryModels.endBatchedUpdates();
    }

    /**
     * Clear all query models from adapter.
     */
    void clearQueryModels() {
        this.queryModels.beginBatchedUpdates();
        while (this.queryModels.size() > 0) {
            this.queryModels.removeItemAt(this.queryModels.size() - 1);
        }
        this.queryModels.endBatchedUpdates();
    }

    /**
     * Sets the click listener for the views.
     *
     * @param clickListener ClickListener.
     */
    void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClicked(QueryModel queryModel);
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder {

        View item;
        ImageView icon;
        TextView ip;
        TextView motd;
        TextView playerCount;

        ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView;
            icon = itemView.findViewById(R.id.icon);
            ip = itemView.findViewById(R.id.ip);
            motd = itemView.findViewById(R.id.motd);
            playerCount = itemView.findViewById(R.id.playerCount);
        }

    }

}
