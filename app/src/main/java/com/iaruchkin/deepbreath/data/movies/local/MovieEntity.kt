package ru.gaket.themoviedb.data.movies.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gaket.themoviedb.domain.movies.models.MovieId

/**
 * DB class of detailed Movie stored in room
 */
@Entity(tableName = MovieEntity.TABLE_NAME)
data class MovieEntity(

    @PrimaryKey
    @ColumnInfo(name = MOVIE_ID)
    val id: MovieId,

    @ColumnInfo(name = IMDB_ID)
    val imdbId: String?,

    @ColumnInfo(name = TITLE)
    val title: String,

    @ColumnInfo(name = OVERVIEW)
    val overview: String,

    @ColumnInfo(name = ALLOWED_AGE)
    val allowedAge: String,

    @ColumnInfo(name = RATING)
    val rating: Int,

    @ColumnInfo(name = REVIEWS_COUNTER)
    val reviewsCounter: Int,

    @ColumnInfo(name = POPULARITY)
    val popularity: Float,

    @ColumnInfo(name = RELEASE_DATE)
    val releaseDate: String,

    @ColumnInfo(name = DURATION)
    val duration: Int,

    @ColumnInfo(name = BUDGET)
    val budget: Int,

    @ColumnInfo(name = REVENUE)
    val revenue: Int,

    @ColumnInfo(name = STATUS)
    val status: String,

    @ColumnInfo(name = GENRES)
    val genres: String,

    @ColumnInfo(name = HOMEPAGE)
    val homepage: String,

    @ColumnInfo(name = THUMBNAIL)
    val thumbnail: String,
) {

    companion object {

        const val TABLE_NAME = "movies"

        const val MOVIE_ID = "movie_id"
        const val IMDB_ID = "imdb_id"
        const val TITLE = "title"
        const val ALLOWED_AGE = "allowed_age"
        const val OVERVIEW = "overview"
        const val RATING = "rating"
        const val REVIEWS_COUNTER = "reviews_counter"
        const val POPULARITY = "popularity"
        const val RELEASE_DATE = "release_date"
        const val DURATION = "duration"
        const val BUDGET = "budget"
        const val REVENUE = "revenue"
        const val STATUS = "status"
        const val GENRES = "genres"
        const val HOMEPAGE = "homepage"
        const val THUMBNAIL = "thumbnail"
    }
}