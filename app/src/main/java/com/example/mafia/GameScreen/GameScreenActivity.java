package com.example.mafia.GameScreen;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.PlayerPicker.Player;
import com.example.mafia.PlayerPicker.Status;
import com.example.mafia.R;
import com.example.mafia.RolePicker.Enums.Alignment;
import com.example.mafia.RolePicker.Enums.RoleType;
import com.example.mafia.RolePicker.Enums.Targeting;
import com.example.mafia.RolePicker.Role;
import com.example.mafia.RolePicker.Roles.Bodyguard;
import com.example.mafia.RolePicker.Roles.Consigliere;
import com.example.mafia.RolePicker.Roles.Consort;
import com.example.mafia.RolePicker.Roles.Doctor;
import com.example.mafia.RolePicker.Roles.Escort;
import com.example.mafia.RolePicker.Roles.Executioner;
import com.example.mafia.RolePicker.Roles.Investigator;
import com.example.mafia.RolePicker.Roles.Jester;
import com.example.mafia.RolePicker.Roles.Lookout;
import com.example.mafia.RolePicker.Roles.Mafioso;
import com.example.mafia.RolePicker.Roles.SerialKiller;
import com.example.mafia.RolePicker.Roles.Sheriff;
import com.example.mafia.RolePicker.Roles.ToughGuy;
import com.example.mafia.RolePicker.Roles.Tracker;
import com.example.mafia.RolePicker.Roles.Vigilante;
import com.example.mafia.RolePicker.Roles.Villager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.example.mafia.PlayerPicker.PlayerActivity.EXTRA_SELECTED_PLAYERS;
import static com.example.mafia.PlayerPicker.PlayerActivity.EXTRA_SELECTED_ROLES;

public class GameScreenActivity extends AppCompatActivity {

    private ArrayList<InGamePlayer> inGamePlayers = new ArrayList<>();
    private ArrayList<InGamePlayer> allPlayers = new ArrayList<>();
    private ArrayList<InGamePlayer> playersToShow = new ArrayList<>();
    private ArrayList<Player> selectedPlayers = new ArrayList<>();
    private ArrayList<Role> selectedRoles = new ArrayList<>();
    private ArrayList<Role> rolesInGame = new ArrayList<>();
    private ArrayList<Role> mafiaRoles = new ArrayList<>();
    private ArrayList<Role> neutralRoles = new ArrayList<>();

    private Role mafioso;
    private Role jester;

    private int currentNight = 0;
    private int currentPlayer;
    private int numberOfResolves = 0;
    private long lastClickTime = 0;
    private boolean restartApp = false;

    private TextView textMain;
    private TextView textSecondary;
    GameScreenAdapter adapter;
    RecyclerView recyclerView;

    FloatingActionButton button_za_prikaz_uloge_i_odabir_mete;
    FloatingActionButton button_za_prikaz_kome_proslijediti_mobitel_za_feedback;
    FloatingActionButton button_za_prikaz_feedbacka;
    FloatingActionButton button_za_prikaz_koji_je_dan;
    FloatingActionButton button_za_prikaz_tko_je_umro;
    FloatingActionButton button_za_prikaz_liste_za_glasanje;
    FloatingActionButton button_za_prikaz_koja_je_noc;
    FloatingActionButton button_za_prikaz_kome_proslijediti_mobitel;
    FloatingActionButton button_za_prikaz_tko_je_objesen;
    FloatingActionButton button_za_pocetak_nove_igre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // hide the navigation bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        textMain = findViewById(R.id.Game_screen_text_main);
        textSecondary = findViewById(R.id.Game_Screen_text_secondary);

