package com.example.movieappuas.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movieappuas.Database.MovieDao
import com.example.movieappuas.Database.MovieRoom
import com.example.movieappuas.Database.MovieRoomDatabase
import com.example.movieappuas.databinding.ActivityFavoriteBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = MovieRoomDatabase.getDatabase(this)
        movieDao = db!!.movieDao()


        with(binding) {

            homeActivity.setOnClickListener {
                startActivity(Intent(this@FavoriteActivity, MainActivity::class.java))
            }

        }

    }

    override fun onResume() {
        super.onResume()
        getAllMovies()
    }

    private fun getAllMovies (){
        movieDao.allMovieRoom.observe(this) { movieList ->
            val adapter = FavoriteMovieAdapter(this, movieList.toMutableList())
            binding.listMovie.adapter = adapter
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
        }
    }



}