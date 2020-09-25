package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Tracker extends Role {
    public Tracker(Role tracker) {
        super(tracker);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            if (target.getTarget() != null)
                player.appendFeedback("Your target, "
                + target.getName()
                + "\nvisited " + target.getTarget().getName());
            else
                player.appendFeedback("Your target, "
                + target.getName()
                + "\ndidn't visit anyone");
        }
    }
    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null)
            target.addToTargetedBy(player);
    }
}