        button_za_prikaz_uloge_i_odabir_mete = findViewById(R.id.button_za_prikaz_uloge_i_odabir_mete);
        button_za_prikaz_kome_proslijediti_mobitel_za_feedback = findViewById(R.id.button_za_prikaz_kome_proslijediti_mobitel_za_feedback);
        button_za_prikaz_feedbacka = findViewById(R.id.button_za_prikaz_feedbacka);
        button_za_prikaz_koji_je_dan = findViewById(R.id.button_za_prikaz_koji_je_dan);
        button_za_prikaz_tko_je_umro = findViewById(R.id.button_za_prikaz_tko_je_umro);
        button_za_prikaz_liste_za_glasanje = findViewById(R.id.button_za_prikaz_liste_za_glasanje);
        button_za_prikaz_koja_je_noc = findViewById(R.id.button_za_prikaz_koja_je_noc);
        button_za_prikaz_kome_proslijediti_mobitel = findViewById(R.id.button_za_prikaz_kome_proslijediti_mobitel);
        button_za_prikaz_tko_je_objesen = findViewById(R.id.button_za_prikaz_tko_je_objesen);
        button_za_pocetak_nove_igre = findViewById(R.id.button_za_pocetak_nove_igre);

        // postavljanje recyclerViewa
        recyclerView = findViewById(R.id.targetsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        adapter = new GameScreenAdapter(playersToShow);
        adapter.setOnItemClickListener(player -> {
            if (inGamePlayers.get(currentPlayer)
                             .getRole().getAlignment()
                             .equals(Alignment.MAFIA)
                && player.getRole().getAlignment().equals(Alignment.MAFIA)
                && button_za_prikaz_tko_je_objesen.getVisibility() != View.VISIBLE) {
                return;
            }
            if (player.isSelected()) {
                player.setSelected(false);
                inGamePlayers.get(currentPlayer).setTarget(null);
            } else {
                for (InGamePlayer x : inGamePlayers) x.setSelected(false);
                player.setSelected(true);
                inGamePlayers.get(currentPlayer).setTarget(player);
            }
        });
        recyclerView.setAdapter(adapter);


        //Skupljanje podataka od prošlog Activity-a
        Bundle b = getIntent().getExtras();
        if (b != null) {
            selectedPlayers = (ArrayList<Player>) b.getSerializable(EXTRA_SELECTED_PLAYERS);
            selectedRoles = (ArrayList<Role>) b.getSerializable(EXTRA_SELECTED_ROLES);
        }


        //Mafioso se odvaja jer mora biti uvijek jedan u igri i uvijek prvi u krugu
        for (Role role : Objects.requireNonNull(selectedRoles)) {
            if (role.getName().equalsIgnoreCase("Mafioso"))
                mafioso = role;
        }
        selectedRoles.removeIf(x -> (x.getName().equals("Mafioso")));

        //Jester se odvaja za pretvaranje Executionera u Jestera
        for (Role role : Objects.requireNonNull(selectedRoles)) {
            if (role.getName().equalsIgnoreCase("Jester"))
                jester = role;
        }
        for (int i = 0; i < selectedRoles.size(); i++) {
            if (selectedRoles.get(i).getName().equalsIgnoreCase("Jester")) {
                selectedRoles.remove(i);
                break;
            }
        }
        selectedRoles.removeIf(x -> (x.getName().equals("Mafioso")));


        //Odvajanje mafijaških uloga za reguliranje broja članova
        for (Role role : Objects.requireNonNull(selectedRoles)) {
            if (role.getAlignment().equals(Alignment.MAFIA))
                mafiaRoles.add(role);
        }
        selectedRoles.removeIf(x -> (x.getAlignment().equals(Alignment.MAFIA)));


        //Odvajanje neutralnih uloga zbog reguliranja broja članova
        for (Role role : Objects.requireNonNull(selectedRoles)) {
            if (role.getAlignment().equals(Alignment.NEUTRAL))
                neutralRoles.add(role);
        }

        //Ostavljamo samo Town uloge u selectedRoles
        selectedRoles.removeIf(x -> (x.getAlignment().equals(Alignment.NEUTRAL)));


        //U slučaju da nema odabranih mafijaških uloga doda se mafioso
        //
        //samo u slučaju 6 ili manje igrača (provjera unutar RoleRandomActivity)
        if (mafiaRoles.isEmpty())
            mafiaRoles.add(mafioso);


        //Nasumično stvaranje Uloga i spremanje istih u listu rolesInGame
        Random rand = new Random();
        int randomNumber;
        //Kreiranje mafije
        double numberOfMafiaDouble = selectedPlayers.size() / 3.3;
        int numberOfMafia = (int) numberOfMafiaDouble;
        for (int i = 0; i < numberOfMafia; i++) {
            randomNumber = rand.nextInt(mafiaRoles.size());
            rolesInGame.add(createRole(mafiaRoles, randomNumber));
        }
        //Kreiranje neutrala
        int numberOfNeutrals = rand.nextInt(
                (int) (selectedPlayers.size() / 5) + 1);
        for (int i = 0; i < numberOfNeutrals; i++) {
            if (neutralRoles.size() > 0) {
                randomNumber = rand.nextInt(neutralRoles.size());
                Role role = createRole(neutralRoles, randomNumber);
                if (role != null) {
                if (role.getRoleType().equals(RoleType.KILLING)
                    && numberOfMafia > 1) {
                    rolesInGame.add(role);
                    if (numberOfMafiaDouble - numberOfMafia < 0.2) {
                        numberOfMafia--;
                        rolesInGame.remove(0);
                    } else {
                        i++;
                    }
                    neutralRoles.removeIf(x ->
                    (x.getName().equalsIgnoreCase(role.getName())));
                } else {
                    rolesInGame.add(role);
                }
                }
            }
        }

        //Kreiranje Towna
        int numberOfTownies = selectedPlayers.size() - rolesInGame.size();
        for (int i = 0; i < numberOfTownies; i++) {
            randomNumber = rand.nextInt(selectedRoles.size());
            rolesInGame.add(createRole(selectedRoles, randomNumber));
        }

        //Dodjela uloga igracima
        Collections.shuffle(rolesInGame);
        for (int i = 0; i < selectedPlayers.size(); i++)
            inGamePlayers.add(
                new InGamePlayer(
                (selectedPlayers.get(i).getName()), 0,
                rolesInGame.get(i)));

        allPlayers.addAll(inGamePlayers);

        //Assign target for Executioners
        for (InGamePlayer player : inGamePlayers) {
            if (player.getRole().getName().equalsIgnoreCase("Executioner")) {
                ArrayList<InGamePlayer> townMembers = new ArrayList<>();

                for (InGamePlayer townMember : inGamePlayers) {
                    if (townMember.getRole().getAlignment().equals(Alignment.TOWN))
                        townMembers.add(townMember);
                }
                player.getRole().setAbilityText(player.getRole().getAbilityText() + "\n\n" + "Your target is " + townMembers.get(rand.nextInt(townMembers.size())).getName());
            }
        }


        //Game loop
        //
        //prikazuje se koja je noc po redu
        button_za_prikaz_koja_je_noc.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                lastClickTime = SystemClock.elapsedRealtime() - 1000;
                recyclerView.setVisibility(View.GONE);
                currentNight++;
                textMain.setText(currentNight + ". night");
                textSecondary.setText("");
                button_za_prikaz_koja_je_noc.hide();
                button_za_prikaz_kome_proslijediti_mobitel.show();
                currentPlayer = -1;
                for (InGamePlayer player : inGamePlayers) {
                    player.returnToDefault();
                }
                checkVictory();
                checkMafioso();
            }
        });

        //prikaz kome proslijediti
        button_za_prikaz_kome_proslijediti_mobitel.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                button_za_prikaz_kome_proslijediti_mobitel.hide();
                button_za_prikaz_uloge_i_odabir_mete.show();
                if (currentPlayer + 1 < inGamePlayers.size())
                    currentPlayer++;
                showOtherMafiaMembers(false);
                textMain.setText("Pass the device to \n" + inGamePlayers.get(currentPlayer).getName());
                textSecondary.setText("");
                recyclerView.setVisibility(View.GONE);
            }
        });

        //prikaz uloge
        button_za_prikaz_uloge_i_odabir_mete.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                textMain.setText("Your role is \n" + inGamePlayers.get(currentPlayer).getRole().getName());
                textSecondary.setText(inGamePlayers.get(currentPlayer).getRole().getAbilityText());
                if (!inGamePlayers.get(currentPlayer).getRole().getTargeting().equals(Targeting.NO_TARGET)) {
                    playersToShow.clear();
                    for (InGamePlayer player : inGamePlayers) {
                        if (!player.equals(inGamePlayers.get(currentPlayer))) {
                            playersToShow.add(player);
                        }
                    }
                    if (inGamePlayers.get(currentPlayer).getRole().getAlignment().equals(Alignment.MAFIA))
                        showOtherMafiaMembers(true);
                    else
                        showOtherMafiaMembers(false);

                    for (InGamePlayer player : inGamePlayers) {
                        player.setSelected(false);
                    }
                    recyclerView.setVisibility(View.VISIBLE);

                } else
                    recyclerView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();

                button_za_prikaz_uloge_i_odabir_mete.hide();
                if (currentPlayer + 1 < inGamePlayers.size()) {
                    button_za_prikaz_kome_proslijediti_mobitel.show();
                } else {
                    button_za_prikaz_kome_proslijediti_mobitel_za_feedback.show();
                }
            }
        });


        //igra radi drugi krug gdje prikazuje svakom igracu sto mu se dogodilo tu noc
        //
        //prikaz kome proslijediti
        button_za_prikaz_kome_proslijediti_mobitel_za_feedback.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                showOtherMafiaMembers(false);
                resolveChoices();
                recyclerView.setVisibility(View.GONE);
                textMain.setText("Pass the device to " + inGamePlayers.get(currentPlayer).getName());
                textSecondary.setText("");
                button_za_prikaz_feedbacka.show();
                button_za_prikaz_kome_proslijediti_mobitel_za_feedback.hide();
            }
        });

        //prikaz uloge i sto se dogodilo toj osobi
        button_za_prikaz_feedbacka.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                textMain.setText("Your role is \n" + inGamePlayers.get(currentPlayer).getRole().getName());
                textSecondary.setText(inGamePlayers.get(currentPlayer).getFeedback());
                currentPlayer++;
                button_za_prikaz_feedbacka.hide();
                if (currentPlayer < inGamePlayers.size()) {
                    button_za_prikaz_kome_proslijediti_mobitel_za_feedback.show();
                } else {
                    button_za_prikaz_koji_je_dan.show();
                    currentPlayer = 0;
                }
            }
        });


        //igra ulazi u fazu dana
        //
        //prikazuje se koji je dan po redu
        button_za_prikaz_koji_je_dan.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                lastClickTime = SystemClock.elapsedRealtime() - 1000;
                textMain.setText(currentNight + ". day");
                textSecondary.setText("");
                button_za_prikaz_koji_je_dan.hide();
                button_za_prikaz_tko_je_umro.show();
                currentPlayer = 0;
            }
        });

        //prikaz igraca koji su poginuli tu noc
        button_za_prikaz_tko_je_umro.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                for (int i = currentPlayer; i < inGamePlayers.size(); i++) {
                    if (inGamePlayers.get(i).getStatus().equals(Status.KILLED) || inGamePlayers.get(i).getStatus().equals(Status.SUICIDE)) {
                        if (inGamePlayers.get(i).getStatus().equals(Status.SUICIDE)) {
                            textMain.setText(inGamePlayers.get(i).getName() + " has died");
                            textSecondary.setText("His role was " + inGamePlayers.get(i).getRole().getName());
                            textSecondary.append("\n\nHe killed himself out of guilt");

                        } else {
                            textMain.setText(inGamePlayers.get(i).getName() + " has died");
                            textSecondary.setText("His role was " + inGamePlayers.get(i).getRole().getName());
                            String showAttackers = textSecondary.getText().toString() + "\n\nHe was killed by: \n";
                            for (InGamePlayer attacker : inGamePlayers.get(i).getAttackedBy()) {
                                showAttackers = showAttackers.concat(attacker.getRole().getName() + ", ");
                            }
                            showAttackers = showAttackers.substring(0, showAttackers.length() - 2);
                            textSecondary.setText(showAttackers);
                        }

                        currentPlayer = i;
                        boolean moreDeadToShow = false;
                        for (int j = currentPlayer + 1; j < inGamePlayers.size(); j++) {
                            if (inGamePlayers.get(j).getStatus().equals(Status.KILLED))
                                moreDeadToShow = true;
                        }
                        if (!moreDeadToShow) {
                            currentPlayer = 0;
                            button_za_prikaz_tko_je_umro.hide();
                            button_za_prikaz_liste_za_glasanje.show();
                        }

                        for (InGamePlayer player : inGamePlayers) {
                            if (player.getRole().getName().equalsIgnoreCase("Executioner") && player.getRole().getAbilityText().substring(player.getRole().getAbilityText().lastIndexOf(" ") + 1).equalsIgnoreCase(inGamePlayers.get(i).getName())) {
                                player.setRole(new Jester(jester));
                                player.getRole().setAbilityText(player.getRole().getAbilityText().concat("\nYour target died during the night so you became a Jester"));
                            }
                        }
                        inGamePlayers.remove(i);
                        return;
                    }
                }

                if (currentPlayer == 0) {
                    textMain.setText("No one died tonight");
                    textSecondary.setText("");
                    button_za_prikaz_tko_je_umro.hide();
                    button_za_prikaz_liste_za_glasanje.show();
                }
            }
        });

        //prikaz za glasanje igraca
        button_za_prikaz_liste_za_glasanje.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                textMain.setText("Choose a player you want to lynch");
                textSecondary.setText("");
                playersToShow.clear();
                playersToShow.addAll(inGamePlayers);
                adapter.notifyDataSetChanged();
                for (InGamePlayer player : inGamePlayers) player.setSelected(false);
                recyclerView.setVisibility(View.VISIBLE);
                button_za_prikaz_liste_za_glasanje.hide();
                button_za_prikaz_tko_je_objesen.show();
                checkVictory();
            }
        });

        //prikaz izglasanog igraca
        button_za_prikaz_tko_je_objesen.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                boolean playerShown = false;
                for (InGamePlayer player : inGamePlayers) {
                    if (player.isSelected()) {
                        recyclerView.setVisibility(View.GONE);
                        textMain.setText(player.getName() + " has died");
                        textSecondary.setText("His role was " + player.getRole().getName());
                        playerShown = true;

                        if (player.getRole().getName().equalsIgnoreCase("Jester")) {
                            for (InGamePlayer player2 : allPlayers) {
                                if (player2.getName().equalsIgnoreCase(player.getName()) && !player2.isVictorious())
                                    player2.setVictorious(true);
                            }
                        }

                        for (InGamePlayer player2 : inGamePlayers) {
                            if (player2.getRole().getName().equalsIgnoreCase("Executioner") && player2.getRole().getAbilityText().substring(player2.getRole().getAbilityText().lastIndexOf(" ") + 1).equalsIgnoreCase(player.getName())) {
                                for (InGamePlayer player3 : allPlayers) {
                                    if (player3.getName().equalsIgnoreCase(player2.getName()) && !player3.isVictorious()) {
                                        player3.setVictorious(true);
                                        player2.getRole().setAbilityText("You won!\n\nYou are still in the game, and you win even if you die");
                                    }

                                }
                            }
                        }
                    }
                }

                if (playerShown) {
                    inGamePlayers.removeIf(Player::isSelected);
                    button_za_prikaz_tko_je_objesen.hide();
                    button_za_prikaz_koja_je_noc.show();
                } else {
                    lastClickTime = 0;
                    button_za_prikaz_tko_je_objesen.hide();
                    button_za_prikaz_koja_je_noc.callOnClick();
                }
            }
        });

        //Po zavrsetku igre se prikazuje 'Replay' button koji resetira igru
        button_za_pocetak_nove_igre.setOnClickListener(v -> {
            if (preventDoubleClick()) {
                restartApp = true;
                onBackPressed();
            }
        });

        //pokretanje prvog prikaza
        button_za_prikaz_koja_je_noc.callOnClick();
    }


    private void resolveChoices() {
        if (numberOfResolves < currentNight) {
            numberOfResolves++;
            currentPlayer = 0;
            ArrayList<InGamePlayer> playersByPriority = new ArrayList<>(inGamePlayers);
            Collections.sort(playersByPriority, new SortByPriority());

            for (InGamePlayer player : playersByPriority) {
                player.getRole().resolveChoice(player, player.getTarget());
            }

            for (InGamePlayer player : playersByPriority) {
                player.getRole().useAbility(player, player.getTarget());
            }


            for (InGamePlayer player : playersByPriority) {
                int highestAttack = 0;
                if (player.getAttackedBy() != null) {
                    for (InGamePlayer attacker : player.getAttackedBy()) {
                        if (attacker.getRole().getAttack().getValue() > player.getRole().getDefense().getValue() && attacker.getRole().getAttack().getValue() > player.getProtectionStrength().getValue()) {
                            if (player.getRole().getName().equalsIgnoreCase("Tough guy") && player.getRole().getAbilityText().equalsIgnoreCase("You don't die the first time you are attacked")) {
                                player.setStatus(Status.PROTECTED);
                                player.getRole().setAbilityText("You survived last time you were attacked\n\nNext time you are attacked you will die");
                            } else {
                                player.setFeedback("You have died!\n\n");
                                if (attacker.getStatus() != Status.KILLED)
                                    attacker.appendFeedback("You killed your target, " + player.getName());
                                player.setStatus(Status.KILLED);
                            }
                        }
                        if (attacker.getRole().getAttack().getValue() > highestAttack)
                            highestAttack = attacker.getRole().getAttack().getValue();
                    }


                    if (highestAttack != 0 && (highestAttack <= player.getRole().getDefense().getValue() || highestAttack <= player.getProtectionStrength().getValue()))
                        player.setStatus(Status.PROTECTED);

                    if (player.getStatus().equals(Status.PROTECTED)) {
                        for (InGamePlayer protector : player.getProtectedBy()) {
                            protector.appendFeedback("You saved your target, " + player.getName() + " from an attack!");
                        }
                    }


                    for (InGamePlayer attacker : player.getAttackedBy()) {
                        if (attacker.getRole().getName().equalsIgnoreCase("Vigilante") && highestAttack <= attacker.getRole().getAttack().getValue())
                            attacker.getRole().useAbility(attacker, player);
                    }
                }
            }

            for (InGamePlayer player : playersByPriority) {
                if (!player.getFeedback().contains("You have died!")) {
                    if (player.getTarget() != null && player.getRole().getRoleType().equals(RoleType.KILLING)) {
                        if (player.getTarget().getStatus().equals(Status.PROTECTED))
                            player.appendFeedback("Your target, " + player.getTarget().getName() + " survived your attack!");
                    }
                    if (player.getTarget() == null && player.getRole().getTargeting().equals(Targeting.ONE_TARGET) && !player.getFeedback().contains("You have been role blocked!"))
                        player.appendFeedback("You didn't target anyone last night");

                    if (player.getFeedback().equalsIgnoreCase(""))
                        player.setFeedback("Nothing interesting happened last night");
                }
            }
        }
    }

    private Role createRole(ArrayList<Role> roles, int randomNumber) {
        switch (roles.get(randomNumber).getName()) {
            case "Bodyguard":
                return new Bodyguard(roles.get(randomNumber));
            case "Consigliere":
                return new Consigliere(roles.get(randomNumber));
            case "Consort":
                return new Consort(roles.get(randomNumber));
            case "Doctor":
                return new Doctor(roles.get(randomNumber));
            case "Escort":
                return new Escort(roles.get(randomNumber));
            case "Executioner":
                return new Executioner(roles.get(randomNumber));
            case "Investigator":
                return new Investigator(roles.get(randomNumber));
            case "Jester":
                return new Jester(roles.get(randomNumber));
            case "Lookout":
                return new Lookout(roles.get(randomNumber));
            case "Serial killer":
                return new SerialKiller(roles.get(randomNumber));
            case "Sheriff":
                return new Sheriff(roles.get(randomNumber));
            case "Tough guy":
                return new ToughGuy(roles.get(randomNumber));
            case "Tracker":
                return new Tracker(roles.get(randomNumber));
            case "Villager":
                return new Villager(roles.get(randomNumber));
            case "Mafioso":
                return new Mafioso(roles.get(randomNumber));
            case "Vigilante":
                return new Vigilante(roles.get(randomNumber));
            default:
                return null;
        }
    }

    //Ako prvi clan mafije nije mafioso postaje mafioso
    private void checkMafioso() {
        for (InGamePlayer player : inGamePlayers) {
            if (player.getRole()
                    .getAlignment().equals(Alignment.MAFIA)) {
                if (!player.getRole()
                        .getName()
                        .equalsIgnoreCase("Mafioso")) {
                    player.setRole(new Mafioso(mafioso));
                    break;
                } else
                    break;
            }
        }
    }

    private boolean preventDoubleClick() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000)
            return false;
        else {
            lastClickTime = SystemClock.elapsedRealtime();
            return true;
        }

    }


    private void showOtherMafiaMembers(boolean showMafiaMembers) {
        if (showMafiaMembers) {
            String mafiosoTarget = "";
            for (InGamePlayer player : inGamePlayers) {
                if (player.getRole().getName().equalsIgnoreCase("Mafioso") && player.getTarget() != null) {
                    mafiosoTarget = player.getTarget().getName();
                }
            }
            for (InGamePlayer player : inGamePlayers) {
                player.addAlignmentToName();
                if (player.getName().equalsIgnoreCase(mafiosoTarget))
                    player.addMafiosoTargetToName();
            }
        } else {
            for (InGamePlayer player : inGamePlayers) {
                int index = player.getName().indexOf(" ");
                if (index > 0) {
                    if (player.getName().substring(0, player.getName().indexOf(" ")).equalsIgnoreCase("[M]") || player.getName().substring(0, player.getName().indexOf(" ")).equalsIgnoreCase("[X]"))
                        player.setName(player.getName().substring(player.getName().indexOf(" ") + 1));
                }
            }
        }
    }

    private void checkVictory() {
        int numberOfTownies = 0;
        int numberOfMafia = 0;
        int numberOfNeutrals = 0;
        int numberOfKillingNeutrals = 0;
        for (InGamePlayer player : inGamePlayers) {
            if (player.getRole().getAlignment().equals(Alignment.TOWN))
                numberOfTownies++;
            if (player.getRole().getAlignment().equals(Alignment.MAFIA))
                numberOfMafia++;
            if (player.getRole().getAlignment().equals(Alignment.NEUTRAL)) {
                numberOfNeutrals++;
                if (player.getRole().getRoleType().equals(RoleType.KILLING))
                    numberOfKillingNeutrals++;
            }

        }

        if (numberOfKillingNeutrals == 1
                && numberOfMafia == 0
                && numberOfTownies == 0) {
            button_za_prikaz_kome_proslijediti_mobitel.hide();
            button_za_prikaz_tko_je_objesen.hide();
            button_za_pocetak_nove_igre.show();
            String winnerName = "";
            for (InGamePlayer player : inGamePlayers) {
                if (player.getRole().getRoleType().equals(RoleType.KILLING))
                    winnerName = player.getRole().getName();
            }
            textMain.setText(winnerName + " is victorious!");
            textSecondary.setText("Players highlighted green have won");
            playersToShow.clear();
            for (InGamePlayer player : allPlayers) {
                if (player.getRole().getName().equalsIgnoreCase(winnerName)
                        || player.isVictorious()) {
                    player.setSelected(true);
                    player.addRoleToName();
                } else {
                    player.setSelected(false);
                    player.addRoleToName();
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            playersToShow.addAll(allPlayers);
            adapter.setOnItemClickListener(null);
            adapter.notifyDataSetChanged();
        } else if (numberOfKillingNeutrals == 0
            && numberOfMafia > (numberOfTownies + numberOfNeutrals)) {
            endGame("Mafia is victorious", Alignment.MAFIA);
        } else if (numberOfKillingNeutrals == 0
                && numberOfMafia == 0
                && numberOfTownies != 0) {
            endGame("Town is victorious!", Alignment.TOWN);
        } else if (numberOfNeutrals == 0
                && numberOfMafia == 0
                && numberOfTownies == 0) {
            endGame("Everyone died", null);
        }
    }

    private void endGame(String string, Alignment alignment) {
        button_za_prikaz_kome_proslijediti_mobitel.hide();
        button_za_prikaz_tko_je_objesen.hide();
        button_za_pocetak_nove_igre.show();
        textMain.setText(string);
        textSecondary.setText("Players highlighted green have won");
        playersToShow.clear();
        recyclerView.setVisibility(View.VISIBLE);
        playersToShow.addAll(allPlayers);

        for (InGamePlayer player : allPlayers) {
            if (player.getRole().getAlignment().equals(alignment)
                    || player.isVictorious()) {
                player.setSelected(true);
                player.addRoleToName();
            } else {
                player.setSelected(false);
                player.addRoleToName();
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(null);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        if (restartApp)
            super.onBackPressed();
    }
}