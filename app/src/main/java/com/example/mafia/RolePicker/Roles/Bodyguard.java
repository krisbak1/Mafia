package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.PlayerPicker.Status;
import com.example.mafia.RolePicker.Enums.Defense;
import com.example.mafia.RolePicker.Role;

public class Bodyguard extends Role {

    private int protectionStrength = Defense.BASIC.getValue();

    public Bodyguard(Role bodyguard) {
        super(bodyguard);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        String attackerToRemove = "";
        if (target != null) {
            if (target.getAttackedBy() != null) {
                for (InGamePlayer attacker : target.getShuffledAttackedBy()) {
                    if (attacker.getRole().getAttack().getValue() == protectionStrength) {
                        attackerToRemove = attacker.getName();
                        break;
                    }
                }

                if (!attackerToRemove.equalsIgnoreCase("")) {
                    for (int i = 0; i < target.getAttackedBy().size(); i++) {
                        if (target.getAttackedBy().get(i).getName().equalsIgnoreCase(attackerToRemove)) {
                            player.addToAttackedBy(target.getAttackedBy().get(i));
                            target.getAttackedBy().get(i).appendFeedback("A bodyguard protected your previous target, " + target.getName());
                            target.getAttackedBy().get(i).setTarget(player);
                            target.getAttackedBy().get(i).addToAttackedBy(player);
                            target.getAttackedBy().remove(i);
                            target.setStatus(Status.PROTECTED);
                            return;
                        }
                    }
                }
            }
        }

    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            target.addToTargetedBy(player);
            target.addToProtectedBy(player);
        }
    }
}
