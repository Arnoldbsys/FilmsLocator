package ru.dombuketa.filmslocaror

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film (
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    var  isInFavorites: Boolean = false
) : Parcelable
