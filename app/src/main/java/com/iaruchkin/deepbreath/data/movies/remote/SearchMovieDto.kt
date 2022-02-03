package ru.gaket.themoviedb.data.movies.remote

import com.google.gson.annotations.SerializedName

/**
 * Class of Search movie result coming from the api
 */
data class SearchMovieDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("poster_path")
    val posterPath: String?,
)
