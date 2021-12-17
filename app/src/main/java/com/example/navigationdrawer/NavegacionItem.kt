package com.example.navigationdrawer

sealed class NavegacionItem(var route: String, var icon: Int, var title: String)
{
    object Home : NavegacionItem("home", R.drawable.ic_home, "Home")
    object Add : NavegacionItem("add", R.drawable.ic_add, "Añadir pelicula")
}