package com.example.mafia.RolePicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafia.R;

public class RoleRandomAdapter extends ListAdapter<Role, RoleRandomAdapter.RoleHolder> {
    private OnItemClickListener listener;


    public RoleRandomAdapter(RoleViewModel roles) {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Role> DIFF_CALLBACK = new DiffUtil.ItemCallback<Role>() {
        @Override
        public boolean areItemsTheSame(@NonNull Role oldItem, @NonNull Role newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Role oldItem, @NonNull Role newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public RoleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_card_without_counter, parent, false);
        return new RoleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleHolder holder, int position) {
        holder.setIsRecyclable(false);
        Role currentRole = getItem(position);
        holder.textViewName.setText(currentRole.getName());

        if (currentRole.getIsSelected()) {
            holder.linearLayout.setAlpha(1);
        } else {
            holder.linearLayout.setAlpha(0.4f);
        }

        if (currentRole.getName().equalsIgnoreCase("Mafioso")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

        }

    }

    public Role getRoleAt(int position) {
        return getItem(position);
    }


    class RoleHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private LinearLayout linearLayout;

        public RoleHolder(@NonNull final View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.role_name);
            linearLayout = itemView.findViewById(R.id.linear_layout);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getRoleAt(position));
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Role role);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
