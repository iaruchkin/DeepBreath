package com.iaruchkin.deepbreath.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiApi;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.presentation.presenter.WeatherListPresenter;
import com.iaruchkin.deepbreath.presentation.view.WeatherListView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;
import com.iaruchkin.deepbreath.ui.adapter.WeatherItemAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.disposables.CompositeDisposable;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import static com.iaruchkin.deepbreath.ui.MainActivity.SETTINGS_TAG;
import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_DETAILS_TAG;
import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_LIST_TAG;

public class WeatherListFragment extends MvpAppCompatFragment implements WeatherItemAdapter.WeatherAdapterOnClickHandler,
                                                                        WeatherListView,
                                                                        SwipeRefreshLayout.OnRefreshListener {

    private static final int LAYOUT = R.layout.layout_weather_list;
    private MessageFragmentListener listener;

    @InjectPresenter
    WeatherListPresenter weatherListPresenter;

    @ProvidePresenter
    WeatherListPresenter provideWeatherListPresenter() {
        return new WeatherListPresenter(WeatherApi.getInstance(), AqiApi.getInstance());
    }

    @Nullable
    private WeatherItemAdapter mAdapter;
    @Nullable
    private RecyclerView mRecyclerView;
    @Nullable
    private ProgressBar mLoadingIndicator;
    @Nullable
    private View mError;
    @Nullable
    private Button errorAction;
//    @Nullable
//    private FloatingActionButton mUpdate;
    @Nullable
    private SwipeRefreshLayout mRefresh;

//    @Nullable
//    private Toolbar toolbar;
    private CollapsingToolbarLayout mToolbar;


    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        setupOrientation(mRecyclerView);
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
                    listener.onActionClicked(SETTINGS_TAG, "settings");
                }
                return true;
            case R.id.action_map:
                if (listener != null) {
                    listener.onActionClicked(SETTINGS_TAG, "city");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {//todo привести в порядок, сейчас работает через стили и манифест
        setHasOptionsMenu(true);
//        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);

        actionBar.setLogo(getResources().getDrawable(R.drawable.ic_logo));
    }

    private void setHomeButton(View view) {
//        final Toolbar toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ActionBar supportActionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
    private void setupRecyclerViewAdapter(){
        mAdapter = new WeatherItemAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupOrientation(RecyclerView recyclerView) {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            final int columnsCount = getResources().getInteger(R.integer.landscape_news_columns_count);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnsCount));
        }
    }

    private void setupUx() {

//        mUpdate.setOnClickListener(v -> forceLoadData());
        errorAction.setOnClickListener(v -> loadData());

        loadData();

    }

    private String getLocation() {
        return "forecast";
    }

    @Override
    public void onClick(WeatherEntity weatherItem) {
        listener.onActionClicked(WEATHER_DETAILS_TAG, weatherItem.getTemperature().toString());//todo передать нужное значение
    }


    public void loadData() {

        weatherListPresenter.loadData();

    }

    public void forceLoadData() {

        weatherListPresenter.forceLoadData();

    }

    @Override
    public void showWeatherData(List<WeatherEntity> data) {
        if (mAdapter != null) {
            mAdapter.replaceItems(data);
        }
    }

    @Override
    public void showAqiData(@NonNull List<AqiEntity> data) {
        if (mAdapter != null) {
            mAdapter.setAqiItem(data.get(0));
        }
    }

    @Override
    public void showState(@NonNull State state) {
        switch (state) {
            case HasData:
                mError.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.GONE);

                mRecyclerView.setVisibility(View.VISIBLE);

                showRefresher(false);
                break;

            case HasNoData:
                mLoadingIndicator.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mError.setVisibility(View.VISIBLE);

                showRefresher(false);
                break;

            case NetworkError:
                mLoadingIndicator.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mError.setVisibility(View.VISIBLE);

                showRefresher(false);
                break;

            case ServerError:
                mLoadingIndicator.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mError.setVisibility(View.VISIBLE);

                showRefresher(false);
                break;

            case Loading:
                mError.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

//                mLoadingIndicator.setVisibility(View.VISIBLE);

                showRefresher(true);
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    @Override
    public void onRefresh() {
        weatherListPresenter.forceLoadData();
    }

//    @Override
    public void showRefresher(boolean show) {
        mRefresh.setRefreshing(show);
    }

    private void findViews(View view) {
//        toolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.idRecyclerView);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        mError = view.findViewById(R.id.error_layout);
        errorAction = view.findViewById(R.id.action_button);
//        mUpdate = view.findViewById(R.id.floatingActionButton);
        mRefresh = view.findViewById(R.id.refresh);

    }
}