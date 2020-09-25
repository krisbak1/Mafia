package com.example.mafia.RolePicker.Enums;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static Attack fromStringToAttack(String value) {
        for (Attack attack : Attack.values()) {
            if (attack.toString().equals(value))
                return attack;
        }
        return null;
    }
    @TypeConverter
    public static String AttackToString(Attack attack) {
        return attack == null ? null : attack.toString();
    }

    @TypeConverter
    public static Alignment fromStringToAlignment(String value) {
        for (Alignment alignment : Alignment.values()) {
            if (alignment.toString().equals(value))
                return alignment;
        }
        return null;
    }
    @TypeConverter
    public static String AlignmentToString(Alignment alignment) {
        return alignment == null ? null : alignment.toString();
    }


    @TypeConverter
    public static Defense fromStringToDefense(String value) {
        for (Defense defense : Defense.values()) {
            if (defense.toString().equals(value))
                return defense;
        }
        return null;
    }

    @TypeConverter
    public static String DefenseToString(Defense defense) {
        return defense == null ? null : defense.toString();
    }


    @TypeConverter
    public static Targeting fromStringToTargeting(String value) {
        for (Targeting targeting : Targeting.values()) {
            if (targeting.toString().equals(value))
                return targeting;
        }
        return null;
    }

    @TypeConverter
    public static String TargetingToString(Targeting targeting) {
        return targeting == null ? null : targeting.toString();
    }

    @TypeConverter
    public static RoleType fromStringToRoleType(String value) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.toString().equals(value))
                return roleType;
        }
        return null;
    }

    @TypeConverter
    public static String RoleTypeToString(RoleType roleType) {
        return roleType == null ? null : roleType.toString();
    }
}