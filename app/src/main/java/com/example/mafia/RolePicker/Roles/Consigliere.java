package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Consigliere extends Role {

    public Consigliere(Role consigliere) {
        super(consigliere);
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
