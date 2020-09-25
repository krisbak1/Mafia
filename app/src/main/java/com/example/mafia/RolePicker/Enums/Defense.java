package com.example.mafia.RolePicker.Enums;

public enum Defense {
    NONE(0), BASIC(1), MEDIUM(5), STRONG(10);

    private int value;

    Defense(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

