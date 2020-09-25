package com.example.mafia.RolePicker;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mafia.RolePicker.Enums.Alignment;
import com.example.mafia.RolePicker.Enums.Attack;
import com.example.mafia.RolePicker.Enums.Defense;
import com.example.mafia.RolePicker.Enums.RoleType;
import com.example.mafia.RolePicker.Enums.Targeting;
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

@Database(entities = {Role.class}, version = 7, exportSchema = false) //export
public abstract class RoleDatabase extends RoomDatabase {

    private static com.example.mafia.RolePicker.RoleDatabase instance;

    public abstract RoleDao roleDao();

    public static synchronized com.example.mafia.RolePicker.RoleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    com.example.mafia.RolePicker.RoleDatabase.class, "role_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private RoleDao roleDao;

        private PopulateDbAsyncTask(com.example.mafia.RolePicker.RoleDatabase db) {
            roleDao = db.roleDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            roleDao.insert(new Vigilante(new Role("Vigilante", Alignment.TOWN, Targeting.ONE_TARGET, 30, "You will kill your target\nbut if you kill a town member you will commit suicide on the next night", Attack.BASIC, Defense.NONE, RoleType.KILLING, false)));
            roleDao.insert(new Escort(new Role("Escort", Alignment.TOWN, Targeting.ONE_TARGET, 10, "You will prevent your target from using their ability\nYou cannot be role blocked", Attack.NONE, Defense.NONE, RoleType.SUPPORT, true)));
            roleDao.insert(new Bodyguard(new Role("Bodyguard", Alignment.TOWN, Targeting.ONE_TARGET, 40, "If your target is attacked you die instead and you kill the attacker", Attack.BASIC, Defense.NONE, RoleType.PROTECTIVE, false)));
            roleDao.insert(new Doctor(new Role("Doctor", Alignment.TOWN, Targeting.ONE_TARGET, 41, "You will prevent your target from dying", Attack.NONE, Defense.NONE, RoleType.PROTECTIVE, false)));
            roleDao.insert(new Investigator(new Role("Investigator", Alignment.TOWN, Targeting.ONE_TARGET, 50, "You will see your target's role", Attack.NONE, Defense.NONE, RoleType.INVESTIGATIVE, false)));
            roleDao.insert(new Lookout(new Role("Lookout", Alignment.TOWN, Targeting.ONE_TARGET, 65, "You will see everyone who visited your target", Attack.NONE, Defense.NONE, RoleType.INVESTIGATIVE, false)));
            roleDao.insert(new Sheriff(new Role("Sheriff", Alignment.TOWN, Targeting.ONE_TARGET, 50, "You will see if your target is a part of the mafia or not", Attack.NONE, Defense.NONE, RoleType.INVESTIGATIVE, false)));
            roleDao.insert(new Tracker(new Role("Tracker", Alignment.TOWN, Targeting.ONE_TARGET, 60, "You will see who your target visited", Attack.NONE, Defense.NONE, RoleType.INVESTIGATIVE, false)));
            roleDao.insert(new ToughGuy(new Role("Tough guy", Alignment.TOWN, Targeting.NO_TARGET, 90, "You will not die the first time you are attacked", Attack.NONE, Defense.NONE, RoleType.BENIGN, false)));
            roleDao.insert(new Villager(new Role("Villager", Alignment.TOWN, Targeting.NO_TARGET, 90, "You have no special ability", Attack.NONE, Defense.NONE, RoleType.BENIGN, false)));
            roleDao.insert(new Consigliere(new Role("Consigliere", Alignment.MAFIA, Targeting.ONE_TARGET, 50, "You are a part of the mafia\nYou will see your target's role", Attack.NONE, Defense.NONE, RoleType.INVESTIGATIVE, false)));
            roleDao.insert(new Consort(new Role("Consort", Alignment.MAFIA, Targeting.ONE_TARGET, 11, "You are a part of the mafia\nYou will prevent your target from using their ability\nYou cannot be role blocked", Attack.NONE, Defense.NONE, RoleType.SUPPORT, true)));
            roleDao.insert(new Mafioso(new Role("Mafioso", Alignment.MAFIA, Targeting.ONE_TARGET, 31, "You are a part of the mafia\nYou will kill your target", Attack.BASIC, Defense.NONE, RoleType.KILLING, false)));
            roleDao.insert(new Executioner(new Role("Executioner", Alignment.NEUTRAL, Targeting.NO_TARGET, 91, "You win if your target is lynched,\nif he dies during the night you become a jester\nYou have defense against attacks", Attack.NONE, Defense.BASIC, RoleType.EVIL, false)));
            roleDao.insert(new Jester(new Role("Jester", Alignment.NEUTRAL, Targeting.NO_TARGET, 90, "You win if you are lynched", Attack.NONE, Defense.NONE, RoleType.EVIL, false)));
            roleDao.insert(new SerialKiller(new Role("Serial killer", Alignment.NEUTRAL, Targeting.ONE_TARGET, 32, "You will kill your target\nYou win when everyone opposing you dies", Attack.BASIC, Defense.NONE, RoleType.KILLING, false)));
            return null;
        }
    }
}
