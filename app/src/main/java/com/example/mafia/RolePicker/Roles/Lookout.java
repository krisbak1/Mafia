package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Role;

public class Lookout extends Role {

    public Lookout(Role lookout) {
        super(lookout);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            String result = "Your target, " + target.getName() + "\nwas visited by: ";

            if (target.getShuffledTargetedBy().size() - 1 > 0) {
                for (InGamePlayer targetOfTarget : target.getShuffledTargetedBy()) {
                    if (!targetOfTarget.getName().equalsIgnoreCase(player.getName()))
                        result = result.concat(targetOfTarget.getName()).concat(", ");
                }
                result = result.substring(0, result.length() - 2);
            } else
                result = "Your target, " + target.getName() + "\nwasn't visited by anyone tonight";

            player.appendFeedback(result);
        }
    }

    @Override
    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
        if (target != null)
            target.addToTargetedBy(player);
    }
}

