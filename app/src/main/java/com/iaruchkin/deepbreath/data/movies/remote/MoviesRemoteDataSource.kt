package ru.gaket.themoviedb.data.movies.remote

import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.util.doOnError
import ru.gaket.themoviedb.util.runOperationCatching
import timber.log.Timber
import javax.inject.Inject

interface MoviesRemoteDataSource {

    suspend fun searchMovies(query: String): Result<List<SearchMovieDto>, Throwable>

    suspend fun getMovieDetails(id: MovieId): Result<DetailsMovieDto, Throwable>
}

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi,
) : MoviesRemoteDataSource {

    override suspend fun searchMovies(query: String): Result<List<SearchMovieDto>, Throwable> =
        runOperationCatching { moviesApi.searchMovie(query).searchMovies }
            .doOnError { error -> Timber.e("Search movies from server error", error) }

    override suspend fun getMovieDetails(id: MovieId): Result<DetailsMovieDto, Throwable> =
        runOperationCatching { moviesApi.getMovieDetails(id) }
            .doOnError { error -> Timber.e("getMovieDetails from server error", error) }
}
