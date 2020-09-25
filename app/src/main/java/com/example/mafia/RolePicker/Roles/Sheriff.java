package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Enums.Alignment;
import com.example.mafia.RolePicker.Role;

public class Sheriff extends Role {

    public Sheriff(Role sheriff) {
        super(sheriff);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            if (target.getRole().getAlignment().equals(Alignment.MAFIA))
                player.appendFeedback("Your target, " + target.getName() + "\nis a member of the mafia!");
            else
                player.appendFeedback("Your target, " + target.getName() + "\nis not a member of the mafia");
        }
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null)
            target.addToTargetedBy(player);
    }
}
