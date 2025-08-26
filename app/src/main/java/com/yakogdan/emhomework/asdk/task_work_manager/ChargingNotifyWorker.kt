package com.yakogdan.emhomework.asdk.task_work_manager

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yakogdan.emhomework.R

class ChargingNotifyWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext = appContext, params = params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        showChargingNotification(context = applicationContext)
        return Result.success()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showChargingNotification(context: Context) {
        ensureChannel(context = context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Устройство на зарядке")
            .setContentText("Телефон подключен к источнику питания")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                /* id = */ NOTIFICATION_ID,
                /* notification = */ notification,
            )
    }

    private fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                /* id = */ CHANNEL_ID,
                /* name = */ CHANNEL_NAME,
                /* importance = */ NotificationManager.IMPORTANCE_DEFAULT,
            ).apply { description = "Уведомления о состоянии зарядки" }

            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "charging_status"
        private const val CHANNEL_NAME = "Charging status"
    }
}