package ru.gaket.themoviedb.data.movies

import com.iaruchkin.deepbreath.data.aqi.local.MoviesLocalDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import ru.gaket.themoviedb.core.MovieUrlProvider
import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.remote.MoviesRemoteDataSource
import ru.gaket.themoviedb.data.review.local.MyReviewsLocalDataSource
import ru.gaket.themoviedb.data.review.local.toEntity
import ru.gaket.themoviedb.data.review.local.toModel
import ru.gaket.themoviedb.data.review.remote.ReviewsRemoteDataSource
import ru.gaket.themoviedb.domain.auth.User.Email
import ru.gaket.themoviedb.domain.auth.User.Id
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.movies.models.MovieWithReviews
import ru.gaket.themoviedb.domain.movies.models.SearchMovie
import ru.gaket.themoviedb.domain.movies.models.SearchMovieWithMyReview
import ru.gaket.themoviedb.domain.review.models.MyReview
import ru.gaket.themoviedb.domain.review.models.ReviewDraft
import ru.gaket.themoviedb.domain.review.models.SomeoneReview
import ru.gaket.themoviedb.util.VoidResult
import ru.gaket.themoviedb.util.doOnSuccess
import ru.gaket.themoviedb.util.mapNestedSuccess
import ru.gaket.themoviedb.util.mapSuccess
import ru.gaket.themoviedb.util.toSuccessOrErrorList
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository providing data about [MovieEntity]
 */
interface MoviesRepository {

    suspend fun searchMoviesWithReviews(query: String): Result<List<SearchMovieWithMyReview>, Throwable>

    suspend fun getMovieDetails(id: MovieId): Result<Movie, Throwable>

    fun observeMovieDetailsWithReviews(id: MovieId): Flow<Result<MovieWithReviews, Throwable>>

    suspend fun addReview(
        draft: ReviewDraft,
        authorId: Id,
        authorEmail: Email,
    ): VoidResult<Throwable>

    suspend fun getReviewsForUser(userId: Id)

    suspend fun deleteUserReviews()
}

private suspend fun MoviesRepository.getMovieDetailsList(ids: Set<MovieId>): Result<List<Movie>, List<Throwable>> =
    coroutineScope {
        val asyncCalls = ids.map { singleId ->
            async { getMovieDetails(singleId) }
        }

        asyncCalls.awaitAll()
            .toSuccessOrErrorList()
    }

