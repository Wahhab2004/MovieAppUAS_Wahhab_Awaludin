package com.example.movieappuas.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movieappuas.Database.MovieDao
import com.example.movieappuas.Database.MovieRoom
import com.example.movieappuas.Database.MovieRoomDatabase
import com.example.movieappuas.databinding.ActivityMovieDetailBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivityUser : AppCompatActivity() {

    private lateinit var binding : ActivityMovieDetailBinding
    private lateinit var movieDao: MovieDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        executorService = Executors.newSingleThreadExecutor()
        val db = MovieRoomDatabase.getDatabase(this)
        movieDao = db!!.movieDao()

        val id = intent.getStringExtra("MOVIE_ID")
        val title = intent.getStringExtra("MOVIE_TITLE")
        val genre = intent.getStringExtra("MOVIE_GENRE")
        val duration = intent.getStringExtra("MOVIE_DURATION")
        val imageUrl = intent.getStringExtra("MOVIE_IMAGEURL")
        val description = intent.getStringExtra("MOVIE_DESCRIPTION")

        binding.titleMovie.text = title
        binding.genreMovie.text = genre
        binding.durationMovie.text = "${duration}h"
        binding.descriptionMovie.text = description

        Glide.with(this)
            .load(imageUrl)
            .into(binding.imageMovie)


        with(binding) {
            addMovietoFavorite.setOnClickListener(View.OnClickListener {

                if (id != null && title != null && duration != null && imageUrl != null && genre != null && description != null ) {
                    Toast.makeText(this@DetailActivityUser, "Berhasil menambahkan movie", Toast.LENGTH_SHORT).show()
                    Log.d("NAMBAH MOVIE", " Berhasil tambah movie, dengan id: ${id}")

                    insert(
                        MovieRoom(
                            id = id,
                            title = title,
                            duration = duration,
                            imageUrl = imageUrl,
                            genre = genre,
                            description = description
                        )
                    )
                }
            })


            backHome.setOnClickListener {
                startActivity(Intent(this@DetailActivityUser, MainActivity::class.java))
            }

        }

    }

    private fun insert(movie: MovieRoom) {
        executorService.execute { movieDao.insert(movie) }
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