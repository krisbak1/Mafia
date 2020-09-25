package com.example.mafia.PlayerPicker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafia.R;
import com.example.mafia.RolePicker.RoleRandomActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_PLAYERS = "com.example.mafia.PlayerPicker.EXTRA_SELECTED_PLAYERS";
    public static final String EXTRA_SELECTED_ROLES = "com.example.mafia.RolePicker.EXTRA_SELECTED_ROLES";

    private ArrayList<Player> selectedPlayers = new ArrayList<>();
    PlayerAdapter adapter;
    private PlayerViewModel playerViewModel;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_picker);
        this.setTitle("Select players");

        // hide the navigation bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        FloatingActionButton buttonNextActivity =
                findViewById(R.id.button_next_activity);
        buttonNextActivity.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 800) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            selectedPlayers.clear();
            for (Player player
                    : playerViewModel.getAllPlayers().getValue()) {
                if (player.isSelected())
                    selectedPlayers.add(player);
            }
            if (selectedPlayers.size() < 4) {
                Toast.makeText(PlayerActivity.this,
                        "You can't play with less than 4 players",
                        Toast.LENGTH_SHORT).show();
            } else {

               Intent intent = new Intent(PlayerActivity.this,
                       RoleRandomActivity.class);
                intent.putExtra(EXTRA_SELECTED_PLAYERS, selectedPlayers);
                startActivity(intent);
            }
        });

        FloatingActionButton buttonAddPlayer = findViewById(R.id.button_add_player);
        buttonAddPlayer.setOnClickListener(v -> {
            final EditText input = new EditText(PlayerActivity.this);
            input.setSingleLine();

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            final AlertDialog dialog = new
                    AlertDialog.Builder(PlayerActivity.this)
                    .setTitle("Add new player")
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setView(input)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view -> {
                    String text = input.getText().toString().trim().replaceAll(" +", " ");
                    if (text.isEmpty())
                        dialog.dismiss();
                    else if (text.length() > 13) {
                        Toast.makeText(getApplicationContext(),"The maximum number of characters is 13", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean alreadyExists = false;
                        if (!Objects.requireNonNull(playerViewModel.getAllPlayers()
                                .getValue()).isEmpty())
                            for (Player player
                                    : playerViewModel.getAllPlayers().getValue()) {
                                if (player.getName().equalsIgnoreCase(text)) {
                                    Toast.makeText(getApplicationContext(),"That player already exists", Toast.LENGTH_SHORT).show();
                                    alreadyExists = true;
                                }
                            }
                        if (!alreadyExists) {
                            Player player = new Player(text, playerViewModel.getAllPlayers().getValue().size() + 1);
                            playerViewModel.insert(player);
                            dialog.dismiss();
                        }
                    }
                });

                input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).callOnClick();
                        return false;
                    }
                });


            });

            new Dialog(getApplicationContext());
            dialog.show();
        });

        RecyclerView recyclerView = findViewById(R.id.player_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new PlayerAdapter();
        recyclerView.setAdapter(adapter);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getAllPlayers().observe(this, players -> adapter.submitList(players));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();


                if (fromPos < toPos) {
                    for (int i = fromPos; i < toPos; i++) {
                        Collections.swap(playerViewModel.getAllPlayers().getValue(), i, i + 1);
                    }
                } else {
                    for (int i = fromPos; i > toPos; i--) {
                        Collections.swap(playerViewModel.getAllPlayers().getValue(), i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                for (int i = 0; i < playerViewModel.getAllPlayers().getValue().size(); i++) {
                    Player player = adapter.getPlayerAt(i);
                    player.setOrder(i);
                    playerViewModel.update(player);
                }
            }

            @Override
            public void onSwiped(
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    int direction) {
                Player playerToDelete = adapter.getPlayerAt(viewHolder.getAdapterPosition());
                playerViewModel.delete(playerToDelete);
                Toast.makeText(PlayerActivity.this,
                        "Player deleted", Toast.LENGTH_SHORT).show();
                for (Player player : playerViewModel.getAllPlayers().getValue()) {
                    if (player.getOrder() > playerToDelete.getOrder())
                        player.setOrder(player.getOrder() - 1);
                    playerViewModel.update(player);
                }
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(player -> {
            if (!player.isSelected()) {
                player.setSelected(true);
                playerViewModel.update(player);
            } else {
                player.setSelected(false);
                playerViewModel.update(player);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_player_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_players:
                playerViewModel.deleteAllPlayers();
                Toast.makeText(this, "All players deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.deselect_all:
                for (Player player :
                        playerViewModel.getAllPlayers().getValue()) {
                    player.setSelected(false);
                    playerViewModel.update(player);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "All players deselected",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.select_all_players:
                for (Player player :
                        playerViewModel.getAllPlayers().getValue()) {
                    player.setSelected(true);
                    playerViewModel.update(player);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "All players selected",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
