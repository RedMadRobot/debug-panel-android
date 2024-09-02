package com.redmadrobot.debug.core.inapp.shake

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.redmadrobot.debug.core.DebugPanel

internal class ShakeController(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shakeDetector = ShakeListener { openDebugPanelAction?.invoke() }
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var openDebugPanelAction: (() -> Unit)? = null

    fun register(activity: Activity) {
        unregister()
        openDebugPanelAction = { DebugPanel.showPanel(activity) }
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun unregister() {
        sensorManager.unregisterListener(shakeDetector)
        openDebugPanelAction = null
    }
}

