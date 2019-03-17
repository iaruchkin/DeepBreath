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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class WeatherItemAdapter extends RecyclerView.Adapter<WeatherItemAdapter.WeatherViewHolder>{

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;

    private final List<ForecastEntity> weatherItemList = new ArrayList<>();
    private AqiEntity aqiItem = new AqiEntity();
    private final WeatherAdapterOnClickHandler mClickHandler;

    public interface WeatherAdapterOnClickHandler {
        void onClick(ForecastEntity weatherItem);
    }

    public WeatherItemAdapter(WeatherAdapterOnClickHandler clickHandler) {
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
        ForecastEntity weatherItem = weatherItemList.get(position);
        holder.bind(weatherItem ,aqiItem, position);

        //todo set images
//        int weatherImageId;
//        int viewType = getItemViewType(position);
//
//        switch (viewType) {
//            case VIEW_TYPE_TODAY:
//                weatherImageId = SunshineWeatherUtils
//                        .getLargeArtResourceIdForWeatherCondition(weatherId);
//                break;
//            case VIEW_TYPE_FUTURE_DAY:
//                weatherImageId = SunshineWeatherUtils
//                        .getSmallArtResourceIdForWeatherCondition(weatherId);
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
//        }

    }

    @Override
    public int getItemCount() {
        if (null == weatherItemList) return 0;
        return weatherItemList.size();
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
        private final TextView categoryTextView;
        private final TextView dateTextView;
        private final TextView temperatureTextView;
        private final TextView aqiTextView;


        public void bind(ForecastEntity weatherItem, AqiEntity aqiItem , int viewType) {

            imageView.setImageResource(R.drawable.art_clouds);
            categoryTextView.setText(weatherItem.getLocationName());
            dateTextView.setText(weatherItem.getDate());
            temperatureTextView.setText(String.valueOf(weatherItem.getAvgtemp_c())+"\u00b0");

            if (viewType == VIEW_TYPE_TODAY) {
                    aqiTextView.setText(String.valueOf(aqiItem.getAqi()));
                }
//                    aqiTextView.setText(String.valueOf(aqiItem.getAqi()));
//            aqiTextView.setText(aqiItem.getCityName());
        }

        public WeatherViewHolder(View view) {
            super(view);

            imageView=view.findViewById(R.id.weather_icon);
            categoryTextView=view.findViewById(R.id.weather_description);
            dateTextView = view.findViewById(R.id.date);
            temperatureTextView = view.findViewById(R.id.high_temperature);
            aqiTextView = view.findViewById(R.id.aqi);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ForecastEntity dataItem = weatherItemList.get(adapterPosition);
            mClickHandler.onClick(dataItem);
        }
    }

        public void replaceItems(List<ForecastEntity> forecastData) {
            weatherItemList.clear();
            weatherItemList.addAll(forecastData);
            notifyDataSetChanged();
        }

        public void setAqiItem(AqiEntity data){
            aqiItem = data;
            notifyDataSetChanged();
        }
}