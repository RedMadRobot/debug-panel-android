package com.redmadrobot.debug.core.util

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.redmadrobot.debug.core.R
import com.redmadrobot.debug.core.ui.settings.DebugSettingsActivity

internal class DebugPanelNotification(private val context: Context) {

    private var notificationManager: NotificationManagerCompat? = null

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "DEBUG_NOTIFICATION_CHANNEL"
        private const val NOTIFICATION_ID = 1
    }

    fun show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return
        }

        notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.debug_panel)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            notificationManager?.createNotificationChannel(channel)
        }

        val settingActivityPendingIntent = getSettingActivityPendingIntent(context)
        val openPanelPendingIntent = getPanelPendingIntent(context)

        val notification = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_debug_notification)
            .setContentTitle(context.getString(R.string.debug_panel_notification_title))
            .setOngoing(true)
            .setSound(Uri.EMPTY)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openPanelPendingIntent)
            .addAction(
                R.drawable.ic_settings_notification_action,
                context.getString(R.string.debug_panel_notification_settings),
                settingActivityPendingIntent
            )
            .build()

        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    fun hide() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }

    private val pendingIntentFlag
        get() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

    private fun getSettingActivityPendingIntent(context: Context): PendingIntent? {
        val settingActivityIntent = Intent(context, DebugSettingsActivity::class.java)

        return TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(settingActivityIntent)
            .getPendingIntent(0, pendingIntentFlag)
    }

    private fun getPanelPendingIntent(context: Context): PendingIntent? {
        val openPanelIntent = Intent(DebugPanelBroadcastReceiver.ACTION_OPEN_DEBUG_PANEL).apply {
            setPackage(context.packageName)
        }
        return PendingIntent.getBroadcast(
            context,
            0,
            openPanelIntent,
            pendingIntentFlag
        )
    }
}
