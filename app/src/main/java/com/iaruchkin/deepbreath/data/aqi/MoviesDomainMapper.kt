package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.SearchMovie

internal fun MovieEntity.toSearchMovie() = SearchMovie(
    id = this.id,
    title = this.title,
    thumbnail = this.thumbnail,
)

internal fun MovieEntity.toModel(): Movie = Movie(
    id = this.id,
    title = this.title,
    overview = this.overview,
    allowedAge = this.allowedAge,
    rating = this.rating,
    reviewsCounter = this.reviewsCounter,
    popularity = this.popularity,
    releaseDate = this.releaseDate,
    duration = this.duration,
    budget = this.budget,
    revenue = this.revenue,
    status = this.status,
    genres = this.genres,
    homepage = this.homepage,
    thumbnail = this.thumbnail
)