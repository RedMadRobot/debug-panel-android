package com.redmadrobot.debug_panel

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.redmadrobot.debug_panel.util.ActivityLifecycleCallbacksAdapter

class DebugPanel(private val context: Context) {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "DEBUG_NOTIFICATION_CHANNEL"
        private const val NOTIFICATION_ID = 1
    }

    //Список имен открытых activity
    private var activityList = mutableListOf<String>()
    private var notificationManager: NotificationManagerCompat? = null

    fun start() {
        registerActivityLifecycleCallback()
    }

    private fun registerActivityLifecycleCallback() {
        (context as? Application)?.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityResumed(activity: Activity) {
                    super.onActivityResumed(activity)
                    if (activityList.isEmpty()) showDebugNotification()
                    activityList.add(activity.javaClass.simpleName)
                }

                override fun onActivityPaused(activity: Activity) {
                    super.onActivityPaused(activity)
                    activityList.remove((activity.javaClass.simpleName))
                    if (activityList.isEmpty()) {
                        notificationManager?.cancel(NOTIFICATION_ID)
                    }
                }
            }
        )
    }

    private fun showDebugNotification() {
        notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.debug_panel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            notificationManager?.createNotificationChannel(channel)
        }

        val debugActivityIntent = Intent(context, DebugActivity::class.java)
        val debugActivityPendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(debugActivityIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_debug_notification)
            .setContentTitle(context.getString(R.string.notification_title))
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(debugActivityPendingIntent)
            .build()

        notificationManager?.notify(NOTIFICATION_ID, notification)
    }
}
