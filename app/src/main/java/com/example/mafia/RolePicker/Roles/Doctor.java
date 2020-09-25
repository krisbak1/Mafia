package com.example.mafia.RolePicker.Roles;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Enums.Defense;
import com.example.mafia.RolePicker.Role;

public class Doctor extends Role {

    public Doctor(Role doctor) {
        super(doctor);
    }

    @Override
    public void useAbility(InGamePlayer player, InGamePlayer target) {
        if (target != null) {
            if (target.getProtectionStrength().getValue() < Defense.BASIC.getValue())
                target.setProtectionStrength(Defense.BASIC);
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