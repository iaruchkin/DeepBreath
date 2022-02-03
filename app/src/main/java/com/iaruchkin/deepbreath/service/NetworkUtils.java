package com.iaruchkin.deepbreath.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;

import com.iaruchkin.deepbreath.core.application.App;

import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class NetworkUtils {

    private static NetworkUtils networkUtils;
    private NetworkReceiver networkReceiver = new NetworkReceiver();
    private CancelReceiver cancelReceiver = new CancelReceiver();

    public static Subject<Boolean> networkState = BehaviorSubject.createDefault(isNetworkAvailable());


    public static NetworkUtils getInstance() {
        synchronized (App.class) {
            if (networkUtils == null)
                synchronized (App.class) {
                    networkUtils = new NetworkUtils();
                }
            return networkUtils;
        }
    }

    //todo refactor
    public static boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) App.INSTANCE
//                .getApplicationContext()
//                .getSystemService(CONNECTIVITY_SERVICE);
//        if (connectivityManager == null)
//            return false;
//        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//        return info != null && info.isConnected();
        return true;
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

