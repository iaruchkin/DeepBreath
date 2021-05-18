package com.iaruchkin.deepbreath.ui.fragments

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Station
import com.iaruchkin.deepbreath.presentation.presenter.FindPresenter
import com.iaruchkin.deepbreath.presentation.view.FindView
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity
import com.iaruchkin.deepbreath.ui.adapter.AutocompleteAdapter
import com.iaruchkin.deepbreath.ui.adapter.FavoriteItemAdapter
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import kotlinx.android.synthetic.main.layout_error.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class FindFragment : MvpAppCompatFragment(), FindView, FavoriteItemAdapter.AdapterOnClickHandler {

    private var mListener: MessageFragmentListener? = null
    private var mAdapter: FavoriteItemAdapter? = null

    @JvmField
    @InjectPresenter
    var presenter: FindPresenter? = null

    @ProvidePresenter
    fun providePresenter(): FindPresenter {
        return FindPresenter()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageFragmentListener) {
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        setupToolbar(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupView()

        presenter?.update()
    }

    override fun showCityList(cityList: List<Data?>) {
        val stations = cityList.map { it?.station!! }
        setupSuggestions(stations)
    }

    override fun showState(state: State) {
        when (state) {
            State.HasData -> {
                rFavoritesRecyclerview?.visibility = View.VISIBLE
                rEmptyFavorites?.visibility = View.GONE
            }
            State.HasNoData -> {
                rFavoritesRecyclerview?.visibility = View.GONE
                rEmptyFavorites?.visibility = View.VISIBLE
            }
            State.DbError -> {
                rFavoritesRecyclerview?.visibility = View.GONE
                rEmptyFavorites?.visibility = View.GONE
                rErrorLayout?.visibility = View.VISIBLE
            }
            else -> throw IllegalArgumentException("Unknown state: $state")
        }    }

    override fun showFavorites(favorites: List<FavoritesEntity?>) {
        mAdapter?.replaceItems(favorites)
    }

    override fun onBookmarkOpen(location: Location) {
        mListener?.onStationInfoShow(location)
    }

    override fun onBookmarkRemove(id: String) {
        presenter?.removeItem(id)
    }

    private fun setupView() {
        rFindCity?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.length in 3..10) presenter?.loadCityList(s.toString())
            }

            override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
            ) {
            }
        })
    }

    private fun setupSuggestions(list: List<Station>) {
        rFindCity?.setAdapter(AutocompleteAdapter(requireContext(), list))
        rFindCity?.threshold = 1
        rFindCity?.setOnItemClickListener{
            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            Log.d("search item click", list[i].toString())
            mListener?.onStationInfoShow(list[i].getCoordinates())
        }
    }

    private fun setupRecyclerView(){
        if (mAdapter == null) mAdapter = FavoriteItemAdapter(this)
        rFavoritesRecyclerview.adapter = mAdapter
        rFavoritesRecyclerview.layoutManager = LinearLayoutManager(context)
    }

    private fun setupToolbar(view: View) {
        setHasOptionsMenu(true)

        val toolbar = view.findViewById<Toolbar>(R.id.rToolbar)
        (context as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (context as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayUseLogoEnabled(false)
            actionBar.title = getString(R.string.action_find)
        }
    }

}

