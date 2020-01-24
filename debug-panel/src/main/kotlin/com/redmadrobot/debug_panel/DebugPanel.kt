package com.redmadrobot.debug_panel

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.redmadrobot.debug_panel.inapp.shake.ShakeController
import com.redmadrobot.debug_panel.internal.DebugPanelContainer
import com.redmadrobot.debug_panel.ui.DebugActivity
import com.redmadrobot.debug_panel.util.ActivityLifecycleCallbacksAdapter

class DebugPanel(private val context: Context) {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "DEBUG_NOTIFICATION_CHANNEL"
        private const val NOTIFICATION_ID = 1

        //TODO Перенести в класс DebugPanelInstance после того как PR с ним будет принят
        private var debugPanelContainer: DebugPanelContainer? = null

        internal fun initContainer(context: Context) {
            debugPanelContainer = DebugPanelContainer(context)
        }

        internal fun getContainer(): DebugPanelContainer {
            return debugPanelContainer
                ?: throw IllegalStateException("Container must be initialised")
        }
    }

    //Список имен открытых activity
    private var openActivityCount = 0
    private var notificationManager: NotificationManagerCompat? = null

    private val shakeController = ShakeController(context)

    fun start() {
        registerActivityLifecycleCallback()

        //TODO Перенести в класс DebugPanelInstance после того как PR с ним будет принят
        DebugPanel.initContainer(context)
    }

    private fun registerActivityLifecycleCallback() {
        (context as? Application)?.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityResumed(activity: Activity) {
                    super.onActivityResumed(activity)
                    if (openActivityCount == 0) showDebugNotification()
                    ++openActivityCount

                    (activity as? AppCompatActivity)?.let(shakeController::register)
                }

                override fun onActivityPaused(activity: Activity) {
                    super.onActivityPaused(activity)
                    --openActivityCount
                    if (openActivityCount == 0) {
                        notificationManager?.cancel(NOTIFICATION_ID)
                    }
                    shakeController.unregister()
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
