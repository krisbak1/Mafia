package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Escort extends Role {

    public Escort(Role escort) {
        super(escort);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            if (target.getTarget() != null && !target.getRole().isRoleBlockImmune()) {
                target.setTarget(null);
                player.appendFeedback("You role blocked your target, " + target.getName());
                target.appendFeedback("You have been role blocked!");
                target.addToTargetedBy(player);
            } else if (target.getTarget() != null && target.getRole().isRoleBlockImmune()) {
                player.appendFeedback("Your target, " + target.getName() + "\nis role block immune!");
                target.addToTargetedBy(player);
            } else {
                player.appendFeedback("You role blocked your target, " + target.getName());
                target.addToTargetedBy(player);
            }
        }
    }
}
