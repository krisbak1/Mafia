package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Executioner extends Role {

    public Executioner(Role executioner) {
        super(executioner);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
    }
}
