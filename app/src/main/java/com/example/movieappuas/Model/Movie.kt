package com.example.movieappuas.Model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("data")
    val `data` : List<MovieAPI>
)