package com.example.mafia.RolePicker;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafia.GameScreen.GameScreenActivity;
import com.example.mafia.PlayerPicker.Player;
import com.example.mafia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mafia.PlayerPicker.PlayerActivity.EXTRA_SELECTED_PLAYERS;
import static com.example.mafia.PlayerPicker.PlayerActivity.EXTRA_SELECTED_ROLES;

public class RoleRandomActivity extends AppCompatActivity {

    private ArrayList<Role> selectedRoles = new ArrayList<>();
    private ArrayList<Player> selectedPlayers = new ArrayList<>();
    RoleRandomAdapter adapterTown;
    RoleRandomAdapter adapterMafia;
    RoleRandomAdapter adapterNeutral;
    private long lastClickTime = 0;
    private TextView textViewMafia;

    private RoleViewModel roleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_picker);
        this.setTitle("Select roles");

        // hide the navigation bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Intent intent = getIntent();
        selectedPlayers = (ArrayList<Player>) intent.getSerializableExtra(EXTRA_SELECTED_PLAYERS);

        textViewMafia = findViewById(R.id.textViewMafia);


        FloatingActionButton buttonNextActivity = findViewById(R.id.button_next_activity);
        buttonNextActivity.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 800) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            selectedRoles.clear();
            boolean hasMafia = true;
            boolean hasTown = false;


            if (selectedPlayers.size() >= 7) {
                hasMafia = false;
                for (Role role : roleViewModel.getAllMafiaRoles().getValue()) {
                    if (role.getIsSelected()) {
                        selectedRoles.add(role);
                        hasMafia = true;
                    }
                }
            }
            selectedRoles.add(roleViewModel.getRole("Mafioso"));


            for (Role role : roleViewModel.getAllTownRoles().getValue()) {
                if (role.getIsSelected()) {
                    selectedRoles.add(role);
                    hasTown = true;
                }

            }

            if (hasMafia && hasTown) {
                for (Role role : roleViewModel.getAllNeutralRoles().getValue()) {
                    if (role.getName().equalsIgnoreCase("Jester"))
                        selectedRoles.add(role);
                    if (role.getIsSelected())
                        selectedRoles.add(role);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_SELECTED_PLAYERS, selectedPlayers);
                bundle.putSerializable(EXTRA_SELECTED_ROLES, selectedRoles);
                Intent intent1 = new Intent(getApplicationContext(), GameScreenActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                finish();

            } else if (!hasMafia && !hasTown)
                Toast.makeText(RoleRandomActivity.this, "You must select a Mafia and Town role", Toast.LENGTH_SHORT).show();
            else if (!hasMafia)
                Toast.makeText(RoleRandomActivity.this, "You must select a Mafia role", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(RoleRandomActivity.this, "You must select a Town role", Toast.LENGTH_SHORT).show();
        });

        final RecyclerView recyclerViewTown = findViewById(R.id.role_recycler_view_town);
        recyclerViewTown.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewTown.setHasFixedSize(false);

        roleViewModel = new ViewModelProvider(this).get(RoleViewModel.class);
        roleViewModel.getAllTownRoles().observe(this, roles -> adapterTown.submitList(roles));

        adapterTown = new RoleRandomAdapter(roleViewModel);
        recyclerViewTown.setAdapter(adapterTown);

        adapterTown.setOnItemClickListener(role -> {
            if (!role.getIsSelected()) {
                role.setIsSelected(true);
                roleViewModel.update(role);
            } else {
                role.setIsSelected(false);
                roleViewModel.update(role);
            }
        });


        final RecyclerView recyclerViewMafia = findViewById(R.id.role_recycler_view_mafia);
        recyclerViewMafia.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewMafia.setHasFixedSize(false);

        roleViewModel.getAllMafiaRoles().observe(this, roles -> adapterMafia.submitList(roles));

        adapterMafia = new RoleRandomAdapter(roleViewModel);
        recyclerViewMafia.setAdapter(adapterMafia);

        adapterMafia.setOnItemClickListener(role -> {
            if (!role.getIsSelected()) {
                role.setIsSelected(true);
                roleViewModel.update(role);
            } else {
                role.setIsSelected(false);
                roleViewModel.update(role);
            }
        });

        if (selectedPlayers.size() < 7) {
            recyclerViewMafia.setVisibility(View.GONE);
            textViewMafia.setVisibility(View.GONE);
        }


        final RecyclerView recyclerViewNeutral = findViewById(R.id.role_recycler_view_neutral);
        recyclerViewNeutral.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewNeutral.setHasFixedSize(false);

        roleViewModel.getAllNeutralRoles().observe(this, roles -> adapterNeutral.submitList(roles));

        adapterNeutral = new RoleRandomAdapter(roleViewModel);
        recyclerViewNeutral.setAdapter(adapterNeutral);

        adapterNeutral.setOnItemClickListener(role -> {
            if (!role.getIsSelected()) {
                role.setIsSelected(true);
                roleViewModel.update(role);
            } else {
                role.setIsSelected(false);
                roleViewModel.update(role);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_role_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all_roles:
                for (Role role : Objects.requireNonNull(roleViewModel.getAllTownRoles().getValue())) {
                    role.setIsSelected(true);
                    roleViewModel.update(role);
                }
                for (Role role : Objects.requireNonNull(roleViewModel.getAllMafiaRoles().getValue())) {
                    role.setIsSelected(true);
                    roleViewModel.update(role);
                }
                for (Role role : Objects.requireNonNull(roleViewModel.getAllNeutralRoles().getValue())) {
                    role.setIsSelected(true);
                    roleViewModel.update(role);
                }
                Toast.makeText(this, "All roles selected", Toast.LENGTH_SHORT).show();
                adapterTown.notifyDataSetChanged();
                adapterMafia.notifyDataSetChanged();
                adapterNeutral.notifyDataSetChanged();
                return true;

            case R.id.deselect_all:
                for (Role role : Objects.requireNonNull(roleViewModel.getAllTownRoles().getValue())) {
                    role.setIsSelected(false);
                    roleViewModel.update(role);
                }
                for (Role role : Objects.requireNonNull(roleViewModel.getAllMafiaRoles().getValue())) {
                    role.setIsSelected(false);
                    roleViewModel.update(role);
                }
                for (Role role : Objects.requireNonNull(roleViewModel.getAllNeutralRoles().getValue())) {
                    role.setIsSelected(false);
                    roleViewModel.update(role);
                }
                Toast.makeText(this, "All roles deselected", Toast.LENGTH_SHORT).show();
                adapterTown.notifyDataSetChanged();
                adapterMafia.notifyDataSetChanged();
                adapterNeutral.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
