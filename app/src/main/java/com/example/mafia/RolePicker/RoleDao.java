package com.example.mafia.RolePicker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoleDao {
    @Insert
    void insert(Role role);
    @Update
    void update(Role role);
    @Delete
    void delete(Role role);

    @Query("SELECT * FROM role_table WHERE alignment='TOWN' ORDER BY id")
    LiveData<List<Role>> getAllTownRoles();

    @Query("SELECT * FROM role_table WHERE alignment='MAFIA' ORDER BY id")
    LiveData<List<Role>> getAllMafiaRoles();

    @Query("SELECT * FROM role_table WHERE alignment='NEUTRAL' ORDER BY id")
    LiveData<List<Role>> getAllNeutralRoles();
}
