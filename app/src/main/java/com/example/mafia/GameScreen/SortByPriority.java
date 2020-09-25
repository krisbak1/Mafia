package com.example.mafia.GameScreen;

import com.example.mafia.PlayerPicker.InGamePlayer;

import java.util.Comparator;

public class SortByPriority implements Comparator<InGamePlayer> {
    public int compare(InGamePlayer a, InGamePlayer b) {
        return a.getRole().getPriority() - b.getRole().getPriority();
    }

}
