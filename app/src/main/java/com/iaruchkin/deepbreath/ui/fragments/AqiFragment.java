package com.iaruchkin.deepbreath.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.presentation.presenter.AqiPresenter;
import com.iaruchkin.deepbreath.presentation.view.AqiView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public class AqiFragment extends MvpAppCompatFragment implements AqiView {
    private static final int LAYOUT = R.layout.layout_detail;

    static final String FORECAST_ID = "extra:itemLocation";
    static final String WEATHER_ID = "extra:itemOption";
    static final String AQI_ID = "extra:itemOption";

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

    @ProvidePresenter
    AqiPresenter providePresenter() {
//        if (getArguments() != null) {
            String idForecast = getArguments().getString(FORECAST_ID, "");
            String idWeather = getArguments().getString(WEATHER_ID, "");
            String idAqi = getArguments().getString(AQI_ID, "");
            return new AqiPresenter(idForecast, idWeather, idAqi);
//        }
    }

    public static AqiFragment newInstance(String idForecast, String idWeather, String idAqi){
        AqiFragment fragmentAqi = new AqiFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FORECAST_ID, idForecast);
        bundle.putSerializable(WEATHER_ID, idWeather);
        bundle.putSerializable(AQI_ID, idAqi);

        fragmentAqi.setArguments(bundle);
        return fragmentAqi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
//        if (getArguments() != null) {
//            getArguments().getString(FORECAST_ID);
//            getArguments().getString(WEATHER_ID);
//            getArguments().getString(WEATHER_ID);
//        }
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

     private void setForecastView(ForecastEntity forecastData){
        date.setText(forecastData.getDate());
        weather_description.setText(forecastData.getConditionText());
        high_temperature.setText(String.valueOf(forecastData.getMaxtemp_c()));
        low_temperature.setText(String.valueOf(forecastData.getMintemp_c()));
     }

    private void setAqiView(AqiEntity aqiData) {
        humidity.setText(aqiData.getCityName());
        pressure.setText(String.valueOf(aqiData.getAqi()));
        wind_measurement.setText(String.valueOf(aqiData.getPm25()));
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
    public void showWeatherData(@NonNull WeatherEntity data) {
//        setForecastView(data);
    }

    @Override
    public void showForecastData(@NonNull ForecastEntity data) {
        setForecastView(data);
    }

    @Override
    public void showAqiData(@NonNull AqiEntity data) {
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
