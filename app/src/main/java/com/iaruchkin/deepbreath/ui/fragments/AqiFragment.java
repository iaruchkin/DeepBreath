package com.iaruchkin.deepbreath.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiApi;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.presentation.presenter.AqiPresenter;
import com.iaruchkin.deepbreath.presentation.presenter.WeatherListPresenter;
import com.iaruchkin.deepbreath.presentation.view.AqiView;
import com.iaruchkin.deepbreath.presentation.view.WeatherListView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public class AqiFragment extends MvpAppCompatFragment implements AqiView {
    private static final int LAYOUT = R.layout.layout_detail;

    static final String EXTRA_ITEM_LOCATION = "extra:itemLocation";
    static final String EXTRA_ITEM_OPTION = "extra:itemOption";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final String TAG = AqiFragment.class.getSimpleName();
    private MessageFragmentListener listener;

    @InjectPresenter
    AqiPresenter aqiPresenter;

    TextView date;
    TextView weather_description;
    TextView high_temperature;
    TextView low_temperature;

    TextView humidity;
    TextView pressure;
    TextView wind_measurement;

//    Toolbar toolbar;

    public static AqiFragment newInstance(String location, String option){
        AqiFragment fragmentAqi = new AqiFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ITEM_LOCATION, location);
        bundle.putSerializable(EXTRA_ITEM_OPTION, option);
        fragmentAqi.setArguments(bundle);
        return fragmentAqi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
//        getArguments().getString(EXTRA_ITEM_LOCATION);
//        getArguments().getString(EXTRA_ITEM_OPTION);

        findViews(view);
        setupToolbar();
        return view;
    }

    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageFragmentListener){
            listener = (MessageFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

     private void setWeatherView(WeatherEntity weatherData){
        date.setText(weatherData.getDate());
        weather_description.setText(weatherData.getLocation());
        low_temperature.setText(weatherData.getTemperature().toString());
     }

    private void setAqiView(AqiEntity aqiData) {
        high_temperature.setText(aqiData.getAqi());
        humidity.setText(aqiData.getCityName());
        pressure.setText(aqiData.getId());
        wind_measurement.setText(aqiData.getPm10().toString());
    }

    private void findViews(View view) {
//        toolbar = view.findViewById(R.id.toolbar);
        date = view.findViewById(R.id.date);
        weather_description = view.findViewById(R.id.weather_description);
        high_temperature = view.findViewById(R.id.high_temperature);
        low_temperature = view.findViewById(R.id.low_temperature);
        humidity = view.findViewById(R.id.humidity);
        pressure = view.findViewById(R.id.pressure);
        wind_measurement = view.findViewById(R.id.wind_measurement);

    }

    @Override
    public void setWeatherData(@NonNull WeatherEntity data) {
        setWeatherView(data);
    }

    @Override
    public void setAqiData(@NonNull AqiEntity data) {
        setAqiView(data);
    }

    @Override
    public void showState(@NonNull State state) {

    }

    public void loadData() {

        aqiPresenter.loadData();

    }

    private void setupToolbar() {//todo привести в порядок, сейчас работает через стили и манифест
        setHasOptionsMenu(true);
//        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Moscow");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
