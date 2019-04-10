package com.iaruchkin.deepbreath.ui.fragments.intro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.AppPreferences;
import com.iaruchkin.deepbreath.ui.fragments.MessageFragmentListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import static com.iaruchkin.deepbreath.ui.MainActivity.GET_LOCATION;
import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_LIST_TAG;

public class IntroFragment extends Fragment {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Button btnSkip, btnNext, btnPermission;
    private MessageFragmentListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.intro_layout, container, false);

        mPager = view.findViewById(R.id.pager);
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip = view.findViewById(R.id.btn_skip);
        btnNext = view.findViewById(R.id.btn_next);
        btnPermission = view.findViewById(R.id.btn_permission);

        btnPermission.setOnClickListener(v -> {
            getLocation();
            btnNext.setVisibility(View.VISIBLE);
            btnSkip.setVisibility(View.GONE);
            btnPermission.setVisibility(View.GONE);
            btnNext.setText(getString(R.string.start_button));
        });

        btnSkip.setOnClickListener(v -> startApp());

        btnNext.setOnClickListener(v -> {

            int current = mPager.getCurrentItem()+1;
            if (current < NUM_PAGES) {
                mPager.setCurrentItem(current);
                if (current == NUM_PAGES - 1){
                    showLastPage();
                }
            } else {
                startApp();
            }
        });

        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

    return view;
    }

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            if (position == NUM_PAGES - 1) {
                showLastPage();
            } else {
                showPage();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void showLastPage(){
        btnNext.setVisibility(View.GONE);
        btnSkip.setVisibility(View.VISIBLE);
        btnSkip.setText(getString(R.string.later_button));
        btnPermission.setVisibility(View.VISIBLE);
        btnPermission.setText(getString(R.string.permission_button));
    }

    private void showPage(){
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setText(getString(R.string.next_button));
        btnSkip.setVisibility(View.VISIBLE);
        btnSkip.setText(getString(R.string.skip_button));
        btnPermission.setVisibility(View.GONE);
    }

    private void startApp() {
        AppPreferences.introFinished(getContext());
        if (listener != null) {
            listener.onActionClicked(WEATHER_LIST_TAG);
        }
    }

    private void getLocation() {
        if (listener != null) {
            listener.onActionClicked(GET_LOCATION);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("intro", "onStop");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageFragmentListener) {
            listener = (MessageFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return IntroPage.newInstance(R.drawable.ic_flask, R.string.intro_page1_title, R.string.intro_page1_desc, R.color.colorPrimaryDark);
                case 1:
                    return IntroPage.newInstance(R.drawable.ic_weather_icon_thunder, R.string.intro_page2_title, R.string.intro_page2_desc, R.color.bg_screen1);
                case 2:
                    return IntroPage.newInstance(R.drawable.ic_gps_map, R.string.intro_page3_title, R.string.intro_page3_desc, R.color.colorPrimaryDark);
                default:
                    return IntroPage.newInstance(R.drawable.ic_flask, R.string.intro_page1_title, R.string.intro_page1_desc, R.color.colorPrimaryDark);
            }
        }

    }

}