package ru.dombuketa.filmslocaror.domain;

import java.util.List;

import ru.dombuketa.filmslocaror.data.MainRepository_J;

public class Interactor_J {
    private MainRepository_J repo;
    public Interactor_J(MainRepository_J repo) {
        this.repo = repo;
    }
    public List<Film> getFilmsDB(){
        return repo.filmsDataBase;
    }
}
