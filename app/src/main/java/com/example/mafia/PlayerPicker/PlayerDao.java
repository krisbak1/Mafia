package com.example.mafia.PlayerPicker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {
    @Insert
    void insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);

    @Query("DELETE FROM player_table ")
    void deleteAllPlayers();

    @Query("SELECT * FROM player_table ORDER BY `order`")
    LiveData<List<Player>> getAllPlayers();
}
