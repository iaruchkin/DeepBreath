package com.iaruchkin.deepbreath.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.presentation.presenter.FindPresenter
import com.iaruchkin.deepbreath.presentation.view.FindView

class FindFragment : MvpAppCompatFragment(), FindView {

    @JvmField
    @InjectPresenter
    var presenter: FindPresenter? = null

    @ProvidePresenter
    fun providePresenter(): FindPresenter {
        return FindPresenter()
    }

    var mFindCity: AutoCompleteTextView? = null
    var cities = listOf("Moscow", "London", "New York")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_find, container, false)

        setupToolbar(view)
        findViews(view)
        setupSuggestions()
        //todo запрос к апи
        // создание элемента, избранное, удаление, история
        return view
    }

    private fun setupSuggestions() {
        mFindCity?.setAdapter(ArrayAdapter<String>(context!!, android.R.layout.select_dialog_item, cities))
        mFindCity?.threshold = 1 //todo настроить
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
        cities = cityList.map { it.toString() }
    }

}

