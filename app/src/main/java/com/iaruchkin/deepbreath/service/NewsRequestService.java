package com.iaruchkin.deepbreath.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iaruchkin.deepbreath.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.disposables.Disposable;

import static com.iaruchkin.deepbreath.service.NetworkUtils.CancelReceiver.ACTION_CANCEL;

public class NewsRequestService extends Worker {
    private static final String TAG = NewsRequestService.class.getName();
    public static final String WORK_TAG = "News download";

    private static final String CHANNEL_ID = "CHANNEL_UPDATE_NEWS";
    private static final int UPDATE_NOTIFICATION_ID = 3447;

    private NotificationManager notificationManager;

    private Disposable downloadDisposable;

    public NewsRequestService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private void logError(Throwable throwable) {
        if (throwable instanceof IOException) {
            Log.e(TAG, "logError: " + throwable.getMessage());
        } else
            Log.e(TAG, "logError: stopped unexpectedly : \n" + throwable.getMessage());
        makeNotification(false);
    }

    @Override
    public void onStopped() {
        if (downloadDisposable != null && !downloadDisposable.isDisposed())
            downloadDisposable.dispose();
        super.onStopped();
    }

    private void makeNotification(boolean success) {
        Intent cancelIntent = new Intent(getApplicationContext(), NetworkUtils.CancelReceiver.class);
        cancelIntent.setAction(ACTION_CANCEL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, cancelIntent, 0);

        NotificationCompat.Builder notificationBuilder;
        if (success)
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.art_clouds)
                    .setContentTitle("News app")
                    .setContentText("Data succesfully downloaded to DB")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .addAction(R.drawable.art_clouds, getApplicationContext().getString(R.string.cancel_work), pendingIntent);

        else
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.vec_error)
                    .setContentTitle("Download failed")
                    .setContentText("Error while downloading news")
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
        downloadDisposable = NetworkUtils.getInstance().getOnlineNetwork()
                .timeout(1, TimeUnit.MINUTES)
                .subscribe(this::makeNotification, this::logError);
        Log.i(TAG, "onStartCommand: service stopped");
        return Result.SUCCESS;
    }
}