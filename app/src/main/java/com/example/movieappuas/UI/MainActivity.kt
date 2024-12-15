package com.example.movieappuas.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.movieappuas.Database.MovieDao
import com.example.movieappuas.Database.MovieRoomDatabase
import com.example.movieappuas.Model.MovieAPI
import com.example.movieappuas.Network.ApiClient
import com.example.movieappuas.databinding.ActivityMainBinding
import com.example.movieappuas.databinding.ActivityMainUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingUser: ActivityMainUserBinding
    private lateinit var movieDao: MovieDao
    private lateinit var horizontalAdapter: MovieAdapter
    private lateinit var verticalAdapter: MovieAdapter
    private val movieAPIList = mutableListOf<MovieAPI>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = MovieRoomDatabase.getDatabase(this)
        movieDao = db!!.movieDao()!!
        prefManager = PrefManager.getInstance(this)


        val user = intent.getStringExtra("USER")

        if (user != null) {
            prefManager.saveUsername(user)
        }

        // Cek peran pengguna (admin atau user)
        val userName = prefManager.getUsername() // Tambahkan metode ini ke PrefManager

        if (userName == "admin") {
            Toast.makeText(this, "Anda Login Sebagai Admin", Toast.LENGTH_SHORT ).show()
            setupAdminView()
        } else {
            Toast.makeText(this, "Anda Login Sebagai Pengguna", Toast.LENGTH_SHORT ).show()
            setupUserView()
        }
    }

    private fun setupAdminView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager.setLoggedIn(true)
        prefManager.saveUsername("admin")
        prefManager.saveUserRole("admin") // Atau "user" untuk pengguna biasa


        checkLoginStatus()

        // Setup RecyclerView
        horizontalAdapter = MovieAdapter(this, movieAPIList, MovieAdapter.TYPE_HORIZONTAL, prefManager) { movie ->
            // Callback untuk menghapus movie ketika ditekan lama
            showDeleteConfirmationDialog(movie)
        }

        verticalAdapter = MovieAdapter(this, movieAPIList, MovieAdapter.TYPE_VERTICAL, prefManager) { movie ->
            // Callback untuk menghapus movie ketika ditekan lama
            showDeleteConfirmationDialog(movie)
        }

        binding.listMovie.adapter = verticalAdapter
        binding.listMovieHorizontally.adapter = horizontalAdapter

        // Load movies
        fetchMoviesFromApi()

        // Admin-only buttons
        binding.btnLogout.setOnClickListener {
            prefManager.setLoggedIn(false)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        binding.addNewMovie.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateActivity::class.java)
            startActivity(intent)
        }

        // Pergi ke favorit
        binding.favoriteActivity.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
        }
    }

    private fun setupUserView() {
        bindingUser = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(bindingUser.root)

        checkLoginStatus()

        // Setup RecyclerView
        horizontalAdapter = MovieAdapter(this, movieAPIList, MovieAdapter.TYPE_HORIZONTAL, prefManager,null)
        verticalAdapter = MovieAdapter(this, movieAPIList, MovieAdapter.TYPE_VERTICAL, prefManager,null)

        with(bindingUser) {
            // inisiasi list
            listMovie.adapter = verticalAdapter
            listMovieHorizontally.adapter = horizontalAdapter

            // Load movies
            fetchMoviesFromApi()

            // logout
            btnLogout.setOnClickListener{
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            // Menambahkan movie ke list favorit
            favoriteActivity.setOnClickListener {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            }

        }

    }

    fun fetchMoviesFromApi() {
        val client = ApiClient.instance
        val response = client.getMovies()

        response.enqueue(object : Callback<List<MovieAPI>> {
            override fun onResponse(call: Call<List<MovieAPI>>, response: Response<List<MovieAPI>>) {
                if (response.isSuccessful) {
                    val rawJson = response.body().toString()
//                    Log.d("RAW_JSON", rawJson ?: "JSON Kosong")
                    response.body()?.let { movies ->
//                        for (movie in movies) {
//                            Log.d("FETCH_MOVIE", "Movie ID: ${movie.id}, Title: ${movie.title}, ${movie.genre}")
//                        }
                        verticalAdapter.updateMovies(movies)
                        horizontalAdapter.updateMovies(movies)
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                    Log.e("FETCH_MOVIE", "Failed to fetch movies: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<MovieAPI>>, t: Throwable) {
                Log.e("FETCH_MOVIE", "Error: ${t.message}")
            }
        })
    }

    private fun showDeleteConfirmationDialog(movieAPI: MovieAPI) {
        Log.d("DELETE_MOVIE", "Menghapus movie dengan ID: ${movieAPI.id}")
        AlertDialog.Builder(this).apply {
            setTitle("Hapus Movie")
            setMessage("Apakah Anda yakin ingin menghapus movie ini?")
            setPositiveButton("Ya") { _, _ ->
                deleteMovie(movieAPI)
            }
            setNegativeButton("Tidak", null)
            show()
        }
    }

    private fun deleteMovie(movieAPI: MovieAPI) {
        val client = movieAPI.id?.let { ApiClient.instance.deleteMovie(it) }

        if (client != null) {
            client.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Movie berhasil dihapus", Toast.LENGTH_SHORT).show()
                        movieAPIList.remove(movieAPI)
                        verticalAdapter.updateMovies(movieAPIList)
                        horizontalAdapter.updateMovies(movieAPIList)
                    } else {
                        Log.e("DELETE_MOVIE", "Error: ${response.code()} - ${response.message()}")
                        Toast.makeText(this@MainActivity, "Gagal menghapus movie: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
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

