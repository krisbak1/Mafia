package com.example.mafia.RolePicker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mafia.PlayerPicker.InGamePlayer;
import com.example.mafia.RolePicker.Enums.Alignment;
import com.example.mafia.RolePicker.Enums.Attack;
import com.example.mafia.RolePicker.Enums.Converters;
import com.example.mafia.RolePicker.Enums.Defense;
import com.example.mafia.RolePicker.Enums.RoleType;
import com.example.mafia.RolePicker.Enums.Targeting;

import java.io.Serializable;


@Entity(tableName = "role_table")
@TypeConverters(Converters.class)
public class Role implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private boolean isSelected;
    private Alignment alignment;
    private Targeting targeting;
    private int priority;
    private String abilityText;
    private Attack attack;
    private Defense defense;
    private RoleType roleType;
    private boolean isRoleBlockImmune;


    public Role(String name, Alignment alignment, Targeting targeting, int priority, String abilityText, Attack attack, Defense defense, RoleType roleType, boolean isRoleBlockImmune) {
        this.name = name;
        this.alignment = alignment;
        this.targeting = targeting;
        this.priority = priority;
        this.abilityText = abilityText;
        this.attack = attack;
        this.defense = defense;
        this.roleType = roleType;
        this.isSelected = !name.equalsIgnoreCase("Mafioso");
        this.isRoleBlockImmune = isRoleBlockImmune;

    }

    public Role(Role role) {
        this.id = role.id;
        this.name = role.name;
        this.alignment = role.alignment;
        this.targeting = role.targeting;
        this.priority = role.priority;
        this.abilityText = role.abilityText;
        this.attack = role.attack;
        this.defense = role.defense;
        this.roleType = role.roleType;
        this.isSelected = role.isSelected;
        this.isRoleBlockImmune = role.isRoleBlockImmune;
    }


    public void resolveChoice(InGamePlayer player, InGamePlayer target) {
    }

    public void useAbility(InGamePlayer player, InGamePlayer target) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Targeting getTargeting() {
        return targeting;
    }

    public void setTargeting(Targeting targeting) {
        this.targeting = targeting;
    }

    public int getPriority() {
        return priority;
    }

    public String getAbilityText() {
        return abilityText;
    }

    public void setAbilityText(String abilityText) {
        this.abilityText = abilityText;
    }

    public Attack getAttack() {
        return attack;
    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    public Defense getDefense() {
        return defense;
    }

    public void setDefense(Defense defense) {
        this.defense = defense;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public boolean isRoleBlockImmune() {
        return isRoleBlockImmune;
    }
}
