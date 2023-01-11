package com.iaruchkin.deepbreath.screens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.screens.ABOUT_TAG

class AboutFragment : Fragment() {
    private var mDeveloper: View? = null
    private var mGithub: View? = null
    private var mAqiLink: View? = null
    private var mAqiAvLink: View? = null
    private var mWeatherLink: View? = null
    private var mIconsLink: View? = null
    private var mLogoLink: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        setupToolbar(view)
        findViews(view)
        setupUx()
        return view
    }

    fun openURL(url: String) {
        val intent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse(url))
        startActivity(intent)
    }

    fun composeEmail() {
        Log.i(ABOUT_TAG, "composeEmail")
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data =
            Uri.parse(
                String.format(
                    "mailto:%s",
                    getString(R.string.email_adress)
                )
            ) // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email))
        startActivity(intent)
    }

    private fun setupUx() {
        mDeveloper!!.setOnClickListener { v: View? ->
            composeEmail()
        }
        mGithub!!.setOnClickListener { v: View? ->
            openURL(getString(R.string.github_link))
        }
        mAqiLink!!.setOnClickListener { v: View? ->
            openURL(getString(R.string.aqi_link))
        }
        mAqiAvLink!!.setOnClickListener { v: View? ->
            openURL(getString(R.string.aqiav_link))
        }
        mWeatherLink!!.setOnClickListener { v: View? ->
            openURL(getString(R.string.weather_link))
        }
        mIconsLink!!.setOnClickListener { v: View? ->
            openURL(getString(R.string.icons_link))
        }
        mLogoLink!!.setOnClickListener { v: View? ->
            openURL(getString(R.string.logo_link))
        }
    }

    private fun setupToolbar(view: View) {
        setHasOptionsMenu(true)
        val toolbar = view.findViewById<Toolbar>(R.id.rToolbar)
        (context as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as AppCompatActivity?)!!.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayUseLogoEnabled(false)
            actionBar.title = getString(R.string.action_about)
        }
    }

    private fun findViews(view: View) {
        mDeveloper = view.findViewById(R.id.developer)
        mGithub = view.findViewById(R.id.github)
        mAqiLink = view.findViewById(R.id.aqi_link)
        mAqiAvLink = view.findViewById(R.id.aqiav_link)
        mWeatherLink = view.findViewById(R.id.weather_link)
        mIconsLink = view.findViewById(R.id.icons_link)
        mLogoLink = view.findViewById(R.id.logo_link)
    }

}