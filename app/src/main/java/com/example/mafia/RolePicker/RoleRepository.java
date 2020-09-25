package com.example.mafia.RolePicker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RoleRepository {

    private RoleDao roleDao;
    private LiveData<List<Role>> allTownRoles;
    private LiveData<List<Role>> allMafiaRoles;
    private LiveData<List<Role>> allNeutralRoles;

    public RoleRepository(Application application) {
        RoleDatabase database = RoleDatabase.getInstance(application);
        roleDao = database.roleDao();
        allTownRoles = roleDao.getAllTownRoles();
        allMafiaRoles = roleDao.getAllMafiaRoles();
        allNeutralRoles = roleDao.getAllNeutralRoles();
    }

    public void insert(Role role) {
        new InsertRoleAsyncTask(roleDao).execute(role);
    }

    public void update(Role role) {
        new UpdateRoleAsyncTask(roleDao).execute(role);
    }


    public void delete(Role role) {
        new DeleteRoleAsyncTask(roleDao).execute(role);
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

    private static class InsertRoleAsyncTask extends AsyncTask<Role, Void, Void> {
        private RoleDao roleDao;

        private InsertRoleAsyncTask(RoleDao roleDao) {
            this.roleDao = roleDao;
        }

        @Override
        protected Void doInBackground(Role... roles) {
            roleDao.insert(roles[0]);
            return null;
        }
    }

    private static class UpdateRoleAsyncTask extends AsyncTask<Role, Void, Void> {
        private RoleDao roleDao;

        private UpdateRoleAsyncTask(RoleDao roleDao) {
            this.roleDao = roleDao;
        }

        @Override
        protected Void doInBackground(Role... roles) {
            roleDao.update(roles[0]);
            return null;
        }
    }

    private static class DeleteRoleAsyncTask extends AsyncTask<Role, Void, Void> {
        private RoleDao roleDao;

        private DeleteRoleAsyncTask(RoleDao roleDao) {
            this.roleDao = roleDao;
        }

        @Override
        protected Void doInBackground(Role... roles) {
            roleDao.delete(roles[0]);
            return null;
        }
    }


}
