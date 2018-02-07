package com.lony13.moviesfortoday

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*

class MainActivity : AppCompatActivity() {

    private var listOfMovies = ArrayList<Movie>()
    private var adapter:MoviesAdapter? = null
    private val TIME_SIZE = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMovies()
    }

    private fun loadMovies() {
        val url = "http://www.filmweb.pl/program-tv"
        val result = MovieAsyncTask().execute(url)
        val movies:ArrayList<Movie> = result.get()

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1
        val currentMinutes = Calendar.getInstance().get(Calendar.MINUTE)

        (0 until movies.size)
                .filter { compareTime(movies[it].hour, currentHour, currentMinutes) }
                .forEach { listOfMovies.add(movies[it]) }

        listOfMovies.sort()

        adapter = MoviesAdapter(this, listOfMovies)
        tvListMovies.adapter = adapter
    }

    private fun compareTime(movieTime: String?, currentHour: Int, currentMinutes: Int): Boolean {
        val movieHour: Long?
        val movieMinutes:Long?

        if(movieTime!!.length == TIME_SIZE){
            movieHour = movieTime.substring(0,1).toLong()
            movieMinutes = movieTime.substring(2).toLong()
        } else{
            movieHour = movieTime.substring(0,2).toLong()
            movieMinutes = movieTime.substring(3).toLong()
        }

        if(movieHour >= currentHour)
            return true
        else if(movieHour == (currentHour - 1L) && movieMinutes >= currentMinutes)
            return true
        return false
    }

    @SuppressLint("StaticFieldLeak")
    inner class MovieAsyncTask: AsyncTask<String, String, ArrayList<Movie>>() {

        override fun onPreExecute() {
            //Before task started
        }

        override fun doInBackground(vararg p0: String?): ArrayList<Movie> {
            try {
                val prototypeElements = getPrototypeElements()
                val resultList = arrayListOf<Movie>()

                for( index in 0 until prototypeElements!!.size) {
                    val title = getTitle(prototypeElements, index)
                    val image = getImage(prototypeElements, index)
                    val channel = getChannel(prototypeElements, index)
                    val hour = getHour(prototypeElements, index)
                    val link = getLink(prototypeElements, index)

                    val connDesc = getConnectionMovieDesc(link)
                    val description = getMovieDescription(connDesc)
                    val rating = getRating(connDesc)
                    val year = getYear(connDesc)
                    val genre = getGenre(connDesc)

                    resultList.add(Movie(title, channel, hour, description, image, link, rating,
                            year, genre))
                }
                return resultList
            } catch (e:Exception){}
            return ArrayList()
        }

        override fun onProgressUpdate(vararg values: String?) {
        }

        override fun onPostExecute(result: ArrayList<Movie>?) {
            //After task done
        }

        private fun getPrototypeElements(): Elements? {
            val conn = Jsoup.connect("http://www.filmweb.pl/program-tv")
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get()

            return conn.body().getElementsByClass("element hasRibbon")
        }

        private fun getConnectionMovieDesc(link: String?): Document {
            return Jsoup.connect("http://www.filmweb.pl" + link)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; " +
                            "rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get()
        }

        private fun getTitle(prototypeElements: Elements?, index: Int): String?{
            val title = prototypeElements!![index]
                    .getElementsByClass("maxlines-2 s-16 name top-5")[0]
                    .childNode(0)
                    .toString()
            return title.substring(1, title.length - 1)
        }

        private fun getImage(prototypeElements: Elements?, index: Int): String? {
            return prototypeElements!![index]
                    .getElementsByTag("img")[0]
                    .attr("src")
        }

        private fun getChannel(prototypeElements: Elements?, index: Int): String? {
            return prototypeElements!![index]
                    .getElementsByClass("cap")[1]
                    .childNode(0)
                    .toString()
        }

        private fun getHour(prototypeElements: Elements?, index: Int): String? {
            val hour = prototypeElements!![index]
                    .getElementsByClass("top-5 maxlines-2 cap")[0]
                    .childNode(0)
                    .toString()
            return hour.substring(1, hour.length - 2)
        }

        private fun getLink(prototypeElements: Elements?, index: Int): String? {
            return prototypeElements!![index]
                    .getElementsByClass("maxlines-2 s-16 name top-5")[0]
                    .attr("href")
                    .toString()
        }

        private fun getMovieDescription(connDesc: Document): String? {
            val prototypeElementsDesc = connDesc.body()
                    .getElementsByClass("filmPlot bottom-15")

            var description:String? = ""
            if(prototypeElementsDesc.size != 0) {
                description = prototypeElementsDesc[0]
                        .getElementsByClass("text")[0]
                        .childNode(0)
                        .toString()
                        .replace("&nbsp", " ")
            }
            return description
        }

        private fun getRating(connDesc: Document): String? {
            return connDesc.body()
                    .getElementsByClass("filmRateBox")
                    .toString()
                    .split("ratingValue")[1]
                    .substring(3,6)
        }

        private fun getYear(connDesc: Document): String? {
            return connDesc.body()
                    .getElementsByClass("halfSize")[0]
                    .childNode(0)
                    .toString()
                    .substring(1, 5)
        }

        private fun getGenre(connDesc: Document): String? {
            return connDesc.body()
                    .getElementsByClass("inline sep-comma genresList")[0]
                    .toString()
                    .split("\">")[2]
                    .split("<")[0]
        }
    }
}
