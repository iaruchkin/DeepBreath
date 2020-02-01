package com.iaruchkin.deepbreath.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.AppConstants;
import com.iaruchkin.deepbreath.common.AppPreferences;
import com.iaruchkin.deepbreath.common.GpsUtils;
import com.iaruchkin.deepbreath.ui.fragments.AboutFragment;
import com.iaruchkin.deepbreath.ui.fragments.FindFragment;
import com.iaruchkin.deepbreath.ui.fragments.ForecastFragment;
import com.iaruchkin.deepbreath.ui.fragments.GroupieFragment;
import com.iaruchkin.deepbreath.ui.fragments.MessageFragmentListener;
import com.iaruchkin.deepbreath.ui.fragments.SettingsFragment;
import com.iaruchkin.deepbreath.ui.fragments.intro.IntroFragment;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener {

    public final static String WEATHER_LIST_TAG = "WEATHER_LIST";
    public final static String WEATHER_DETAILS_TAG = "WEATHER_DETAILS";
    public final static String SETTINGS_TAG = "SETTINGS";
    public final static String INTRO_TAG = "INTRO";
    public final static String ABOUT_TAG = "ABOUT";
    public final static String FIND_TAG = "FIND";
    public final static String GET_LOCATION = "LOCATION";

    public boolean firstLaunch = false;

    private FragmentManager mFragmentManager;
    private ForecastFragment mForecastFragment;
    private AboutFragment mAboutFragment;
    private FindFragment mFindFragment;
    private GroupieFragment mGroupieFragment;
    private SettingsFragment mSettingsFragment;
    private IntroFragment mIntroFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Forecast);
        super.onCreate(savedInstanceState);

        init();

        if (savedInstanceState == null){
            firstLaunch = AppPreferences.needToShowIntro(this);
            if (firstLaunch) {
                startIntro();
            } else {
                setupLocation();
                startForecast();
            }
        }
    }

    private void startIntro(){
        mIntroFragment = new IntroFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mIntroFragment)
                .commit();
    }

    private void startAbout(){
        mAboutFragment = new AboutFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mAboutFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startFind() {
        mFindFragment = new FindFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mFindFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startForecast() {
        mForecastFragment = ForecastFragment.newInstance(isGPS);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mForecastFragment)
                .commitAllowingStateLoss();//todo fix in future
    }

    private void startDetails(String idForecast, String idWeather, String idAqi, String idCondition, int viewType) {
        mGroupieFragment = GroupieFragment.newInstance(idForecast, idWeather, idAqi, idCondition, viewType);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mGroupieFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startSettings() {
        mSettingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mSettingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init() {
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onListClicked(String idF, String idW, String idA, String idC, int viewType) {
        startDetails(idF, idW, idA, idC, viewType);
    }

    @Override
    public void onActionClicked(String tag) {
        switch (tag) {
            case WEATHER_LIST_TAG:
                startForecast();
                break;
            case SETTINGS_TAG:
                startSettings();
                break;
            case ABOUT_TAG:
                startAbout();
                break;
            case FIND_TAG:
                startFind();
                break;
            case INTRO_TAG:
                startIntro();
                break;
            case GET_LOCATION:
                setupLocation();
                break;
        }
    }

    /**here get location and ask permissions
     *
     */
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isGPS = false;
    private FusedLocationProviderClient mFusedLocationClient;

    private void saveLocation(Location location){
        AppPreferences.setLocationDetails(this, location.getLatitude(), location.getLongitude());
    }

    private void resetLocation(){
        AppPreferences.resetLocationCoordinates(this);
    }

    private void setupLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            Log.w("GPS isGPSEnable: ", String.valueOf(isGPSEnable));
            isGPS = isGPSEnable;
        });

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        saveLocation(location);

                            if (!firstLaunch) startForecast();

                            Log.w("GPS setupLocation", location.toString());

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

            if (!isGPS) {
                Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
//                resetLocation();
                Log.w("GPS isGPS :", "not enabled");
                return;
            }

            getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                    if (location != null) {
                        saveLocation(location);
                        if (!firstLaunch) startForecast();
                        Log.w("GPS getLocation", location.toString());
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                            if (location != null) {
                                saveLocation(location);
                                Log.w("GPS missingPermission", location.toString());
                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        });

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                    resetLocation();
                    Log.w("GPS missingPermission :", "Permission denied");
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
                setupLocation();
            }
        }
    }
}