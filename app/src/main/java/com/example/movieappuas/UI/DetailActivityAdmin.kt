package com.example.movieappuas.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movieappuas.Model.MovieAPI
import com.example.movieappuas.Network.ApiClient
import com.example.movieappuas.databinding.ActivityMovieDetailAdminBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getStringExtra("MOVIE_ID")
        val title = intent.getStringExtra("MOVIE_TITLE")
        val genre = intent.getStringExtra("MOVIE_GENRE")
        val duration = intent.getStringExtra("MOVIE_DURATION")
        val imageUrl = intent.getStringExtra("MOVIE_IMAGEURL")
        val description = intent.getStringExtra("MOVIE_DESCRIPTION")

        if (movieId != null) {
            fetchMovieDetails(movieId)
        }

        with(binding) {
            btnUpdateMovie.setOnClickListener {
                val intent = Intent(this@DetailActivityAdmin, UpdateActivity::class.java).apply {
                    putExtra("MOVIE_ID", movieId)
                    putExtra("MOVIE_TITLE", title)
                    putExtra("MOVIE_DURATION", duration)
                    putExtra("MOVIE_IMAGEURL", imageUrl)
                    putExtra("MOVIE_DESCRIPTION", description)
                    putExtra("MOVIE_GENRE", genre)
                }
                startActivity(intent)
            }

            backHome.setOnClickListener {
                startActivity(Intent(this@DetailActivityAdmin, MainActivity::class.java))
            }
        }
    }


    private fun fetchMovieDetails(movieId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.getMovieById(movieId).execute()
                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        withContext(Dispatchers.Main) {
                            updateUI(movie)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Log error
            }
        }
    }

    private fun updateUI(movie: MovieAPI) {
        with(binding) {
            titleMovie.text = movie.title
            genreMovie.text = movie.genre
            durationMovie.text = movie.duration
            descriptionMovie.text = movie.description

            Glide.with(this@DetailActivityAdmin)
                .load(movie.imageUrl)
                .into(imageMovie)
        }
    }


    private fun setupListeners(movieId: String?) {
        with(binding) {
            // Tombol Update
            btnUpdateMovie.setOnClickListener {
                val intent = Intent(this@DetailActivityAdmin, UpdateActivity::class.java).apply {
                    putExtra("MOVIE_ID", movieId)
                }
                startActivity(intent)
            }

            // Tombol Kembali ke Home
            backHome.setOnClickListener {
                startActivity(Intent(this@DetailActivityAdmin, MainActivity::class.java))
            }
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
