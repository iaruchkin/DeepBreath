package com.iaruchkin.deepbreath.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.WeatherViewHolder>{

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;

    private final List<ForecastEntity> forecastItemList = new ArrayList<>();
    private WeatherEntity weatherItem = new WeatherEntity();
    private AqiEntity aqiItem = new AqiEntity();
    private final ForecastAdapterOnClickHandler mClickHandler;

    public interface ForecastAdapterOnClickHandler {
        void onClickList(ForecastEntity forecastItem, WeatherEntity weatherEntity, AqiEntity aqiEntity);
    }

    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mUseTodayLayout = App.INSTANCE.getApplicationContext().getResources().getBoolean(R.bool.use_today_layout);
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.item_today_forecast;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.item_forecast;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View weatherListView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new WeatherViewHolder(weatherListView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, final int position) {
        ForecastEntity forecastItem = forecastItemList.get(position);

        //todo set images
//        int weatherImageId;
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_TODAY:
                holder.bindFirst(forecastItem, weatherItem ,aqiItem);
//                weatherImageId = SunshineWeatherUtils
//                        .getLargeArtResourceIdForWeatherCondition(weatherId);
                break;
            case VIEW_TYPE_FUTURE_DAY:
                holder.bindFuture(forecastItem);
//                weatherImageId = SunshineWeatherUtils
//                        .getSmallArtResourceIdForWeatherCondition(weatherId);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

    }

    @Override
    public int getItemCount() {
        if (null == forecastItemList) return 0;
        return forecastItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView weatherDescTextView;
        private final TextView locationTextView;
        private final TextView dateTextView;
        private final TextView highTemperatureTextView;
        private final TextView lowTemperatureTextView;

        private final TextView aqiTextView;


        public void bindFirst(ForecastEntity forecastItem, WeatherEntity weatherItem, AqiEntity aqiItem) {

            imageView.setImageResource(R.drawable.art_clouds);

            aqiTextView.setText(String.valueOf(aqiItem.getAqi()));
            locationTextView.setText(forecastItem.getLocationName());
            weatherDescTextView.setText(weatherItem.getConditionText());
            dateTextView.setText(weatherItem.getLast_updated());
            highTemperatureTextView.setText(String.format(Locale.getDefault(), "%s\u00b0", weatherItem.getTemp_c()));
            lowTemperatureTextView.setText(String.format(Locale.getDefault(), "%s\u00b0", weatherItem.getFeelslike_c()));
        }

        public void bindFuture(ForecastEntity forecastItem) {

            imageView.setImageResource(R.drawable.art_clouds);

            weatherDescTextView.setText(forecastItem.getParameter());
            dateTextView.setText(forecastItem.getDate());
            highTemperatureTextView.setText(String.format(Locale.getDefault(), "%s\u00b0", forecastItem.getMaxtemp_c()));
            lowTemperatureTextView.setText(String.format(Locale.getDefault(), "%s\u00b0", forecastItem.getMintemp_c()));
        }

        public WeatherViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.weather_icon);
            weatherDescTextView = view.findViewById(R.id.weather_description);
            locationTextView = view.findViewById(R.id.location_desc);
            dateTextView = view.findViewById(R.id.date);
            highTemperatureTextView = view.findViewById(R.id.high_temperature);
            lowTemperatureTextView = view.findViewById(R.id.low_temperature);

            aqiTextView = view.findViewById(R.id.aqi);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ForecastEntity dataItem = forecastItemList.get(adapterPosition);
            mClickHandler.onClickList(dataItem, weatherItem, aqiItem);
        }
    }

        public void setForecastItems(List<ForecastEntity> forecastData) {
            forecastItemList.clear();
            forecastItemList.add(forecastData.get(0));
            forecastItemList.addAll(forecastData);
            notifyDataSetChanged();
        }

        public void setWeatherItem(WeatherEntity data){
            weatherItem = data;
            notifyDataSetChanged();
        }

        public void setAqiItem(AqiEntity data){
            aqiItem = data;
            notifyDataSetChanged();
        }
}