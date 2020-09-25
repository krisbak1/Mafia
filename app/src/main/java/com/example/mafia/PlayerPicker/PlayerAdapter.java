package com.example.mafia.PlayerPicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafia.R;

public class PlayerAdapter extends ListAdapter<Player, PlayerAdapter.PlayerHolder> {
    private OnItemClickListener listener;


    public PlayerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Player>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<Player>() {
        @Override
        public boolean areItemsTheSame(@NonNull Player oldItem,
                                       @NonNull Player newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Player oldItem,
                                          @NonNull Player newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };



    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        Player currentPlayer = getItem(position);
        holder.textViewName.setText(currentPlayer.getName());

        if (currentPlayer.isSelected()) {
            holder.itemView.setBackgroundColor(0xFF74CF93);
            holder.textViewName.setTextColor(0xFB2C2C2C);
        } else {
            holder.itemView.setBackgroundColor(0xFF444444);
            holder.textViewName.setTextColor(0xDD9B9B9B);
        }

    }

    public Player getPlayerAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new PlayerHolder(itemView);
    }


    class PlayerHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public PlayerHolder(@NonNull final View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getPlayerAt(position));
                    notifyDataSetChanged();
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Player player);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
