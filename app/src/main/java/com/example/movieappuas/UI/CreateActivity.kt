package com.example.movieappuas.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieappuas.Database.MovieRoom
import com.example.movieappuas.Model.MovieAPI
import com.example.movieappuas.Network.ApiClient
import com.example.movieappuas.databinding.ActivityCreateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            submitAddMovie.setOnClickListener {
                val title = binding.titleMovie.text.toString()
                val duration = binding.durationMovie.text.toString()
                val imageUrl = binding.imageMovie.text.toString()
                val genre = binding.genreMovie.text.toString()
                val description = binding.descriptionMovie.text.toString()

                // Validasi input (opsional)
                if (title.isBlank() || duration.isBlank() || genre.isBlank()) {
                    Toast.makeText(this@CreateActivity, "Isi semua data film!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val newMovieRoom = MovieAPI(
                    title = title,
                    duration = duration,
                    imageUrl = imageUrl,
                    genre = genre,
                    description = description
                )

                addMovieToApi(newMovieRoom)

            }

            backHome.setOnClickListener {
                startActivity(Intent(this@CreateActivity, MainActivity::class.java))
            }
        }
    }

    private fun addMovieToApi(movieApi: MovieAPI) {
        val client = ApiClient.instance.addMovie(movieApi)

        client.enqueue(object : Callback<MovieAPI> {
            override fun onResponse(call: Call<MovieAPI>, response: Response<MovieAPI>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CreateActivity,
                        "Film berhasil ditambahkan!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@CreateActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@CreateActivity,
                        "Gagal menambahkan film: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MovieAPI>, t: Throwable) {
                Toast.makeText(
                    this@CreateActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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
