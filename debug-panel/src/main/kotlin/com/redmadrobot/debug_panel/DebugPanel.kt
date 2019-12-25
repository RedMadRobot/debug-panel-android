package com.redmadrobot.debug_panel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder

class DebugPanel {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "DEBUG_NOTIFICATION_CHANNEL"
        private const val NOTIFICATION_ID = 1
    }

    fun start(context: Context) {
        showDebugNotification(context)
    }

    private fun showDebugNotification(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.debug_panel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(channel)
        }


        val debugActivityIntent = Intent(context, DebugActivity::class.java)
        val debugActivityPendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(debugActivityIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_debug_notification)
            .setContentTitle(context.getString(R.string.notification_title))
            .setAutoCancel(false)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(debugActivityPendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
