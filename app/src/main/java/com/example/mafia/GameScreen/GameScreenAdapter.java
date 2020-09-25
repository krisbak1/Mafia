package com.example.mafia.GameScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.R;

import java.util.ArrayList;

public class GameScreenAdapter extends RecyclerView.Adapter<GameScreenAdapter.GameScreenHolder> {
    private GameScreenAdapter.OnItemClickListener listener;
    private ArrayList<InGamePlayer> players;


    public GameScreenAdapter(ArrayList<InGamePlayer> players) {
        this.players = players;
    }


    @NonNull
    @Override
    public GameScreenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new GameScreenHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameScreenHolder holder, int position) {
        InGamePlayer currentPlayer = players.get(position);
        holder.textViewName.setText(currentPlayer.getName());

        if (players.get(position).isSelected()) {
            holder.itemView.setBackgroundColor(0xFF74CF93);
            holder.textViewName.setTextColor(0xFB2C2C2C);
        } else {
            holder.itemView.setBackgroundColor(0xFF444444);
            holder.textViewName.setTextColor(0xDD9B9B9B);
        }

    }

    public InGamePlayer getPlayerAt(int position) {
        return players.get(position);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class GameScreenHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public GameScreenHolder(@NonNull View itemView) {
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
        void onItemClick(InGamePlayer player);
    }

    public void setOnItemClickListener(GameScreenAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}