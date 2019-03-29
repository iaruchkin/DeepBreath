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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.AppConstants;
import com.iaruchkin.deepbreath.common.GpsUtils;
import com.iaruchkin.deepbreath.ui.fragments.AqiFragment;
import com.iaruchkin.deepbreath.ui.fragments.ForecastFragment;
import com.iaruchkin.deepbreath.ui.fragments.GroupieFragment;
import com.iaruchkin.deepbreath.ui.fragments.MessageFragmentListener;
import com.iaruchkin.deepbreath.ui.fragments.SettingsFragment;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener {

    public final static String WEATHER_LIST_TAG = "WEATHER_LIST";
    public final static String WEATHER_DETAILS_TAG = "WEATHER_DETAILS";
    public final static String SETTINGS_TAG = "SETTINGS";
    public final static String INTRO_TAG = "INTRO";

//    private String option = "forecast";
//    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
//    private StringBuilder stringBuilder;
//    private boolean isGPS = false;
//
//    private FusedLocationProviderClient mFusedLocationClient;
    private FragmentManager mFragmentManager;
    private ForecastFragment mForecastFragment;
    private AqiFragment mDetailsFragment;
    private GroupieFragment mGroupieFragment;
    private SettingsFragment mSettingsFragment;
    //    private IntroFragment mIntroFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Forecast);
        super.onCreate(savedInstanceState);

        init();
//        setupLocation();
        startForecast();

//        if (savedInstanceState == null){
//            if (Storage.needToShowIntro(this)) {
//                startIntro();
//            } else {
//                startForecast();
//            }
//        }
    }

//    private void startIntro(){
//        mIntroFragment = new IntroFragment();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_list, mIntroFragment)
//                .commit();
//    }

    private void startForecast() {
        mForecastFragment = new ForecastFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, mForecastFragment)
                .commit();
    }

//    private void startDetails(String idForecast, String idWeather, String idAqi, int viewType) {
//        mAqiFragment = AqiFragment.newInstance(idForecast, idWeather, idAqi, viewType);//todo set correct string messages
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_list, mDetailsFragment)
//                .addToBackStack(null)
//                .commit();
//    }

    private void startDetails(String idForecast, String idWeather, String idAqi, String idCondition, int viewType) {
        mGroupieFragment = GroupieFragment.newInstance(idForecast, idWeather, idAqi, idCondition, viewType);//todo set correct string messages
//        mGroupieFragment = new GroupieFragment();
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
    public void onActionClicked(String fragmentTag) {
        switch (fragmentTag) {
            case WEATHER_LIST_TAG:
                startForecast();
                break;
//            case WEATHER_DETAILS_TAG:
//                startDetails(message);
//                break;
            case SETTINGS_TAG:
                startSettings();
                break;
//            case INTRO_TAG:
//                startIntro();
//                break;
        }
    }

//    private void setupLocation() {
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10 * 1000); // 10 seconds
//        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
//
//        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
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
//                        wayLatitude = location.getLatitude();
//                        wayLongitude = location.getLongitude();
//                        Log.w("MAIN ACTIVITY 2", String.format(Locale.ENGLISH, "%s - %s", wayLatitude, wayLongitude));
//
//                        if (mFusedLocationClient != null) {
//                            mFusedLocationClient.removeLocationUpdates(locationCallback);
//                        }
//                    }
//                }
//            }
//        };
//
//            if (!isGPS) {
//                Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            getLocation();
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    AppConstants.LOCATION_REQUEST);
//
//        } else {
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
//                    if (location != null) {
//                        wayLatitude = location.getLatitude();
//                        wayLongitude = location.getLongitude();
//                        Log.w("MAIN ACTIVITY 1", String.format(Locale.ENGLISH, "%s - %s", wayLatitude, wayLongitude));
//
//                    } else {
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    }
//                });
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
//                        mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
//                            if (location != null) {
//                                wayLatitude = location.getLatitude();
//                                wayLongitude = location.getLongitude();
////                                txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
//                            } else {
//                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                            }
//                        });
//
//                } else {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == AppConstants.GPS_REQUEST) {
//                isGPS = true; // flag maintain before get location
//            }
//        }
//    }

}