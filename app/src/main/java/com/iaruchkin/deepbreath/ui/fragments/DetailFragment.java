//package com.iaruchkin.deepbreath.ui.fragments;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.iaruchkin.deepbreath.App;
//import com.iaruchkin.deepbreath.R;
//import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
//import com.iaruchkin.deepbreath.common.State;
//import com.iaruchkin.deepbreath.presentation.presenter.DetailPresenter;
//import com.iaruchkin.deepbreath.presentation.view.DetailView;
//import com.iaruchkin.deepbreath.room.AqiEntity;
//import com.iaruchkin.deepbreath.room.ConditionEntity;
//import com.iaruchkin.deepbreath.room.ForecastEntity;
//import com.iaruchkin.deepbreath.room.WeatherEntity;
//import com.iaruchkin.deepbreath.utils.StringUtils;
//
//import java.util.Locale;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import io.reactivex.disposables.CompositeDisposable;
//
//public class DetailFragment extends MvpAppCompatFragment implements DetailView {
//    private static final int LAYOUT = R.layout.layout_detail;
//
//    Context context = App.INSTANCE.getApplicationContext();
//
//    static final String FORECAST_ID = "extra:forecast";
//    static final String WEATHER_ID = "extra:weather";
//    static final String AQI_ID = "extra:aqi";
//    static final String CONDITION_ID = "extra:condition";
//    static final String VIEW_TYPE = "extra:viewType";
//
//    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//    public static final String TAG = DetailFragment.class.getSimpleName();
//    private MessageFragmentListener listener;
//
////    @InjectPresenter
//    DetailPresenter detailPresenter;
//
//    TextView date;
//    TextView weather_description;
//    TextView high_temperature;
//    TextView low_temperature;
//    ImageView imageView;
//
//    TextView humidity;
//    TextView pressure;
//    TextView wind_measurement;
//
//    View aqiDetails;
//    TextView aqiLabel;
//
////    Toolbar toolbar;
//
////    @ProvidePresenter
//    DetailPresenter providePresenter() {
//            String idForecast = getArguments() != null ? getArguments().getString(FORECAST_ID, "") : null;
//            String idWeather = getArguments() != null ? getArguments().getString(WEATHER_ID, "") : null;
//            String idAqi = getArguments() != null ? getArguments().getString(AQI_ID, "") : null;
//            String idCondition = getArguments() != null ? getArguments().getString(CONDITION_ID, "") : null;
//
//        int viewType = getArguments() != null ? getArguments().getInt(VIEW_TYPE, 1) : 0;
//        return new DetailPresenter(idForecast, idWeather, idAqi, idCondition, viewType);
//    }
//
//    public static DetailFragment newInstance(String idForecast, String idWeather, String idAqi, int viewType){
//        DetailFragment fragmentAqi = new DetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(FORECAST_ID, idForecast);
//        bundle.putString(WEATHER_ID, idWeather);
//        bundle.putString(AQI_ID, idAqi);
//        bundle.putInt(VIEW_TYPE, viewType);
//
//        fragmentAqi.setArguments(bundle);
//        return fragmentAqi;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(LAYOUT, container, false);
////        if (getArguments() != null) {
////            getArguments().getString(FORECAST_ID);
////            getArguments().getString(WEATHER_ID);
////            getArguments().getString(WEATHER_ID);
////        }
//
//        findViews(view);
//        setupToolbar();
//        return view;
//    }
//
//    public void onStop() {
//        super.onStop();
//        compositeDisposable.clear();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof MessageFragmentListener){
//            listener = (MessageFragmentListener) context;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        listener = null;
//        super.onDetach();
//    }
//
//     private void setForecastView(ForecastEntity forecastData){
////        date.setText(forecastData.getDate());
//        imageView.setImageResource(R.drawable.art_clouds);
//        weather_description.setText(forecastData.getConditionText());
//        high_temperature.setText(String.valueOf(forecastData.getMaxtemp_c()));
//        low_temperature.setText(String.valueOf(forecastData.getMintemp_c()));
//     }
//
//    private void setAqiView(AqiEntity aqiData) {
//        humidity.setText(aqiData.getCityName());
//        pressure.setText(String.valueOf(aqiData.getAqi()));
////        wind_measurement.setText(String.valueOf(aqiData.getPm25()));
////        wind_measurement.setText(StringUtils.formatEpoch(context, aqiData.getDateEpoch()));
//        wind_measurement.setText(String.format(Locale.getDefault(), ""+"%s", (StringUtils.formatDateAqi(aqiData.getDateEpoch(), "HH:mm"))));
//    }
//
//    private void setWeatherView(WeatherEntity weatherData) {
////        date.setText(weatherData.getLast_updated());
////        date.setText(StringUtils.formatDate(context, weatherData.getLast_updated()));
//        imageView.setImageResource(R.drawable.art_clouds);
//        weather_description.setText(weatherData.getConditionText());
//        high_temperature.setText(String.valueOf(weatherData.getTemp_c()));
//        low_temperature.setText(String.valueOf(weatherData.getFeelslike_c()));
//    }
//
//    private void findViews(View view) {
////        toolbar = view.findViewById(R.id.toolbar);
//        date = view.findViewById(R.id.date);
//        weather_description = view.findViewById(R.id.weather_description);
//        high_temperature = view.findViewById(R.id.high_value);
//        low_temperature = view.findViewById(R.id.low_value);
//        humidity = view.findViewById(R.id.humidity);
//        pressure = view.findViewById(R.id.pressure);
//        wind_measurement = view.findViewById(R.id.wind_measurement);
//        imageView = view.findViewById(R.id.weather_icon);
//
//        aqiDetails = view.findViewById(R.id.aqi_details);
//        aqiLabel = view.findViewById(R.id.aqi_label);
//    }
//
//    @Override
//    public void showWeatherData(@NonNull WeatherEntity data) {
//        setWeatherView(data);
//    }
//
//    @Override
//    public void showForecastData(@NonNull ForecastEntity data, @NonNull ConditionEntity condition) {
//
//    }
//
//    @Override
//    public void showAqiData(@NonNull AqiEntity data) {
//        setAqiView(data);
//    }
//
//    @Override
//    public void showTodayData(@NonNull WeatherEntity weatherEntity, @NonNull AqiEntity aqiEntity, @NonNull ConditionEntity condition) {
//
//    }
//
//    @Override
//    public void showState(@NonNull State state) {
//
//        switch (state) {
//            case Current:
//
//                break;
//
//            case Forecast:
//                aqiDetails.setVisibility(View.GONE);
//                aqiLabel.setVisibility(View.GONE);
//                break;
//
//            default:
//                throw new IllegalArgumentException("Unknown state: " + state);
//        }
//    }
//
//    private void setupToolbar() {//todo привести в порядок, сейчас работает через стили и манифест
//        setHasOptionsMenu(true);
////        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
//        ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
//        actionBar.setDisplayUseLogoEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle("Moscow");
//
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//    }
//}
