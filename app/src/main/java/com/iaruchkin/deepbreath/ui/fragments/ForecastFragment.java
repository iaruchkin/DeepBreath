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

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.presentation.presenter.ForecastPresenter;
import com.iaruchkin.deepbreath.presentation.view.ForecastView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConditionEntity;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;
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

//    //Location
//    private Location mLocation;
//    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
//    private boolean isGPS = false;
//    private FusedLocationProviderClient mFusedLocationClient;

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
//            if(isGPS) setupLocation();
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
                    , getString(R.string.github_link));
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
//        actionBar.setDisplayUseLogoEnabled(true);

        actionBar.setTitle(getResources().getString(R.string.app_name));
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

//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        } else {
//            final int columnsCount = getResources().getInteger(R.integer.landscape_news_columns_count);
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnsCount));
//        }
    }

    @Override
    public void onRefresh() {
//        loadData(true);
        listener.onActionClicked(WEATHER_LIST_TAG);
        listener.onActionClicked(GET_LOCATION);

    }

    private void setupUx() {
//        setupLocation();
        errorAction.setOnClickListener(v -> listener.onActionClicked(WEATHER_LIST_TAG));
//        loadData(false);
    }

    public void loadData() {
//        if(mLocation != null) {
//            mLocation.setLongitude(AppPreferences.getLocationCoordinates(getContext())[0]);
//            mLocation.setLatitude(AppPreferences.getLocationCoordinates(getContext())[1]);
//        }
//        forecastPresenter.loadData(force, AppPreferences.getLocation(getContext()));

    }

    @Override
    public void showData(@NonNull List<ForecastEntity> forecastEntity,
                         @NonNull List<WeatherEntity> weatherEntity,
                         @NonNull List<AqiEntity> aqiEntity,
                         @NonNull List<ConditionEntity> conditionEntity) {

        if(forecastEntity.size() != 0
                && weatherEntity.size() != 0
                && aqiEntity.size() != 0
                && conditionEntity.size() != 0) {
            mAdapter.setData(forecastEntity, weatherEntity.get(0), aqiEntity.get(0), conditionEntity); //todo throws java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
        }
        weatherItem = weatherEntity.get(0);
        aqiItem = aqiEntity.get(0);
    }

//    @Override
//    public void showWeatherData(@NonNull List<WeatherEntity> data) {
////        if (mAdapter != null) {
////            mAdapter.setWeatherItem(data.get(0));
////        }
//    }

//    @Override
//    public void showForecastData(List<ForecastEntity> data) {
////        if (mAdapter != null) {
////            mAdapter.setForecastItems(data);
////        }
//    }

//    @Override
//    public void showAqiData(@NonNull List<AqiEntity> data) {
////        if (mAdapter != null) {
////            mAdapter.setAqiItem(data.get(0));
////        }
//    }

//    @Override
//    public void showConditionData(@NonNull List<ConditionEntity> data) {
////        if (mAdapter != null) {
////            mAdapter.setConditionItems(data);
////        }
//    }

    @Override
    public void showState(@NonNull State state) {
        switch (state) {
            case HasData:
                mError.setVisibility(View.GONE);
//                mLoadingIndicator.setVisibility(View.GONE);

                mRecyclerView.setVisibility(View.VISIBLE);
                showRefresher(false);
                break;

            case HasNoData:
//                mLoadingIndicator.setVisibility(View.GONE);
//                mRecyclerView.setVisibility(View.GONE);

//                mError.setVisibility(View.VISIBLE);
                mRefresh.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);
                showRefresher(false);
                break;

            case NetworkError:
//                mLoadingIndicator.setVisibility(View.GONE);
                mRefresh.setVisibility(View.GONE);
                mError.setVisibility(View.GONE);
                showRefresher(false);
                break;

            case DbError:
                mRefresh.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);
                showRefresher(false);
                break;

//            case ServerError:
////                mLoadingIndicator.setVisibility(View.GONE);
////                mRecyclerView.setVisibility(View.GONE);
//                mRefresh.setVisibility(View.GONE);
//                mError.setVisibility(View.VISIBLE);
//                showRefresher(false);
//                break;

            case Loading:
                mError.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);

//                mLoadingIndicator.setVisibility(View.VISIBLE);

                showRefresher(true);
                break;

            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    public void showRefresher(boolean show) {
        mRefresh.setRefreshing(show);
    }

    private void findViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.idRecyclerView);
        mError = view.findViewById(R.id.error_layout);
        errorAction = view.findViewById(R.id.action_button);
        mRefresh = view.findViewById(R.id.refresh);
    }

//    private void saveLocation(Location location){
//        AppPreferences.setLocationDetails(getContext(), location.getLatitude(), location.getLongitude());
//    }
//
//    private void setupLocation() {
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10 * 1000); // 10 seconds
//        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
//
//        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
//            @Override
//            public void gpsStatus(boolean isGPSEnable) {
//                // turn on GPS
//                isGPS = isGPSEnable;
//            }
//        });
//
//        locationCallback = new LocationCallback() {
//
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        saveLocation(location);
//                        loadData(true);
//                        Log.w("GPS onLocationResult ", location.toString());
//
//                        if (mFusedLocationClient != null) {
//                            mFusedLocationClient.removeLocationUpdates(locationCallback);
//                        }
//                    }
//                }
//            }
//        };
//
//        if (!isGPS) {
//            Toast.makeText(getContext(), "Please turn on GPS", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        getLocation();
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    AppConstants.LOCATION_REQUEST);
//
//        } else {
//            mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) getContext(), location -> {
//                if (location != null) {
//                    saveLocation(location);
//                    loadData(true);
//                    Log.w("GPS getLocation ", location.toString());
//                } else {
//                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                }
//            });
//        }
//    }

//    @SuppressLint("MissingPermission")
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1000: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) getContext(), location -> {
//                        if (location != null) {
//                            saveLocation(location);
//                            loadData(true);
//                            Log.w("GPS missingPermission ", location.toString());
//                        } else {
//                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                    loadData(true);
//                }
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == AppConstants.GPS_REQUEST) {
//                isGPS = true; // flag maintain before get location
//            }
//        }
//    }
}