package com.example.movieappuas.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieappuas.Model.MovieAPI
import com.example.movieappuas.Network.ApiClient
import com.example.movieappuas.databinding.ActivityUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var id: String? = null // Menyimpan ID film yang akan diupdate (sebagai String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getStringExtra("MOVIE_ID")


        // Ambil data film yang akan diupdate dari Intent
        id = intent.getStringExtra("MOVIE_ID") // ID bertipe String
        val title = intent.getStringExtra("MOVIE_TITLE")
        val duration = intent.getStringExtra("MOVIE_DURATION")
        val imageUrl = intent.getStringExtra("MOVIE_IMAGEURL")
        val genre = intent.getStringExtra("MOVIE_GENRE")
        val description = intent.getStringExtra("MOVIE_DESCRIPTION")

        // Set data ke EditText
        binding.titleMovie.setText(title)
        binding.durationMovie.setText(duration)
        binding.imageMovie.setText(imageUrl)
        binding.genreMovie.setText(genre)
        binding.descriptionMovie.setText(description)


        binding.backHome.setOnClickListener {
            startActivity(Intent(this@UpdateActivity, DetailActivityAdmin::class.java))
        }

        binding.submitAddMovie.setOnClickListener {
            val updatedTitle = binding.titleMovie.text.toString()
            val updatedDuration = binding.durationMovie.text.toString()
            val updatedImageUrl = binding.imageMovie.text.toString()
            val updatedGenre = binding.genreMovie.text.toString()
            val updatedDescription = binding.descriptionMovie.text.toString()

            // Validasi input (opsional)
            if (updatedTitle.isBlank() || updatedDuration.isBlank() || updatedGenre.isBlank()) {
                Toast.makeText(this, "Isi semua data film!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedMovie = MovieAPI(
                title = updatedTitle,
                duration = updatedDuration,
                imageUrl = updatedImageUrl,
                genre = updatedGenre,
                description = updatedDescription
            )

            // Panggil fungsi untuk update movie
            updateMovieToApi(updatedMovie)


            val intent = Intent(this@UpdateActivity, DetailActivityAdmin::class.java).apply {
                putExtra("MOVIE_ID", id)
                putExtra("MOVIE_TITLE", updatedTitle)
                putExtra("MOVIE_GENRE", updatedGenre)
                putExtra("MOVIE_DURATION", updatedDuration)
                putExtra("MOVIE_IMAGEURL", updatedImageUrl)
                putExtra("MOVIE_DESCRIPTION", updatedDescription)
            }

            startActivity(intent)
            finish()

        }
    }

    private fun updateMovieToApi(movieAPI: MovieAPI) {
        // Pastikan id tidak null
        val id = id ?: return

        // Mengupdate film ke API
        val client = ApiClient.instance.updateMovieToApi(id, movieAPI)

        client.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateActivity, "Movie berhasil diperbarui", Toast.LENGTH_SHORT).show()

                } else {
                    Log.e("Response", "Error: ${response.code()} - ${response.message()}")
                    Log.d("Response", "Gagal Memperbarui ID: $id")
                    Toast.makeText(this@UpdateActivity, "Gagal Memperbarui movie: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
