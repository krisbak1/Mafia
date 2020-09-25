package com.example.mafia.RolePicker.Enums;

public enum Attack {
    NONE(0), BASIC(1), MEDIUM(5), STRONG(10);

    private int value;

    Attack(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
