package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Mafioso extends Role {

    public Mafioso(Role mafioso) {
        super(mafioso);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            target.addToTargetedBy(player);
            target.addToAttackedBy(player);
        }
    }
}
