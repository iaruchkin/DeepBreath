package com.iaruchkin.deepbreath;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.iaruchkin.deepbreath.common.AppPreferences;
import com.iaruchkin.deepbreath.service.NetworkUtils;
import com.iaruchkin.deepbreath.service.WeatherRequestService;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

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
        int hourOfTheDay = 8; // When to run the job
        int repeatInterval = 1; // In days

        long flexTime = calculateFlex(hourOfTheDay, repeatInterval);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        if(AppPreferences.areNotificationsEnabled(INSTANCE.getApplicationContext())) {

            WorkRequest workRequest = new PeriodicWorkRequest.Builder(WeatherRequestService.class
                    ,repeatInterval, TimeUnit.DAYS
                    ,flexTime, TimeUnit.MILLISECONDS)
                    .setConstraints(constraints)
                    .addTag(WORK_TAG)
                    .build();

                NetworkUtils.getInstance().getCancelReceiver().setWorkRequestId(workRequest.getId());
                WorkManager.getInstance()
                        .enqueue(workRequest);
        }
        else {
            WorkManager.getInstance().cancelAllWorkByTag(WORK_TAG);
        }
    }

    private static long calculateFlex(int hourOfTheDay, int periodInDays) {

        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        if (cal2.getTimeInMillis() < cal1.getTimeInMillis()) {
            // Add the worker periodicity.
            cal2.setTimeInMillis(cal2.getTimeInMillis() + TimeUnit.DAYS.toMillis(periodInDays));
        }

        long delta = (cal2.getTimeInMillis() - cal1.getTimeInMillis());

        return ((delta > PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) ? delta
                : PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS);
    }
}
