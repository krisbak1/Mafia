package com.example.mafia.PlayerPicker;

import com.example.mafia.RolePicker.Enums.Alignment;
import com.example.mafia.RolePicker.Enums.Defense;
import com.example.mafia.RolePicker.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InGamePlayer extends Player {

    private String feedback = "";
    private InGamePlayer target;
    private List<InGamePlayer> targetedBy = new ArrayList<>();
    private List<InGamePlayer> protectedBy = new ArrayList<>();
    private List<InGamePlayer> attackedBy = new ArrayList<>();
    private Role role;
    private Defense protectionStrength;
    private Status status = Status.ALIVE;
    private boolean isVictorious;


    public InGamePlayer(String name, int order, Role role) {
        super(name, order);
        this.role = role;
        isVictorious = false;
    }

    public String getFeedback() {
        return feedback;
    }

    public void appendFeedback(String feedback) {
        this.feedback = this.feedback.concat(feedback).concat("\n\n");
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


    public void setTarget(InGamePlayer target) {
        this.target = target;
    }

    public InGamePlayer getTarget() {
        return target;
    }

    public void addToTargetedBy(InGamePlayer player) {
        targetedBy.add(player);
    }

    public void addToProtectedBy(InGamePlayer player) {
        protectedBy.add(player);
    }

    public List<InGamePlayer> getProtectedBy() {
        return protectedBy;
    }

    public List<InGamePlayer> getShuffledTargetedBy() {
        Collections.shuffle(targetedBy);
        return targetedBy;
    }

    public void addToAttackedBy(InGamePlayer player) {
        attackedBy.add(player);
    }

    public Defense getProtectionStrength() {
        return protectionStrength;
    }

    public void setProtectionStrength(Defense protectionStrength) {
        this.protectionStrength = protectionStrength;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<InGamePlayer> getAttackedBy() {
        return attackedBy;
    }

    public List<InGamePlayer> getShuffledAttackedBy() {
        Collections.shuffle(attackedBy);
        return attackedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isVictorious() {
        return isVictorious;
    }

    public void setVictorious(boolean victorious) {
        isVictorious = victorious;
    }

    public void returnToDefault() {
        feedback = "";
        target = null;
        targetedBy.clear();
        protectedBy.clear();
        attackedBy.clear();
        protectionStrength = Defense.NONE;
        status = Status.ALIVE;
    }

    public void addRoleToName() {
        this.setName(this.getName() + " [" + role.getName() + "]");
    }

    public void addAlignmentToName() {
        if (this.getRole().getAlignment()
                .equals(Alignment.MAFIA)) {
            this.setName("[" + role.getAlignment().toString().substring(0, 1)
                       + "] " + this.getName());
        }
    }

    public void addMafiosoTargetToName() {
        this.setName("[X] " + this.getName());
    }


}
