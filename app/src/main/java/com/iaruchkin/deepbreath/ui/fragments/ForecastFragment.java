package com.iaruchkin.deepbreath.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.presentation.presenter.ForecastPresenter;
import com.iaruchkin.deepbreath.presentation.view.ForecastView;
import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;
import com.iaruchkin.deepbreath.ui.adapter.ForecastAdapter;
import com.iaruchkin.deepbreath.utils.AqiUtils;
import com.iaruchkin.deepbreath.utils.StringUtils;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.disposables.CompositeDisposable;

import static com.iaruchkin.deepbreath.ui.MainActivity.ABOUT_TAG;
import static com.iaruchkin.deepbreath.ui.MainActivity.GET_LOCATION;
import static com.iaruchkin.deepbreath.ui.MainActivity.SETTINGS_TAG;
import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_LIST_TAG;

public class ForecastFragment extends MvpAppCompatFragment implements ForecastAdapter.ForecastAdapterOnClickHandler,
        ForecastView, SwipeRefreshLayout.OnRefreshListener {

    private static final int LAYOUT = R.layout.fragment_weather_list;
    private MessageFragmentListener listener;
    private WeatherEntity weatherItem;
    private AqiEntity aqiItem;

    @InjectPresenter
    ForecastPresenter forecastPresenter;

    @Nullable
    private ForecastAdapter mAdapter;
    @Nullable
    private RecyclerView mRecyclerView;
    @Nullable
    private View mError;
    @Nullable
    private Button errorAction;
    @Nullable
    private SwipeRefreshLayout mRefresh;
    @Nullable
    private Toolbar toolbar;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @ProvidePresenter
    ForecastPresenter providePresenter() {
        Boolean idGPS = getArguments() != null ? getArguments().getBoolean("GEO", false) : null;

        return new ForecastPresenter(idGPS);
    }

    public static ForecastFragment newInstance(Boolean isGPS){
        ForecastFragment fragmentForecast = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("GEO", isGPS);

        fragmentForecast.setArguments(bundle);

        return fragmentForecast;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        Log.i(WEATHER_LIST_TAG, "OnCreateView executed on thread:" + Thread.currentThread().getName());

        View view = inflater.inflate(LAYOUT, container, false);

        setupUi(view);
        setupUx();

        return view;
    }

    @Override
    public void onStart() {
        Log.i(WEATHER_LIST_TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        mRecyclerView = null;
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

    private void setupUi(View view) {
        findViews(view);
        setupToolbar();
        setupOrientation();
        setupRecyclerViewAdapter();

        setHomeButton(view);

        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.forecast, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if (listener != null) {
                    listener.onActionClicked(SETTINGS_TAG);
                }
                return true;
            case R.id.action_about:
                if (listener != null) {
                    listener.onActionClicked(ABOUT_TAG);
                }
                return true;
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StringFormatMatches")
    private void share(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

        String  message = getString(R.string.github_link);

        if(aqiItem != null && weatherItem != null) {
            message = String.format(Locale.getDefault(), getString(R.string.share_message)
                    , StringUtils.transliterateLatToRus(weatherItem.getLocation(), weatherItem.getCountry())
                    , getResources().getString(AqiUtils.getPollutionLevel(aqiItem.getAqi()))
                    , getString(R.string.google_play_link));
        }

        i.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(i, getString(R.string.share)));
    }

    @Override
    public void onClickList(ForecastEntity forecastItem, WeatherEntity weatherEntity, AqiEntity aqiEntity, ConditionEntity conditionEntity, int viewType) {
        listener.onListClicked(forecastItem.getId(), weatherEntity.getId(), aqiEntity.getId(), conditionEntity.getId(), viewType);
    }

    private void setupToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.app_name));

        //if need to show logo at toolbar
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setLogo(getResources().getDrawable(R.drawable.ic_action_name));
    }

    private void setHomeButton(View view) {
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ActionBar supportActionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupRecyclerViewAdapter(){
        mAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupOrientation() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //todo fix in future versions
        //ability to rotate the screen
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        } else {
//            final int columnsCount = getResources().getInteger(R.integer.landscape_news_columns_count);
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnsCount));
//        }
    }

    @Override
    public void onRefresh() {
        listener.onActionClicked(WEATHER_LIST_TAG);
        listener.onActionClicked(GET_LOCATION);
    }

    private void setupUx() {
        errorAction.setOnClickListener(v -> listener.onActionClicked(WEATHER_LIST_TAG));
    }

    @Override
    public void showWeather(@NonNull List<ForecastEntity> forecastEntity,
                         @NonNull List<WeatherEntity> weatherEntity,
                         @NonNull List<ConditionEntity> conditionEntity) {

        if(forecastEntity.size() != 0
                && weatherEntity.size() != 0
                && conditionEntity.size() != 0) {
            mAdapter.setWeather(forecastEntity, weatherEntity.get(0), conditionEntity);
        }
        weatherItem = weatherEntity.get(0);
    }

    @Override
    public void showAqi(@NonNull List<AqiEntity> aqiEntity) {

        if(aqiEntity.size() != 0) {
            mAdapter.setAqi(aqiEntity.get(0));
        }
        aqiItem = aqiEntity.get(0);
    }

    @Override
    public void showState(@NonNull State state) {
        switch (state) {
            case HasData:
                mRefresh.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mError.setVisibility(View.GONE);
                showRefresher(false);
                break;

            case HasNoData:
                mRefresh.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);
                showRefresher(false);
                break;

            case NetworkError:
                mRefresh.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);
                showRefresher(false);
                break;

            case DbError:
                mRefresh.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);
                showRefresher(false);
                break;

          case Loading:
                mRefresh.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mError.setVisibility(View.GONE);

              showRefresher(true);
                break;

            case LoadingAqi:
                mRefresh.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mError.setVisibility(View.GONE);

                showRefresher(false);
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    private void showRefresher(boolean show) {
        mRefresh.setRefreshing(show);
    }

    private void findViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.idRecyclerView);
        mError = view.findViewById(R.id.error_layout);
        errorAction = view.findViewById(R.id.action_button);
        mRefresh = view.findViewById(R.id.refresh);
    }
}