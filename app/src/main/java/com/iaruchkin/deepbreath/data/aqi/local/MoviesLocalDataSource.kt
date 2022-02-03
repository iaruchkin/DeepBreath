package com.iaruchkin.deepbreath.data.aqi.local

import ru.gaket.themoviedb.data.movies.local.MoviesDao
import javax.inject.Inject

interface AqiLocalDataSource {

//    suspend fun getById(id: MovieId): MovieEntity?
//    suspend fun insertAll(searchedMovies: List<MovieEntity>)
//    suspend fun insert(movie: MovieEntity)
//    suspend fun deleteAll()
}

class AqiLocalDataSourceImpl @Inject constructor(
    private val moviesDao: MoviesDao,
) : AqiLocalDataSource {

//    override suspend fun getById(id: MovieId): MovieEntity? = moviesDao.getById(id)
//
//    override suspend fun insertAll(searchedMovies: List<MovieEntity>) {
//        moviesDao.insertAll(searchedMovies)
//    }
//
//    override suspend fun insert(movie: MovieEntity) {
//        moviesDao.insert(movie)
//    }
//
//    override suspend fun deleteAll() {
//        moviesDao.deleteAll()
//    }

}