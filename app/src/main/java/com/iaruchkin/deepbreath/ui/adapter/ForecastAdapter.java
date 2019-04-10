package com.iaruchkin.deepbreath.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConditionEntity;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;
import com.iaruchkin.deepbreath.utils.AqiUtils;
import com.iaruchkin.deepbreath.utils.ConditionUtils;
import com.iaruchkin.deepbreath.utils.StringUtils;
import com.iaruchkin.deepbreath.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.WeatherViewHolder>{

    private final String DATE_FORMAT = "HH:mm, EEEE";
    private final String TAG_ADAPTER = "ADAPTER";

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;

    private Context context = App.INSTANCE.getApplicationContext();
    private final List<ForecastEntity> forecastItemList = new ArrayList<>();
    private final List<ConditionEntity> conditionItemList = new ArrayList<>();

    private WeatherEntity weatherItem = new WeatherEntity();
    private AqiEntity aqiItem = new AqiEntity();
    private final ForecastAdapterOnClickHandler mClickHandler;

    public interface ForecastAdapterOnClickHandler {
        void onClickList(ForecastEntity forecastItem, WeatherEntity weatherEntity, AqiEntity aqiEntity, ConditionEntity conditionEntity, int viewType);
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
        String conditionText;
        int aqi;
        int iconForecast;
        int iconToday;
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_TODAY:

                if (aqiItem.getAqi() != null){
                    Log.i(TAG_ADAPTER, aqiItem.toString());
                    Log.i(TAG_ADAPTER, weatherItem.toString());
                    aqi = aqiItem.getAqi();
                }
                else aqi = 0;

                if(conditionItemList.size()!=0) {
                    iconToday  = conditionItemList.get(ConditionUtils.getConditionCode(weatherItem.getConditionCode())).getIcon();
                    if (weatherItem.getIsDay() == 1) {
                        conditionText = conditionItemList.get(ConditionUtils.getConditionCode(weatherItem.getConditionCode())).getDayText();
                    } else {
                        conditionText = conditionItemList.get(ConditionUtils.getConditionCode(weatherItem.getConditionCode())).getNightText();
                    }
                    Log.i(TAG_ADAPTER, forecastItem.toString());
                    Log.i(TAG_ADAPTER, conditionText);
                } else {
                    conditionText = null;
                    iconToday = 0;
                }

                holder.bindFirst(forecastItem, weatherItem  , aqi, conditionText, iconToday);
                break;

            case VIEW_TYPE_FUTURE_DAY:
                if(conditionItemList.size()!=0) {
                    iconForecast = conditionItemList.get(ConditionUtils.getConditionCode(forecastItem.getConditionCode())).getIcon();
                    Log.i(TAG_ADAPTER, forecastItem.toString());
                    Log.i(TAG_ADAPTER, String.valueOf(iconForecast));
                } else {
                    iconForecast = 0;
                }

                holder.bindFuture(forecastItem, iconForecast);
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
        private final TextView aqiDesc;
        private final CardView aqiCard;
        private final CardView weatherCard;
        private final TextView recomendation;

        public void bindFirst(ForecastEntity forecastItem, WeatherEntity weatherItem, int aqi, String dayText, int icon) {

            String highString = WeatherUtils.formatTemperature(context, weatherItem.getTemp_c());
            String lowString = WeatherUtils.formatTemperature(context, weatherItem.getFeelslike_c());

            imageView.setImageResource(WeatherUtils.getLargeArtResource(icon, weatherItem.getIsDay()));

            aqiTextView.setText(String.valueOf(aqi));
            locationTextView.setText(StringUtils.transliterateLatToRus(forecastItem.getLocationName(), forecastItem.getLocationCountry()));

            weatherDescTextView.setText(dayText);

            dateTextView.setText(String.format(Locale.getDefault(), context.getResources().getString(R.string.today)+" %s",
                    (StringUtils.formatDate(weatherItem.getLast_updated_epoch(), "HH:mm"))));

            highTemperatureTextView.setText(highString);
            lowTemperatureTextView.setText(lowString);

            aqiDesc.setText(AqiUtils.getPollutionLevel(aqi));
            aqiCard.setCardBackgroundColor(context.getResources().getColor(AqiUtils.getColor(aqi)));

            recomendation.setText(AqiUtils.getRecomendation(aqi));
        }

        public void bindFuture(ForecastEntity forecastItem, int icon) {

            String highString = WeatherUtils.formatTemperature(context, forecastItem.getMaxtemp_c());
            String lowString = WeatherUtils.formatTemperature(context, forecastItem.getMintemp_c());

            imageView.setImageResource(WeatherUtils.getSmallArtResource(icon, forecastItem.getIsDay()));

            dateTextView.setText(StringUtils.formatDate(forecastItem.getDate_epoch(), "EEEE"));
            weatherDescTextView.setText(StringUtils.formatDate(forecastItem.getDate_epoch(), "d MMMM"));

            highTemperatureTextView.setText(highString);
            lowTemperatureTextView.setText(lowString);
        }

        public WeatherViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.weather_icon);
            weatherDescTextView = view.findViewById(R.id.weather_description);
            locationTextView = view.findViewById(R.id.location_desc);
            dateTextView = view.findViewById(R.id.date);
            highTemperatureTextView = view.findViewById(R.id.high_value);
            lowTemperatureTextView = view.findViewById(R.id.low_value);

            aqiTextView = view.findViewById(R.id.aqi_value);
            aqiDesc = view.findViewById(R.id.aqi_description);
            aqiCard = view.findViewById(R.id.aqi_pre_card);
            weatherCard = view.findViewById(R.id.today_card);
            recomendation = view.findViewById(R.id.recomendation);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ForecastEntity forecastItem = forecastItemList.get(adapterPosition);
            int code;
                if(adapterPosition == 0) code = weatherItem.getConditionCode();
                else code = forecastItem.getConditionCode();

            ConditionEntity conditionItem = conditionItemList.get(ConditionUtils.getConditionCode(code));
            mClickHandler.onClickList(forecastItem, weatherItem, aqiItem, conditionItem, getItemViewType());
        }
    }

        public void setData(@NonNull List<ForecastEntity> forecastEntity,
                            @NonNull WeatherEntity weatherEntity,
                            @NonNull AqiEntity aqiEntity,
                            @NonNull List<ConditionEntity> conditionEntity){
            forecastItemList.clear();
            forecastItemList.add(forecastEntity.get(0));
            forecastItemList.addAll(forecastEntity);

            weatherItem = weatherEntity;

            aqiItem = aqiEntity;

            conditionItemList.clear();
            conditionItemList.addAll(conditionEntity);

            notifyDataSetChanged();
        }
}