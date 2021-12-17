package com.example.navigationdrawer

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val base_film = "http://iesayala.ddns.net/sergioRuiz/"

interface FilmInterface {
    @GET("jsonpeliculas.php")
    fun filmInformation(): Call<FilmInfo>
}

object FilmInstance {
    val filmInterface: FilmInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_film)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        filmInterface = retrofit.create(FilmInterface::class.java)
    }


}