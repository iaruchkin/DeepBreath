package com.iaruchkin.deepbreath.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment

class FindFragment : MvpAppCompatFragment() {

    private var mDeveloper: View? = null

//    @InjectPresenter
//    internal var presenter: AboutPresenter? = null
//
//    @ProvidePresenter
//    internal fun providePresenter(): AboutPresenter {
//        return AboutPresenter()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        setupToolbar(view)
        findViews(view)

        return view
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
            actionBar.title = getString(R.string.action_about)
        }
    }

    private fun findViews(view: View) {
        mDeveloper = view.findViewById(R.id.developer)
    }

}

