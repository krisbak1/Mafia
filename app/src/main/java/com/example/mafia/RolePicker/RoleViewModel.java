package com.example.mafia.RolePicker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

public class RoleViewModel extends AndroidViewModel {
    private RoleRepository repository;
    private LiveData<List<Role>> allTownRoles;
    private LiveData<List<Role>> allMafiaRoles;
    private LiveData<List<Role>> allNeutralRoles;


    public RoleViewModel(@NonNull Application application) {
        super(application);
        repository = new RoleRepository(application);
        allTownRoles = repository.getAllTownRoles();
        allMafiaRoles = repository.getAllMafiaRoles();
        allNeutralRoles = repository.getAllNeutralRoles();
    }

    public void insert(Role role) {
        repository.insert(role);
    }

    public void update(Role role) {
        repository.update(role);
    }

    public void delete(Role role) {
        repository.delete(role);
    }

    public LiveData<List<Role>> getAllTownRoles() {
        return allTownRoles;
    }

    public LiveData<List<Role>> getAllMafiaRoles() {
        return allMafiaRoles;
    }

    public LiveData<List<Role>> getAllNeutralRoles() {
        return allNeutralRoles;
    }

    public Role getRole(String name) {
        for (Role role : Objects.requireNonNull(allTownRoles.getValue())) {
            if (role.getName().equalsIgnoreCase(name))
                return role;
        }
        for (Role role : Objects.requireNonNull(allMafiaRoles.getValue())) {
            if (role.getName().equalsIgnoreCase(name))
                return role;
        }
        for (Role role : Objects.requireNonNull(allNeutralRoles.getValue())) {
            if (role.getName().equalsIgnoreCase(name))
                return role;
        }
        return null;
    }
}

