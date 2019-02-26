package com.iaruchkin.deepbreath.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class WeatherItemAdapter extends RecyclerView.Adapter<WeatherItemAdapter.WeatherViewHolder>{

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;

    private final List<WeatherEntity> weatherItemList = new ArrayList<>();
    private final WeatherAdapterOnClickHandler mClickHandler;

    public interface WeatherAdapterOnClickHandler {
        void onClick(WeatherEntity weatherItem);
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
        WeatherEntity weatherItem = weatherItemList.get(position);
        holder.bind(weatherItem);

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
        private final TextView aqiTextView;
        private final TextView temperatureTextView;


        public void bind(WeatherEntity weatherItem) {
            imageView.setImageResource(R.drawable.art_clouds);
            categoryTextView.setText(weatherItem.getLocation());
            dateTextView.setText(weatherItem.getDate());
            temperatureTextView.setText(String.valueOf(weatherItem.getTemperature())+"\u00b0");
            aqiTextView.setText(String.valueOf(weatherItem.getTemperature()));
        }

        public WeatherViewHolder(View view) {
            super(view);

            imageView=view.findViewById(R.id.weather_icon);
            categoryTextView=view.findViewById(R.id.weather_description);
            dateTextView = view.findViewById(R.id.date);
            temperatureTextView = view.findViewById(R.id.high_temperature);
            aqiTextView = view.findViewById(R.id.purity);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            WeatherEntity dataItem = weatherItemList.get(adapterPosition);
            mClickHandler.onClick(dataItem);
        }
    }

        public void replaceItems(List<WeatherEntity> weatherData) {
            weatherItemList.clear();
            weatherItemList.addAll(weatherData);
            notifyDataSetChanged();
        }
}