package com.example.movieappuas.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class MovieAPI(

    @SerializedName("_id") val id: String?=null, // id dapat kosong jika server generate otomatis
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "image")
    val imageUrl: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "genre")
    val genre: String
)