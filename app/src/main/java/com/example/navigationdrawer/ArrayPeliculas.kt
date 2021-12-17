package com.example.navigationdrawer

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

class ArrayPeliculas {
    companion object {
        var listapeliculas : MutableList<JsonFilm> = mutableStateListOf<JsonFilm>()

    }

}