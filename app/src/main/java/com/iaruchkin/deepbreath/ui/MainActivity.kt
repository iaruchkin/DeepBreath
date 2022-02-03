package com.iaruchkin.deepbreath.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.AppConstants
import com.iaruchkin.deepbreath.common.AppPreferences
import com.iaruchkin.deepbreath.common.GpsUtils
import com.iaruchkin.deepbreath.ui.fragments.*
import com.iaruchkin.deepbreath.ui.fragments.ForecastFragment.Companion.newInstance
import com.iaruchkin.deepbreath.ui.fragments.intro.IntroFragment
import dagger.hilt.android.AndroidEntryPoint

const val WEATHER_LIST_TAG = "WEATHER_LIST"
const val SEARCH_LIST_TAG = "SEARCH_LIST"
const val WEATHER_DETAILS_TAG = "WEATHER_DETAILS"
const val SETTINGS_TAG = "SETTINGS"
const val INTRO_TAG = "INTRO"
const val ABOUT_TAG = "ABOUT"
const val FIND_TAG = "FIND"
const val GET_LOCATION = "LOCATION"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MessageFragmentListener {
    var firstLaunch = false
    private var mForecastFragment: ForecastFragment? = null
    private var mForecastSearchFragment: ForecastFragment? = null
    private var mAboutFragment: AboutFragment? = null
    private var mFindFragment: FindFragment? = null
//    private var mGroupieFragment: DetailFragment? = null
    private var mSettingsFragment: SettingsFragment? = null
    private var mIntroFragment: IntroFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Forecast)
        super.onCreate(savedInstanceState)
        init()
        if (savedInstanceState == null) {
            firstLaunch = AppPreferences.needToShowIntro(this)
            if (firstLaunch) {
                startIntro()
            } else {
                setupLocation()
                startForecast()
            }
        }
    }

    private fun startIntro() {
        mIntroFragment = IntroFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_list, mIntroFragment!!)
                .commit()
    }

    private fun startAbout() {
        mAboutFragment = AboutFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_list, mAboutFragment!!)
                .addToBackStack(null)
                .commit()
    }

    private fun startFind() {
        mFindFragment = FindFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_list, mFindFragment!!)
                .addToBackStack(null)
                .commit()
    }

    private fun startForecast() {
        if (mForecastFragment == null) mForecastFragment = newInstance(isGPS)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_list, mForecastFragment!!)
                .commitAllowingStateLoss()
    }

    private fun startForecast(location: Location) {
        mForecastSearchFragment = newInstance(false, true, location)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_list, mForecastSearchFragment!!)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

//    private fun startDetails(idForecast: String?, idWeather: String?, idAqi: String?, idCondition: String?, viewType: Int) {
//        mGroupieFragment = newInstance(idForecast!!, idWeather!!, idAqi!!, idCondition!!, viewType)
//        supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.frame_list, mGroupieFragment!!)
//                .addToBackStack(null)
//                .commit()
//    }

    private fun startSettings() {
        if (mSettingsFragment == null) mSettingsFragment = SettingsFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_list, mSettingsFragment!!)
                .addToBackStack(null)
                .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun init() {
        setContentView(R.layout.activity_main)
    }

    override fun onListClicked(idF: String?, idW: String?, idA: String?, idC: String?, viewType: Int) {
//        startDetails(idF, idW, idA, idC, viewType)
    }

    override fun onStationInfoShow(location: Location){
        startForecast(location)
    }

    override fun onActionClicked(tag: String?) {
        when (tag) {
            WEATHER_LIST_TAG -> startForecast()
//            SEARCH_LIST_TAG -> startForecast()
            SETTINGS_TAG -> startSettings()
            ABOUT_TAG -> startAbout()
            FIND_TAG -> startFind()
            INTRO_TAG -> startIntro()
            GET_LOCATION -> setupLocation()
        }
    }

    /**here get location and ask permissions
     *
     */
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var isGPS = false
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private fun saveLocation(location: Location) {
        AppPreferences.setLocationDetails(this, location)
    }

    private fun resetLocation() {
        AppPreferences.resetLocationCoordinates(this)
    }

    private fun setupLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create()?.apply {
            interval = 10_000
            fastestInterval = 5_000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        GpsUtils(this).turnGPSOn { isGPSEnable: Boolean ->
            // turn on GPS
            Log.w("GPS isGPSEnable: ", isGPSEnable.toString())
            isGPS = isGPSEnable
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        saveLocation(location)
                        if (!firstLaunch) {
//                            startForecast() TODO check!!!!!test
                            mForecastFragment?.update()
                        }
                        Log.w("GPS setupLocation", location.toString())
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
        }
        if (!isGPS) {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show()
            //                resetLocation();
            Log.w("GPS isGPS :", "not enabled")
            return
        }
        getLocation()
    }

    private fun getLocation() {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        AppConstants.LOCATION_REQUEST)
            } else {
                mFusedLocationClient!!.lastLocation.addOnSuccessListener(this@MainActivity) { location: Location? ->
                    if (location != null) {
                        saveLocation(location)
                        if (!firstLaunch) {
//                            startForecast() TODO check!!!!!test
                            mForecastFragment?.update()
                        }
                        Log.i("GPS getLocation", location.toString())
                    } else {
                        mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
                    }
                }
            }
        }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient!!.lastLocation.addOnSuccessListener(this@MainActivity) { location: Location? ->
                        if (location != null) {
                            saveLocation(location)
                            Log.w("GPS missingPermission", location.toString())
                        } else {
                            mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
                        }
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    //                    resetLocation();
                    Log.w("GPS missingPermission :", "Permission denied")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
                setupLocation()
            }
        }
    }
}