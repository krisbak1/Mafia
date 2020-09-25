package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.PlayerPicker.Status;
import com.example.mafia.RolePicker.Enums.Alignment;
import com.example.mafia.RolePicker.Enums.Attack;
import com.example.mafia.RolePicker.Enums.Defense;
import com.example.mafia.RolePicker.Enums.Targeting;
import com.example.mafia.RolePicker.Role;

public class Vigilante extends Role {

    private boolean killedTownMember = false;

    public Vigilante(Role vigilante) {
        super(vigilante);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            if (target.getRole().getAlignment().equals(Alignment.TOWN) && target.getStatus().equals(Status.KILLED)) {
                killedTownMember = true;
                player.getRole().setAttack(Attack.STRONG);
                player.getRole().setAbilityText("Last night you killed a town member\n\nYou will kill yourself out of guilt");
                player.getRole().setTargeting(Targeting.NO_TARGET);
            }
        }
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (!killedTownMember) {
            if (target != null) {
                target.addToAttackedBy(player);
                target.addToTargetedBy(player);
            }
        } else {
            player.setFeedback("You have died out of guilt");
            player.setStatus(Status.SUICIDE);
            player.getRole().setDefense(Defense.STRONG);
        }
    }
}
