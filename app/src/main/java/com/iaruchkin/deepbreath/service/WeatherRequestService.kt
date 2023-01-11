package com.iaruchkin.deepbreath.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.network.dtos.AqiAvResponse
import com.iaruchkin.deepbreath.network.dtos.AqiResponse
import com.iaruchkin.deepbreath.network.parsers.AqiApi
import com.iaruchkin.deepbreath.network.parsers.AqiAvApi
import com.iaruchkin.deepbreath.screens.MainActivity
import com.iaruchkin.deepbreath.utils.AqiUtils
import com.iaruchkin.deepbreath.utils.LocationUtils
import com.iaruchkin.deepbreath.utils.PreferencesHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherRequestService(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private var notificationManager: NotificationManager? = null
    private var downloadDisposable: Disposable? = null
    private fun logError(throwable: Throwable) {
        if (throwable is IOException) {
            Log.e(TAG, "logError: " + throwable.message)
            handleError()
        } else Log.e(TAG, """
 logError: stopped unexpectedly : 
     ${throwable.message}
     """.trimIndent())
        handleError()
    }

    override fun onStopped() {
        if (downloadDisposable != null && !downloadDisposable!!.isDisposed) downloadDisposable!!.dispose()
        super.onStopped()
    }

    @SuppressLint("StringFormatMatches")
    private fun makeNotification(aqi: Int, success: Boolean) {
        val intent = Intent(applicationContext, MainActivity::class.java)

        val onClickPendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

        val message = applicationContext.getString(AqiUtils.getPollutionLevel(aqi))
        val notificationBuilder: NotificationCompat.Builder
        if (success) {
            notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_factory_round)
                    .setContentTitle(String.format(Locale.getDefault(), applicationContext.resources.getString(R.string.service_header), aqi))
                    .setContentText(String.format(Locale.getDefault(), applicationContext.resources.getString(R.string.service_message), message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(onClickPendingIntent)
                    .setAutoCancel(true)

            if (notificationManager == null) notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.notify(UPDATE_NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    override fun doWork(): Result {
        Log.i(TAG, "onStartCommand: service starting")
        createNotificationChannel()
        Log.i(TAG, "onStartCommand: service stopped")
        return Result.success()
    }

    fun handleError(): Result {
        return Result.retry()
    }

    private val aqi: Unit
        private get() {
            val parameter = PreferencesHelper.getAqiParameter(applicationContext)
            downloadDisposable = NetworkUtils.getInstance().onlineNetwork
                    .timeout(1, TimeUnit.MINUTES)
                    .flatMap { aLong: Boolean? -> updateAqiCn(parameter) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { aqiEntities: AqiResponse ->
                                val isValid = LocationUtils.locationIsValid(aqiEntities.aqiData.city.getCoordinates(), applicationContext)
                                if (!isValid) aqiAv else makeNotification(aqiEntities.aqiData.aqi, true)
                            }) { throwable: Throwable -> logError(throwable) }
        }
    private val aqiAv: Unit
        private get() {
            downloadDisposable = NetworkUtils.getInstance().onlineNetwork
                    .timeout(1, TimeUnit.MINUTES)
                    .flatMap { aLong: Boolean? -> updateAqiAv() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { aqiEntities: AqiAvResponse -> makeNotification(aqiEntities.aqiAvData.current.pollution.aqicn, true) }) { throwable: Throwable -> logError(throwable) }
        }

    private fun updateAqiCn(parameter: String): Single<AqiResponse> {
        return AqiApi.getInstance()
                .aqiEndpoint()[parameter]
    }

    private fun updateAqiAv(): Single<AqiAvResponse> {
        return AqiAvApi.getInstance()
                .aqiAvEndpoint()
                .get()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Deep breath daily notifications", importance)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private val TAG = WeatherRequestService::class.java.name
        const val WORK_TAG = "AqiData download"
        private const val CHANNEL_ID = "CHANNEL_UPDATE_AQI"
        private const val UPDATE_NOTIFICATION_ID = 3447
    }
}