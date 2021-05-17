package com.iaruchkin.deepbreath.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Station
import com.iaruchkin.deepbreath.presentation.presenter.FindPresenter
import com.iaruchkin.deepbreath.presentation.view.FindView
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity
import com.iaruchkin.deepbreath.ui.adapter.AutocompleteAdapter
import com.iaruchkin.deepbreath.ui.adapter.FavoriteItemAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class FindFragment : MvpAppCompatFragment(), FindView {
    private var listener: MessageFragmentListener? = null
    private var mFindCity: AutoCompleteTextView? = null
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
            listener = context
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        setupToolbar(view)
        findViews(view)
        //todo создание элемента, избранное, удаление, история
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        mFindCity?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.length in 3..4) presenter?.loadCityList(s.toString())
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

        presenter?.update()
    }

    private fun setupSuggestions(list: List<Station>) {
        mFindCity?.setAdapter(AutocompleteAdapter(requireContext(), list))
        mFindCity?.threshold = 1 //todo настроить
        mFindCity?.setOnItemClickListener{
            adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            //todo get aqi for geo
            Log.d("search item click", list[i].toString())
//            listener?.onActionClicked(SEARCH_LIST_TAG)
            listener?.onStationInfoShow(list[i].geo!!)
        }
    }

    private fun setupRecyclerView(){
        mAdapter = FavoriteItemAdapter(null)
        favorites_recyclerview.adapter = mAdapter

        favorites_recyclerview.layoutManager = LinearLayoutManager(context)
    }

    private fun setupToolbar(view: View) {
        setHasOptionsMenu(true)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (context as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (context as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayUseLogoEnabled(false)
            actionBar.title = getString(R.string.action_find)
        }
    }

    private fun findViews(view: View) {
        mFindCity = view.findViewById(R.id.findCity)

    }

    override fun showCityList(cityList: List<Data?>) {
        val stations = cityList.map { it?.station!! }
        setupSuggestions(stations)
    }

    override fun showFavorites(favorites: List<FavoritesEntity?>) {
        mAdapter?.replaceItems(favorites)
    }

}

