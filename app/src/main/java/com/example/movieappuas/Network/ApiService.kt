package com.example.movieappuas.Network

import com.example.movieappuas.Database.MovieRoom
import com.example.movieappuas.Model.MovieAPI
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @GET("mEwLX/movie_table")
    fun getMovies(): Call<List<MovieAPI>>

    @POST("mEwLX/movie_table") // Ganti dengan endpoint yang sesuai
    fun addMovie(@Body movieApi: MovieAPI): Call<MovieAPI>

    @DELETE("mEwLX/movie_table/{id}")
    fun deleteMovie(@Path("id") id: String): Call<Void>

    @POST("mEwLX/movie_table/{id}")
    fun updateMovieToApi(@Path("id") id: String, @Body movie: MovieAPI): Call<Void>

    @GET("mEwLX/movie_table/{id}")
    fun getMovieById(@Path("id") id: String): Call<MovieAPI>



}