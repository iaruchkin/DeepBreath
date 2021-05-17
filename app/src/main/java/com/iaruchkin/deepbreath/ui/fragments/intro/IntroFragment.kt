package com.iaruchkin.deepbreath.ui.fragments.intro

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.AppPreferences
import com.iaruchkin.deepbreath.ui.GET_LOCATION
import com.iaruchkin.deepbreath.ui.WEATHER_LIST_TAG
import com.iaruchkin.deepbreath.ui.fragments.MessageFragmentListener
import me.relex.circleindicator.CircleIndicator

class IntroFragment : Fragment() {

    private lateinit var mPager: ViewPager
    private var mPagerAdapter: PagerAdapter? = null
    private var btnSkip: Button? = null
    private var btnNext: Button? = null
    private var btnPermission: Button? = null
    private var listener: MessageFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.intro_layout, container, false)
        mPager = view.findViewById(R.id.pager)
        mPagerAdapter = ViewPagerAdapter(childFragmentManager)
        mPager.setAdapter(mPagerAdapter)
        mPager.addOnPageChangeListener(viewPagerPageChangeListener)
        btnSkip = view.findViewById(R.id.btn_skip)
        btnNext = view.findViewById(R.id.btn_next)
        btnPermission = view.findViewById(R.id.btn_permission)
        btnPermission?.setOnClickListener(View.OnClickListener { v: View? ->
            location
            btnNext?.setVisibility(View.VISIBLE)
            btnSkip?.setVisibility(View.GONE)
            btnPermission?.setVisibility(View.GONE)
            btnNext?.setText(getString(R.string.start_button))
        })
        btnSkip?.setOnClickListener(View.OnClickListener { v: View? -> startApp() })
        btnNext?.setOnClickListener(View.OnClickListener { v: View? ->
            val current = mPager.getCurrentItem() + 1
            if (current < NUM_PAGES) {
                mPager.setCurrentItem(current)
                if (current == NUM_PAGES - 1) {
                    showLastPage()
                }
            } else {
                startApp()
            }
        })
        val indicator: CircleIndicator = view.findViewById(R.id.indicator)
        indicator.setViewPager(mPager)
        return view
    }

    private val viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            if (position == NUM_PAGES - 1) {
                showLastPage()
            } else {
                showPage()
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    private fun showLastPage() {
        btnNext!!.visibility = View.GONE
        btnSkip!!.visibility = View.VISIBLE
        btnSkip!!.text = getString(R.string.later_button)
        btnPermission!!.visibility = View.VISIBLE
        btnPermission!!.text = getString(R.string.permission_button)
    }

    private fun showPage() {
        btnNext!!.visibility = View.VISIBLE
        btnNext!!.text = getString(R.string.next_button)
        btnSkip!!.visibility = View.VISIBLE
        btnSkip!!.text = getString(R.string.skip_button)
        btnPermission!!.visibility = View.GONE
    }

    private fun startApp() {
        AppPreferences.introFinished(context)
        if (listener != null) {
            listener!!.onActionClicked(WEATHER_LIST_TAG)
        }
    }

    private val location: Unit
        private get() {
            if (listener != null) {
                listener!!.onActionClicked(GET_LOCATION)
            }
        }

    override fun onStop() {
        super.onStop()
        Log.e("intro", "onStop")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageFragmentListener) {
            listener = context
        }
    }

    class ViewPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
        override fun getCount(): Int {
            return NUM_PAGES
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> IntroPage.newInstance(R.drawable.ic_flask, R.string.intro_page1_title, R.string.intro_page1_desc, R.color.colorPrimaryDark)
                1 -> IntroPage.newInstance(R.drawable.ic_weather_icon_thunder, R.string.intro_page2_title, R.string.intro_page2_desc, R.color.bg_screen1)
                2 -> IntroPage.newInstance(R.drawable.ic_gps_map, R.string.intro_page3_title, R.string.intro_page3_desc, R.color.colorPrimaryDark)
                else -> IntroPage.newInstance(R.drawable.ic_flask, R.string.intro_page1_title, R.string.intro_page1_desc, R.color.colorPrimaryDark)
            }
        }
    }

    companion object {
        private const val NUM_PAGES = 3
    }
}