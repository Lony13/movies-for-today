package com.lony13.moviesfortoday

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_movie_details.*
import java.net.URL

class MovieDetails : AppCompatActivity() {

    private val filmwebLink = "http://www.filmweb.pl"
    private val MAX_TITLE_SIZE = 18
    private val FONT_SIZE = 30F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val bundle:Bundle = intent.extras

        val title = bundle.getString("title")
        val channel = bundle.getString("channel")
        val hour = bundle.getString("hour")
        val description = bundle.getString("description")
        val image = bundle.getString("image")
        val link = bundle.getString("link")
        var rating = bundle.getString("rating")
        var year = bundle.getString("year")
        var genre = bundle.getString("genre")

        rating = "Ocena: " + rating
        year = "Rok: " + year
        genre = "Gatunek: " + genre

        setTitleTextSize(title)
        tvTitleDetails.text = title
        tvChannelDetails.text = channel
        tvHourDetails.text = hour
        tvDescriptionDetails.text = description
        tvRating.text = rating
        tvYear.text = year
        tvGenre.text = genre

        val bitmap = ImageToBitmapAsyncTask().execute(image).get()
        ivMovieImageDetails.setImageBitmap(bitmap)

        onClickListeners(link)
    }

    private fun onClickListeners(link: String) {
        tvTitleDetails.setOnClickListener {
            launchBrowser(link)
        }

        ivMovieImageDetails.setOnClickListener{
            launchBrowser(link)
        }
    }

    private fun launchBrowser(link: String) {
        val url = filmwebLink + link
        val launchBrowser = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
        startActivity(launchBrowser)
    }

    private fun setTitleTextSize(title: String?) {
        if(title!!.length > MAX_TITLE_SIZE)
            tvTitleDetails.textSize = FONT_SIZE
    }

    @SuppressLint("StaticFieldLeak")
    inner class ImageToBitmapAsyncTask: AsyncTask<String, String, Bitmap>() {

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg image: String?): Bitmap? {
            return BitmapFactory.decodeStream(URL(image[0]).openConnection().getInputStream())
        }

        override fun onProgressUpdate(vararg values: String?) {

        }

        override fun onPostExecute(result: Bitmap?) {

        }
    }
}
