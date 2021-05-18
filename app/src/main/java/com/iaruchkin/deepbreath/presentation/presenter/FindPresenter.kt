package com.iaruchkin.deepbreath.presentation.presenter

import android.util.Log
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.common.BasePresenter
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.network.dtos.CityList
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.network.parsers.FindCityApi
import com.iaruchkin.deepbreath.presentation.view.FindView
import com.iaruchkin.deepbreath.room.AppDatabase
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState

@InjectViewState
class FindPresenter : BasePresenter<FindView?>() {
    private val PRESENTER_FIND_TAG = "[find - presenter]"
    private val context = App.INSTANCE.applicationContext
    private var mCityList: List<Data?>? = null
    private var mFavoritesList: List<FavoritesEntity?>? = null


    override fun onFirstViewAttach() {
        loadData()
    }

    fun loadCityList(text: String) {
        loadCityFromNet(text)
    }

    fun update() {
        loadData()
    }

    fun removeItem(id: String) {
        removeFromFavorites(id)
    }


    private fun loadData() {
        loadFavoritesFromDb()
    }

    /**network
     *
     * @param parameter
     */
    private fun loadCityFromNet(parameter: String) {
        Log.i(PRESENTER_FIND_TAG, "Load City from net presenter")
        val disposable = FindCityApi.getInstance()
                .findCityEndpoint()
                .get(parameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: CityList ->
                    mCityList = response.cityList
                    updateCityList()
                })
                { th: Throwable -> handleError(th) }

        disposeOnDestroy(disposable)
    }

    /**setting data objects
     *
     */
    private fun updateCityList() {
        if (mCityList != null) {
            viewState?.showCityList(mCityList!!)
        }
    }
    private fun updateFavorites() {
        if (!mFavoritesList.isNullOrEmpty()) {
            viewState?.showFavorites(mFavoritesList!!)
        } else {
            mFavoritesList?.let { viewState?.showFavorites(it) }
            viewState?.showState(State.HasNoData)
        }
    }

    /**handling errors
     *
     * @param th
     */
    private fun handleError(th: Throwable) {
//        viewState.showState(State.NetworkError)
        Log.e(PRESENTER_FIND_TAG, th.message, th)
    }

    private fun loadFavoritesFromDb() {
        val loadFromDb = Single.fromCallable {
            AppDatabase.getAppDatabase(context).favoritesDao().all
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<FavoritesEntity?>? ->
                            mFavoritesList = data
                            updateFavorites()
                        }) { th: Throwable -> handleDbError(th, "loadFavoritesFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_FIND_TAG, "Load Favorites from db")
        viewState?.showState(State.HasData)
    }

    private fun removeFromFavorites(id: String) {
        val removeFromDb = Single.fromCallable {  }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { val db = AppDatabase.getAppDatabase(context)
                            db.favoritesDao().deleteById(id)
                            Log.i(PRESENTER_FIND_TAG, "removed from favorites: $id")
                        }) { th: Throwable -> handleDbError(th, "removeFromFavorites") }
        disposeOnDestroy(removeFromDb)
        loadData()
    }

    private fun handleDbError(th: Throwable, method: String) {
        viewState!!.showState(State.DbError)
        Log.e(PRESENTER_FIND_TAG, "handleDbError " + method + " " + th.message, th)
    }

}