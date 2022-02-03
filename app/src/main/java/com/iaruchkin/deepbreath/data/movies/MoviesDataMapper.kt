package ru.gaket.themoviedb.data.movies

import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.remote.DetailsMovieDto
import ru.gaket.themoviedb.data.movies.remote.GenreDto
import ru.gaket.themoviedb.data.movies.remote.SearchMovieDto
import java.util.*

internal fun SearchMovieDto.toEntity(baseImageUrl: String): MovieEntity = MovieEntity(
    id = this.id,
    title = this.title,
    thumbnail = getPosterUrl(baseImageUrl, this.posterPath),
    imdbId = "",
    overview = "",
    allowedAge = "",
    rating = 0,
    reviewsCounter = 0,
    popularity = 0.00f,
    releaseDate = "",
    duration = 0,
    budget = 0,
    revenue = 0,
    status = "Released",
    genres = "",
    homepage = ""
)

/**
 * check [SearchMovieDto.toEntity]
 * **/
internal val MovieEntity.isFullyLoaded: Boolean
    get() = (this.duration > 0)

internal fun DetailsMovieDto.toEntity(baseImageUrl: String): MovieEntity = MovieEntity(
    id = this.id,
    imdbId = this.imdbId,
    title = this.title,
    overview = this.overview,
    allowedAge = normalizedAllowedAge(this.isAdult),
    rating = normalizedRating(this.rating),
    reviewsCounter = this.reviewsCounter,
    popularity = normalizedPopularity(this.popularity),
    releaseDate = this.releaseDate,
    duration = this.duration,
    budget = this.budget,
    revenue = this.revenue,
    status = this.status,
    genres = genresAsString(this.genres),
    homepage = this.homepage.orEmpty(),
    thumbnail = getPosterUrl(baseImageUrl, this.posterPath)
)

private fun getPosterUrl(baseImageUrl: String, posterPath: String?): String =
    "${baseImageUrl}${posterPath}"

private fun normalizedAllowedAge(isAdult: Boolean): String = if (isAdult) {
    AGE_ADULT
} else {
    AGE_CHILD
}

private fun normalizedRating(rating: Float): Int {
    return (rating / 2).toInt()
}

private fun normalizedPopularity(popularity: Float): Float {
    return "%.2f".format(Locale.US, popularity).toFloat()
}

private fun genresAsString(genres: List<GenreDto>): String =
    genres.joinToString(transform = GenreDto::name)

private const val AGE_ADULT = "18+"
private const val AGE_CHILD = "13+"