package com.spawner.nodo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelStoreOwner;

import com.spawner.nodo.util.NoDoRepository;

import java.util.List;

public class NoDoViewModel extends AndroidViewModel implements ViewModelStoreOwner {
    private NoDoRepository noDoRepository;
    private LiveData<List<NoDo>> allNoDos;

    public NoDoViewModel(@NonNull Application application) {
        super(application);
        noDoRepository = new NoDoRepository(application);
        allNoDos = noDoRepository.getAllNoDos();
    }

    LiveData<List<NoDo>> getAllNoDos() {
        return allNoDos;
    }

    public void insert(NoDo noDo) {
        noDoRepository.insert(noDo);
    }
}
