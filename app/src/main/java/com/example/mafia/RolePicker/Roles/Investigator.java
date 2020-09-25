package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Investigator extends Role {

    public Investigator(Role investigator) {
        super(investigator);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        if (target != null)
            player.appendFeedback("Your target, " + target.getName() + "\nis " + target.getRole().getName());
    }


    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null)
            target.addToTargetedBy(player);
    }
}
