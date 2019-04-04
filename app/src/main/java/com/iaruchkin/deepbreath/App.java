package com.iaruchkin.deepbreath;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.iaruchkin.deepbreath.common.AppPreferences;
import com.iaruchkin.deepbreath.service.NetworkUtils;
import com.iaruchkin.deepbreath.service.WeatherRequestService;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

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

    private static void performsScheduledWork(){
        Constraints constraints = new Constraints.Builder()
//                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        WorkRequest workRequest = new PeriodicWorkRequest.Builder(WeatherRequestService.class,
                15, TimeUnit.MINUTES)//todo set correct repeat interval and ability to disable notifications
                .setConstraints(constraints)
                .addTag(WeatherRequestService.WORK_TAG)
                .build();

        if(AppPreferences.areNotificationsEnabled(INSTANCE.getApplicationContext())) {
            NetworkUtils.getInstance().getCancelReceiver().setWorkRequestId(workRequest.getId());
            WorkManager.getInstance()
                    .enqueue(workRequest);
        }
        else {
            WorkManager.getInstance().cancelWorkById(workRequest.getId());
        }


//        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(NewsUpdateWorker.class,
//                15, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
//                .addTag("UPDATE")
//                .setConstraints(constraints)
//                .build();

//        NetworkUtils.getInstance().getCancelReceiver().setWorkRequestId(work.getId());//todo выяснить зачем это
//        WorkManager.getInstance()
//                .enqueueUniquePeriodicWork("UPDATE", ExistingPeriodicWorkPolicy.REPLACE, work);
    }
}
