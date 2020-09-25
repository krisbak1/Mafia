package com.example.mafia.PlayerPicker;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "player_table")
public class Player implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private boolean isSelected;
    private int order;


    public Player(String name, int order) {
        this.name = name;
        isSelected = true;
        this.order = order;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
