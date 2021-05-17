package com.iaruchkin.deepbreath.presentation.presenter

import android.util.Log
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.common.BasePresenter
import com.iaruchkin.deepbreath.network.dtos.CityList
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.network.parsers.FindCityApi
import com.iaruchkin.deepbreath.presentation.view.FindView
import com.iaruchkin.deepbreath.room.AppDatabase
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState

@InjectViewState
class FindPresenter : BasePresenter<FindView?>() {
    private val context = App.INSTANCE.applicationContext
    private var aqiEntity: List<AqiEntity?>? = null
    private var mCityList: List<Data?>? = null
    private var mFavoritesList: List<FavoritesEntity?>? = null
    private val PRESENTER_FIND_TAG = "[find - presenter]"

    override fun onFirstViewAttach() {
        loadData()
    }

    private fun loadData() {
        loadFavoritesFromDb()
    }

    fun loadCityList(text: String) {
        loadCityFromNet(text)
    }

    fun update() {
        loadData()
    }

    /**network
     *
     * @param parameter
     */
    private fun loadCityFromNet(parameter: String) {
        Log.i(PRESENTER_FIND_TAG, "Load City from net presenter")
        //        getViewState().showState(State.LoadingAqi);
        val disposable = FindCityApi.getInstance()
                .findCityEndpoint()
                .get(parameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: CityList ->
                    //                    updateAqiDB(response, "")
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
            viewState!!.showCityList(mCityList!!)
            //            getViewState().showState(State.HasData);
        }
    }
    private fun updateFavorites() {
            viewState!!.showFavorites(mFavoritesList!!)
    }

    /**handling errors
     *
     * @param th
     */
    private fun handleError(th: Throwable) {
        //        getViewState().showState(State.NetworkError);
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
                        }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.e(PRESENTER_FIND_TAG, "Load Favorites from db")
        //        getViewState().showState(State.HasData);
    }

    private fun handleDbError(th: Throwable) { //        getViewState().showState(State.DbError);
        Log.e(PRESENTER_FIND_TAG, th.message, th)
    }
}