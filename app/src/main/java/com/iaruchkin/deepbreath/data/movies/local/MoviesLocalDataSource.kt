package ru.gaket.themoviedb.data.movies.local

import ru.gaket.themoviedb.domain.movies.models.MovieId
import javax.inject.Inject

interface MoviesLocalDataSource {

    suspend fun getById(id: MovieId): MovieEntity?
    suspend fun insertAll(searchedMovies: List<MovieEntity>)
    suspend fun insert(movie: MovieEntity)
    suspend fun deleteAll()
}

class MoviesLocalDataSourceImpl @Inject constructor(
    private val moviesDao: MoviesDao,
) : MoviesLocalDataSource {

    override suspend fun getById(id: MovieId): MovieEntity? = moviesDao.getById(id)

    override suspend fun insertAll(searchedMovies: List<MovieEntity>) {
        moviesDao.insertAll(searchedMovies)
    }

    override suspend fun insert(movie: MovieEntity) {
        moviesDao.insert(movie)
    }

    override suspend fun deleteAll() {
        moviesDao.deleteAll()
    }
}