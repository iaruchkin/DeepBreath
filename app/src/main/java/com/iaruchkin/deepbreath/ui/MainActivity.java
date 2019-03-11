package com.iaruchkin.deepbreath.ui;

import android.os.Bundle;

import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.ui.fragments.AqiFragment;
import com.iaruchkin.deepbreath.ui.fragments.MessageFragmentListener;
import com.iaruchkin.deepbreath.ui.fragments.SettingsFragment;
import com.iaruchkin.deepbreath.ui.fragments.WeatherListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener {

    public final static String WEATHER_LIST_TAG = "WEATHER_LIST";
    public final static String WEATHER_DETAILS_TAG = "WEATHER_DETAILS";
    public final static String SETTINGS_TAG = "SETTINGS";

    public final static String INTRO_TAG = "INTRO";

    private FragmentManager mFragmentManager;
    private WeatherListFragment mWeatherListFragment;
    private AqiFragment mDetailsFragment;
    private SettingsFragment mSettingsFragment;
    //    private IntroFragment mIntroFragment;

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

    private void startNewsDetails(String message){
        mDetailsFragment = AqiFragment.newInstance(message, message);//todo set correct string messages
        mDetailsFragment = new AqiFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startSettings(){
        mSettingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mSettingsFragment)
                .addToBackStack(null)
                .commit();
    }

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
            case WEATHER_DETAILS_TAG:
                startNewsDetails(message);
                break;
            case SETTINGS_TAG:
                startSettings();
                break;
//            case INTRO_TAG:
//                startIntro();
//                break;
        }

    }
}