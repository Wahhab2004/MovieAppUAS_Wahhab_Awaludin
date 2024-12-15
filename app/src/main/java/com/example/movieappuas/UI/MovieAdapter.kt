package com.example.movieappuas.UI

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappuas.Model.MovieAPI
import com.example.movieappuas.databinding.ItemMovieBinding
import com.example.movieappuas.databinding.MovieHorizontallyBinding

class MovieAdapter(
    private val context: Context,
    private val movieAPI: MutableList<MovieAPI>,
    private val type: Int,
    private val prefManager: PrefManager,
    private val onDeleteMovie: ((MovieAPI) -> Unit)?


) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Konstanta untuk jenis tampilan
    companion object {
        const val TYPE_VERTICAL = 0
        const val TYPE_HORIZONTAL = 1
    }

    // ViewHolder untuk tampilan vertikal & Horizontal
    inner class VerticalMovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
    inner class HorizontalMovieViewHolder(val binding: MovieHorizontallyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VERTICAL -> {
                val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VerticalMovieViewHolder(binding)
            }
            TYPE_HORIZONTAL -> {
                val binding = MovieHorizontallyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HorizontalMovieViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movieAPI[position]
        val user = prefManager.getUsername()

        when (holder) {
            is VerticalMovieViewHolder -> {
                // Bind data untuk tampilan vertikal
                with(holder.binding) {
                    titleMovie.text = movie.title
                    Glide.with(root.context)
                        .load(movie.imageUrl) // URL gambar
                        .fitCenter()
                        .into(imageMovie)


                    if (user == "admin") {
                        listMovie.setOnClickListener {
                            val intent = Intent(context, DetailActivityAdmin::class.java)
                            intent.putExtra("MOVIE_ID", movie.id)
                            intent.putExtra("MOVIE_TITLE", movie.title)
                            intent.putExtra("MOVIE_DURATION", movie.duration)
                            intent.putExtra("MOVIE_IMAGEURL", movie.imageUrl)
                            intent.putExtra("MOVIE_DESCRIPTION", movie.description)
                            intent.putExtra("MOVIE_GENRE", movie.genre)
                            context.startActivity(intent)
                        }

                        listMovie.setOnLongClickListener {
                            onDeleteMovie?.let { it1 -> it1(movie) } // Panggil callback hapus
                            true
                        }
                    }

                    else{
                        listMovie.setOnClickListener {
                            val intent = Intent(context, DetailActivityUser::class.java)
                            intent.putExtra("MOVIE_ID", movie.id)
                            intent.putExtra("MOVIE_TITLE", movie.title)
                            intent.putExtra("MOVIE_DURATION", movie.duration)
                            intent.putExtra("MOVIE_IMAGEURL", movie.imageUrl)
                            intent.putExtra("MOVIE_DESCRIPTION", movie.description)
                            intent.putExtra("MOVIE_GENRE", movie.genre)
                            context.startActivity(intent)
                        }
                    }




                }
            }
            is HorizontalMovieViewHolder -> {
                // Bind data untuk tampilan horizontal
                with(holder.binding) {
                    titleMovie.text = movie.title
                    genreMovie.text = movie.genre
                    durationMovie.text = movie.duration

                    Glide.with(root.context)
                        .load(movie.imageUrl) // URL gambar
                        .fitCenter()
                        .into(imageView)

                }
            }
        }
    }

    override fun getItemCount(): Int = movieAPI.size

    override fun getItemViewType(position: Int): Int {
        return type
    }

    fun updateMovies(newMovieAPI: List<MovieAPI>) {
        movieAPI.clear()
        movieAPI.addAll(newMovieAPI)
        notifyDataSetChanged()
    }
}
