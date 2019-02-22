package com.iaruchkin.deepbreath;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.iaruchkin.deepbreath.service.NetworkUtils;
import com.iaruchkin.deepbreath.service.NewsRequestService;

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

        registerReceiver(NetworkUtils.getInstance().getNetworkReceiver(),
                new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
        registerReceiver(NetworkUtils.getInstance().getCancelReceiver(),
                new IntentFilter());
        performsScheduledWord();
    }

    private static void performsScheduledWord(){
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(NewsRequestService.class, 3, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(NewsRequestService.WORK_TAG)
                .build();
        NetworkUtils.getInstance().getCancelReceiver().setWorkRequestId(workRequest.getId());
        WorkManager.getInstance()
                .enqueue(workRequest);
    }
}
