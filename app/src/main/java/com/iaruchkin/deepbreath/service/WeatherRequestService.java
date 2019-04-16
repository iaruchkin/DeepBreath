package com.iaruchkin.deepbreath.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.network.parsers.AqiApi;
import com.iaruchkin.deepbreath.network.dtos.AqiResponse;
import com.iaruchkin.deepbreath.ui.MainActivity;
import com.iaruchkin.deepbreath.utils.AqiUtils;
import com.iaruchkin.deepbreath.utils.PreferencesHelper;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.iaruchkin.deepbreath.service.NetworkUtils.CancelReceiver.ACTION_CANCEL;

public class WeatherRequestService extends Worker {
    private static final String TAG = WeatherRequestService.class.getName();
    public static final String WORK_TAG = "AqiData download";

    private static final String CHANNEL_ID = "CHANNEL_UPDATE_AQI";
    private static final int UPDATE_NOTIFICATION_ID = 3447;

    private NotificationManager notificationManager;

    private Disposable downloadDisposable;

    public WeatherRequestService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private void logError(Throwable throwable) {
        if (throwable instanceof IOException) {
            Log.e(TAG, "logError: " + throwable.getMessage());
        } else
            Log.e(TAG, "logError: stopped unexpectedly : \n" + throwable.getMessage());
    }

    @Override
    public void onStopped() {
        if (downloadDisposable != null && !downloadDisposable.isDisposed())
            downloadDisposable.dispose();
        super.onStopped();
    }

    @SuppressLint("StringFormatMatches")
    private void makeNotification(Integer aqi, boolean success) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        createNotificationChannel();

        Intent cancelIntent = new Intent(getApplicationContext(), NetworkUtils.CancelReceiver.class);
        cancelIntent.setAction(ACTION_CANCEL);

        PendingIntent onClickPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        String message = getApplicationContext().getString(AqiUtils.getPollutionLevel(aqi));

        NotificationCompat.Builder notificationBuilder;
        if (success)
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_factory_round)
                    .setContentTitle(String.format(Locale.getDefault(), getApplicationContext().getResources().getString(R.string.service_header), aqi))
                    .setContentText(String.format(Locale.getDefault(), getApplicationContext().getResources().getString(R.string.service_message),  message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(onClickPendingIntent)
                    .setAutoCancel(true);

        else
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_warning)
                    .setContentTitle("Download failed")
                    .setContentText("Error while downloading data")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        if (notificationManager == null)
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(UPDATE_NOTIFICATION_ID, notificationBuilder.build());
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        Log.i(TAG, "onStartCommand: service starting");

        String parameter = PreferencesHelper.getAqiParameter(getApplicationContext());

        downloadDisposable = NetworkUtils.getInstance().getOnlineNetwork()
                .timeout(1, TimeUnit.MINUTES)
                .flatMap(aLong -> updateAqi(parameter))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aqiEntities -> makeNotification(aqiEntities.getAqiData().getAqi(), true),
                        this::logError
                );

        Log.i(TAG, "onStartCommand: service stopped");
        return Result.SUCCESS;
    }

    private Single<AqiResponse> updateAqi(String parameter) {
        return AqiApi.getInstance()
                .aqiEndpoint()
                .get(parameter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().
                    getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}