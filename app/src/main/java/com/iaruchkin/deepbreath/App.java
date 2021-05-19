package com.iaruchkin.deepbreath;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.iaruchkin.deepbreath.common.AppPreferences;
import com.iaruchkin.deepbreath.service.NetworkUtils;
import com.iaruchkin.deepbreath.service.WeatherRequestService;

import java.util.concurrent.TimeUnit;

import static com.iaruchkin.deepbreath.service.WeatherRequestService.WORK_TAG;

public class App extends Application {

    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        if(AppPreferences.areNotificationsEnabled(INSTANCE.getApplicationContext())){

            registerReceiver(NetworkUtils.getInstance().getNetworkReceiver(),
                    new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
            registerReceiver(NetworkUtils.getInstance().getCancelReceiver(),
                    new IntentFilter());

            performsScheduledWork();
        }
    }

    public static void performsScheduledWork(){
        //todo modify
        int repeatInterval = 8; // In hours

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        if(AppPreferences.areNotificationsEnabled(INSTANCE.getApplicationContext())) {

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(WeatherRequestService.class
                    ,repeatInterval, TimeUnit.HOURS
                    ,20, TimeUnit.MINUTES
            )
                    .setConstraints(constraints)
                    .addTag(WORK_TAG)
                    .build();

            NetworkUtils.getInstance().getCancelReceiver().setWorkRequestId(workRequest.getId());
            WorkManager.getInstance(INSTANCE.getApplicationContext()).enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, workRequest);
        }
        else {
            WorkManager.getInstance(INSTANCE.getApplicationContext()).cancelAllWorkByTag(WORK_TAG);
        }
    }
}
