package com.lony13.moviesfortoday

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie.view.*

/**
 * Created by Lony13 on 2018-01-19.
 */
class MoviesAdapter: BaseAdapter {
    private var listOfMovies = ArrayList<Movie>()
    var context: Context? = null

    constructor(context: Context, listOfMovies: ArrayList<Movie>):super(){
        this.listOfMovies = listOfMovies
        this.context = context
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        val movie = listOfMovies[index]
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val movieView = inflater.inflate(R.layout.movie, null)

        movieView.tvTitle.text = movie.title!!
        movieView.tvDescription.text = movie.channel!!
        movieView.tvHour.text = movie.hour!!

        Glide.with(context).load(movie.image).into(movieView.ivMovieImage)

        movieView.tvMovieLayout.setOnClickListener{
            val intent = Intent(context, MovieDetails::class.java)
            intent.putExtra("title", movie.title)
            intent.putExtra("channel", movie.channel)
            intent.putExtra("hour", movie.hour)
            intent.putExtra("image", movie.image!!)
            intent.putExtra("link", movie.link)
            intent.putExtra("description", movie.description)
            intent.putExtra("rating", movie.rating)
            intent.putExtra("year", movie.year)
            intent.putExtra("genre", movie.genre)
            context!!.startActivity(intent)
        }

        return movieView
    }

    override fun getItem(index: Int): Any {
        return listOfMovies[index]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return listOfMovies.size
    }

}