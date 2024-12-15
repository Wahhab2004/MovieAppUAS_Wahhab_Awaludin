package com.example.movieappuas.UI

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappuas.Database.MovieRoom
import com.example.movieappuas.databinding.ItemMovieFavoriteBinding

class FavoriteMovieAdapter(
    private val context: Context,
    private val movieRoom: MutableList<MovieRoom>) : RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMovieFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieRoom[position]
        with(holder.binding) {
            Glide.with(root.context)
                .load(movie.imageUrl) // URL gambar
                .fitCenter()
                .into(imageMovie)

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

    override fun getItemCount(): Int = movieRoom.size

    }

