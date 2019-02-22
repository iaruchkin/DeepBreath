package com.iaruchkin.deepbreath.ui;

import android.os.Bundle;

import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.ui.fragments.MessageFragmentListener;
import com.iaruchkin.deepbreath.ui.fragments.WeatherListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener {

    public final static String WEATHER_LIST_TAG = "NEWS_LIST";
    public final static String WEATHER_DETAILS_TAG = "NEWS_DETAILS";
    public final static String ABOUT_TAG = "ABOUT";
    public final static String INTRO_TAG = "INTRO";

    private FragmentManager mFragmentManager;
    private WeatherListFragment mWeatherListFragment;
//    private IntroFragment mIntroFragment;
//    private AboutFragment mAboutFragment;
//    private WeatherDetailsFragment mNewsDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        startNewsList();
//        if (savedInstanceState == null){
//            if (Storage.needToShowIntro(this)) {
//                startIntro();
//            } else {
//                startNewsList();
//            }
//        }
    }

//    private void startIntro(){
//        mIntroFragment = new IntroFragment();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_list, mIntroFragment)
//                .commit();
//    }

    private void startNewsList(){
        mWeatherListFragment = new WeatherListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mWeatherListFragment)
                .commit();
    }

//    private void startNewsDetails(String message){
//        mNewsDetailsFragment = NewsDetailsFragment.newInstance(message);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_list, mNewsDetailsFragment)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    private void startAbout(){
//        mAboutFragment = new AboutFragment();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_list, mAboutFragment)
//                .addToBackStack(null)
//                .commit();
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init(){
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onActionClicked(String fragmentTag, String message) {
        switch (fragmentTag){
            case WEATHER_LIST_TAG:
                startNewsList();
                break;
//            case WEATHER_DETAILS_TAG:
//                startNewsDetails(message);
//                break;
//            case ABOUT_TAG:
//                startAbout();
//                break;
//            case INTRO_TAG:
//                startIntro();
//                break;
        }

    }
}