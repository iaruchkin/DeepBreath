package com.iaruchkin.deepbreath.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.iaruchkin.deepbreath.network.AqiResponse;
import com.iaruchkin.deepbreath.network.NetworkSilngleton;
import com.iaruchkin.deepbreath.presentation.presenter.WeatherListPresenter;
import com.iaruchkin.deepbreath.presentation.view.WeatherListView;
import com.iaruchkin.deepbreath.room.WeatherEntity;
import com.iaruchkin.deepbreath.ui.adapter.WeatherItemAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_DETAILS_TAG;
import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_LIST_TAG;

public class WeatherListFragment extends MvpAppCompatFragment implements WeatherItemAdapter.WeatherAdapterOnClickHandler, WeatherListView {

    private static final int LAYOUT = R.layout.weather_list_layout;
    private MessageFragmentListener listener;

    @InjectPresenter
    WeatherListPresenter weatherListPresenter;

    @ProvidePresenter
    WeatherListPresenter provideWeatherListPresenter() {
        return new WeatherListPresenter(NetworkSilngleton.getInstance());
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
    @Nullable
    private FloatingActionButton mUpdate;
//    @Nullable
//    private Toolbar toolbar;

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
//        setupToolbar();
        setupOrientation(mRecyclerView);
        setupRecyclerViewAdapter();
    }

//    private void setupToolbar() {
//        setHasOptionsMenu(true);
//        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//    }

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

        mUpdate.setOnClickListener(v -> forceLoadData(getLocation()));
        errorAction.setOnClickListener(v -> loadData(getLocation()));

        loadData(getLocation());

    }

    private String getLocation() {
        return "here";
    }

    @Override
    public void onClick(WeatherEntity weatherItem) {
        listener.onActionClicked(WEATHER_DETAILS_TAG, weatherItem.getAqi());
    }


    public void loadData(String category) {

        weatherListPresenter.loadData(category);

    }

    public void forceLoadData(String category) {

        weatherListPresenter.forceLoadData(category);

    }

    @Override
    public void showData(List<WeatherEntity> data) {
        if (mAdapter != null) {
            mAdapter.replaceItems(data);
        }
    }

    @Override
    public void setData(@NonNull AqiResponse data) {
        if (mAdapter != null) {
            mAdapter.setData(data);
        }
    }

    @Override
    public void showState(@NonNull State state) {
        switch (state) {
            case HasData:
                mError.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.GONE);

                mRecyclerView.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                mLoadingIndicator.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mError.setVisibility(View.VISIBLE);
                break;

            case NetworkError:
                mLoadingIndicator.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mError.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                mLoadingIndicator.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mError.setVisibility(View.VISIBLE);
                break;

            case Loading:
                mError.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mLoadingIndicator.setVisibility(View.VISIBLE);
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    private void findViews(View view) {
//        toolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.idRecyclerView);
        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        mError = view.findViewById(R.id.error_layout);
        errorAction = view.findViewById(R.id.action_button);
        mUpdate = view.findViewById(R.id.floatingActionButton);
    }
}