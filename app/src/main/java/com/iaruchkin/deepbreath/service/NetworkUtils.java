package com.iaruchkin.deepbreath.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.iaruchkin.deepbreath.App;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtils {

    private static NetworkUtils networkUtils;
    private NetworkReceiver networkReceiver = new NetworkReceiver();
    private CancelReceiver cancelReceiver = new CancelReceiver();
    private Subject<Boolean> networkState = BehaviorSubject.createDefault(isNetworkAvailable());


    public static NetworkUtils getInstance() {
        synchronized (App.class) {
            if (networkUtils == null)
                synchronized (App.class) {
                    networkUtils = new NetworkUtils();
                }
            return networkUtils;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.INSTANCE
                .getApplicationContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public NetworkReceiver getNetworkReceiver() {
        return networkReceiver;
    }

    public Single<Boolean> getOnlineNetwork() {
        return networkState.subscribeOn(Schedulers.io())
                .filter(online -> online)
                .firstOrError();
    }

    public CancelReceiver getCancelReceiver() {
        return cancelReceiver;
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            networkState.onNext(isNetworkAvailable());
        }
    }

    public class CancelReceiver extends BroadcastReceiver {

        public static final String ACTION_CANCEL = "Cancel downloading";
        private final String TAG = CancelReceiver.class.getName();
        private UUID workRequestId;

        @Override
        public void onReceive(Context context, Intent intent) {
            WorkManager.getInstance().cancelWorkById(workRequestId);
            Log.i(TAG, "onReceive: canceled service ID: " + workRequestId);
        }

        public void setWorkRequestId(@NonNull UUID workRequestId) {
            this.workRequestId = workRequestId;
        }
    }
}