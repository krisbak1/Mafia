package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class SerialKiller extends Role {

    public SerialKiller(Role serial_killer) {
        super(serial_killer);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            target.addToAttackedBy(player);
            target.addToTargetedBy(player);
        }
    }
}
