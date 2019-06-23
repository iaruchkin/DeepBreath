package com.iaruchkin.deepbreath;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.iaruchkin.deepbreath.common.AppPreferences;
import com.iaruchkin.deepbreath.service.NetworkUtils;
import com.iaruchkin.deepbreath.service.WeatherRequestService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
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
        int repeatInterval = 24; // In hours

        long flexTime = calculateFlex(hourOfTheDay, repeatInterval);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        if(AppPreferences.areNotificationsEnabled(INSTANCE.getApplicationContext())) {


            OneTimeWorkRequest firstWork = new OneTimeWorkRequest.Builder(WeatherRequestService.class)
                    .setInitialDelay(calculateDelay(hourOfTheDay), TimeUnit.MILLISECONDS)
                    .addTag(WORK_TAG)
                    .build();


            WorkRequest workRequest = new PeriodicWorkRequest.Builder(WeatherRequestService.class
                    ,repeatInterval, TimeUnit.HOURS
                    ,20, TimeUnit.MINUTES
//                    ,flexTime, TimeUnit.MILLISECONDS
            )
                    .setConstraints(constraints)
                    .addTag(WORK_TAG)
                    .build();

//            List<WorkRequest> list = new ArrayList<>();
//            list.add(firstWork);
//            list.add(workRequest);

            NetworkUtils.getInstance().getCancelReceiver().setWorkRequestId(workRequest.getId());
            WorkManager.getInstance().beginWith(firstWork).enqueue();
//            WorkManager.getInstance().enqueue(workRequest);
//            WorkManager.getInstance().enqueue(list);
            WorkManager.getInstance().enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, (PeriodicWorkRequest) workRequest);
//            WorkManager.getInstance().beginUniqueWork(WORK_TAG, ExistingWorkPolicy.REPLACE, firstWork).enqueue();
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

    private static long calculateDelay(int hourOfTheDay) {

        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        long delta = (cal1.getTimeInMillis() - cal2.getTimeInMillis());

        return delta;
    }
}