class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val moviesLocalDataSource: MoviesLocalDataSource,
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource,
    private val myReviewsLocalDataSource: MyReviewsLocalDataSource,
    private val movieUrlProvider: MovieUrlProvider,
) : MoviesRepository {

    override suspend fun searchMoviesWithReviews(query: String): Result<List<SearchMovieWithMyReview>, Throwable> =
        searchMovies(query)
            .mapSuccess { movies -> fillSearchMoviesWithMyReviews(movies) }

    private suspend fun searchMovies(query: String): Result<List<SearchMovie>, Throwable> =
        moviesRemoteDataSource.searchMovies(query)
            .mapSuccess { dtos -> dtos.map { singleDto -> singleDto.toEntity(movieUrlProvider.baseImageUrl) } }
            .doOnSuccess { movieEntities -> moviesLocalDataSource.insertAll(movieEntities) }
            .mapSuccess { entries -> entries.map { movieEntity -> movieEntity.toSearchMovie() } }

    private suspend fun fillSearchMoviesWithMyReviews(movies: List<SearchMovie>): List<SearchMovieWithMyReview> {
        val movieIds = movies.map { singleMovie -> singleMovie.id }
        val myReviews = getMyReviewsForMovies(movieIds)

        return movies.map { singleMovie ->
            SearchMovieWithMyReview(
                movie = singleMovie,
                myReview = myReviews[singleMovie.id]
            )
        }
    }

    private suspend fun getMyReviewsForMovies(movieIds: List<MovieId>): Map<MovieId, MyReview> =
        myReviewsLocalDataSource.getByMovieIds(movieIds)
            .map { myReviewEntity -> myReviewEntity.toModel() }
            .associateBy { myReview -> myReview.movieId }

    override suspend fun getMovieDetails(id: MovieId): Result<Movie, Throwable> {
        val cachedMovie = moviesLocalDataSource.getById(id)
        val entityResult = if ((cachedMovie != null) && cachedMovie.isFullyLoaded) {
            Result.Success(cachedMovie)
        } else {
            loadMovieDetailsFromRemoteAndStore(id)
        }

        return entityResult.mapSuccess { entity -> entity.toModel() }
    }

    private suspend fun loadMovieDetailsFromRemoteAndStore(id: MovieId): Result<MovieEntity, Throwable> =
        moviesRemoteDataSource.getMovieDetails(id)
            .mapSuccess { dto -> dto.toEntity(movieUrlProvider.baseImageUrl) }
            .doOnSuccess { entity -> moviesLocalDataSource.insert(entity) }

    override fun observeMovieDetailsWithReviews(id: MovieId): Flow<Result<MovieWithReviews, Throwable>> =
        flow {
            val result = getMovieDetailsWithReviews(id)
            if (currentCoroutineContext().isActive) {
                emit(result)
            }
        }
            .flatMapLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val (movie, allReviews) = result.result

                        observeMyReviewForMovieAndMergeWith(id, movie, allReviews)
                            .map { value -> Result.Success(value) }
                    }
                    is Result.Error -> flowOf(result)
                }
            }

    private suspend fun getMovieDetailsWithReviews(
        id: MovieId,
    ): Result<Pair<Movie, List<SomeoneReview>>, Throwable> =
        coroutineScope {
            val allReviewsResultCall = async { reviewsRemoteDataSource.getMovieReviews(id) }
            val movieDetailsCall = async { getMovieDetails(id) }

            val allReviews = allReviewsResultCall.await()
            val movieDetails = movieDetailsCall.await()

            movieDetails.mapNestedSuccess { movie ->
                allReviews.mapSuccess { allReviews -> movie to allReviews }
            }
        }

    private fun observeMyReviewForMovieAndMergeWith(
        id: MovieId,
        movie: Movie,
        allReviews: List<SomeoneReview>,
    ): Flow<MovieWithReviews> =
        myReviewsLocalDataSource.observeReviews(id)
            .map { dbEntity -> dbEntity?.toModel() }
            .map { myReview ->
                MovieWithReviews(
                    movie = movie,
                    someoneElseReviews = allReviews.filter { someoneReview -> someoneReview.review.id != myReview?.review?.id },
                    myReview = myReview
                )
            }

    override suspend fun addReview(
        draft: ReviewDraft,
        authorId: Id,
        authorEmail: Email,
    ): VoidResult<Throwable> =
        reviewsRemoteDataSource.addReview(draft, authorId, authorEmail)
            .mapSuccess { newReview ->
                val entity = newReview.toEntity()
                myReviewsLocalDataSource.insert(entity)
            }

    override suspend fun getReviewsForUser(userId: Id) {
        when (val myReviewsResult = getAndStoreMyReviews(userId)) {
            is Result.Success -> {
                getAndStoreMyReviewedMovies(myReviewsResult.result)
            }
            is Result.Error -> {
                Timber.e("sync Local storage error", myReviewsResult.result)
            }
        }
    }

    private suspend fun getAndStoreMyReviews(userId: Id): Result<List<MyReview>, Throwable> =
        reviewsRemoteDataSource.getMyReviews(userId)
            .doOnSuccess { myReviews ->
                val entities = myReviews.map { model -> model.toEntity() }
                myReviewsLocalDataSource.insertAll(entities)
            }

    private suspend fun getAndStoreMyReviewedMovies(result: List<MyReview>) {
        val myReviewedMovieIds = result
            .map { review -> review.movieId }
            .toSet()

        getMovieDetailsList(ids = myReviewedMovieIds)
    }

    override suspend fun deleteUserReviews() = myReviewsLocalDataSource.deleteAll()
}
