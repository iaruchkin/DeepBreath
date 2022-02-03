package ru.gaket.themoviedb.data.movies.local

import androidx.room.*
import ru.gaket.themoviedb.domain.movies.models.MovieId

@Dao
interface MoviesDao {

    @Query("""
        SELECT *
        FROM ${MovieEntity.TABLE_NAME}
        WHERE ${MovieEntity.MOVIE_ID}=:id
        LIMIT 1
        """)
    suspend fun getById(id: MovieId): MovieEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(searchedMovies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Transaction
    @Query("DELETE FROM ${MovieEntity.TABLE_NAME}")
    suspend fun deleteAll()
}