package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Jester extends Role {

    public Jester(Role jester) {
        super(jester);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
    }
}
