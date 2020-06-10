package com.redmadrobot.debug_panel.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.inapp.shake.ShakeController
import com.redmadrobot.debug_panel.ui.debugpanel.DebugActivity

class ActivityLifecycleHandler(private val application: Application) {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "DEBUG_NOTIFICATION_CHANNEL"
        private const val NOTIFICATION_ID = 1
    }

    //Счетчик открытых activity
    private var openActivityCount = 0
    private var notificationManager: NotificationManagerCompat? = null

    private val shakeController = ShakeController(application.applicationContext)

    fun start() {
        registerActivityLifecycleCallback()
    }

    private fun registerActivityLifecycleCallback() {
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityResumed(activity: Activity) {
                    if (openActivityCount == 0) onResumed()
                    ++openActivityCount

                    (activity as? AppCompatActivity)?.let(shakeController::register)
                }

                override fun onActivityPaused(activity: Activity) {
                    --openActivityCount
                    if (openActivityCount == 0) onPaused()
                }
            }
        )
    }

    private fun onPaused() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }

    private fun onResumed() {
        showDebugNotification(application.applicationContext)
        shakeController.unregister()
    }

    private fun showDebugNotification(context: Context) {
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
