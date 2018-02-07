package com.lony13.moviesfortoday

/**
 * Created by Lony13 on 2018-01-19.
 */
class Movie : Comparable<Movie>{
    var title:String? = null
    var channel:String? = null
    var hour:String? = null
    var description:String? = null
    var image:String? = null
    var link:String? = null
    var rating:String? = null
    var year:String? = null
    var genre:String? = null

    constructor(title: String?, channel: String?, hour: String?, description: String?,
                image: String?, link: String?, rating: String?, year: String?, genre: String?) {
        this.title = title
        this.channel = channel
        this.hour = hour
        this.description = description
        this.image = image
        this.link = link
        this.rating = rating
        this.year = year
        this.genre = genre
    }

    override fun compareTo(other: Movie): Int {
        val thisHour = this.hour!!.split(":")[0].toInt()
        val thisMinutes = this.hour!!.split(":")[1].toInt()
        val otherHour = other.hour!!.split(":")[0].toInt()
        val otherMinutes = other.hour!!.split(":")[1].toInt()

        return when {
            thisHour < otherHour -> -1
            thisHour > otherHour -> 1
            thisMinutes < otherMinutes -> -1
            else -> 1
        }
    }
}