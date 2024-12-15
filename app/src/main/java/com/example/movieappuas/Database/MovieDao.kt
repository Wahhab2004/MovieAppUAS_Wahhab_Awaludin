package com.example.movieappuas.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movieRoom: MovieRoom)
    @Update
    fun update(movieRoom: MovieRoom)
    @Delete
    fun delete(movieRoom: MovieRoom)
    @get:Query("SELECT * from favorite_movie_table ORDER BY id ASC")
    val allMovieRoom: LiveData<List<MovieRoom>>
}