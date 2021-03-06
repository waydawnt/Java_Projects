package com.spawner.nodo.util;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.spawner.nodo.data.NoDoDao;
import com.spawner.nodo.data.NoDoRoomDatabase;
import com.spawner.nodo.model.NoDo;

import java.util.List;

public class NoDoRepository {
    private NoDoDao noDoDao;
    private LiveData<List<NoDo>> allNoDos;

    public NoDoRepository(Application application) {
        NoDoRoomDatabase db = NoDoRoomDatabase.getDatabase(application);
        noDoDao = db.noDoDao();
        allNoDos = noDoDao.getAllNoDos();
    }

    public LiveData<List<NoDo>> getAllNoDos() {
        return allNoDos;
    }

    public void insert(NoDo noDo) {
        new insertAsyncTask(noDoDao).execute(noDo);
    }

    private class insertAsyncTask extends AsyncTask<NoDo, Void, Void> {
        private NoDoDao asyncTaskDao;
        public insertAsyncTask(NoDoDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(NoDo... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
