package ru.dombuketa.filmslocaror

import androidx.recyclerview.widget.DiffUtil

class FilmDiff(val oldList:ArrayList<Film>, val newList: ArrayList<Film>) :DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFilm = oldList[oldItemPosition]
        val newFilm = newList[newItemPosition]
        return oldFilm.title == newFilm.title &&
                oldFilm.description == newFilm.description &&
                oldFilm.poster == newFilm.poster
    }
}