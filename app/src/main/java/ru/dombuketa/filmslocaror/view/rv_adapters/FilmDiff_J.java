package ru.dombuketa.filmslocaror.view.rv_adapters;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

import ru.dombuketa.db_module.dto.Film;


public class FilmDiff_J extends DiffUtil.Callback {
    ArrayList<Film> oldList;
    ArrayList<Film> newList;

    public FilmDiff_J(ArrayList<Film> oldList, ArrayList<Film> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Film oldFilm = oldList.get(oldItemPosition);
        Film newFilm = newList.get(newItemPosition);
        return oldFilm.getTitle() == newFilm.getTitle() &&
                oldFilm.getDescription() == newFilm.getDescription() &&
                oldFilm.getPoster() == newFilm.getPoster();
    }
}
