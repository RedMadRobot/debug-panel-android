package com.redmadrobot.debug_panel_core.inapp.shake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.debug_panel_core.inapp.DebugBottomSheet

internal class ShakeController(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shakeDetector = ShakeListener { openDebugPanelAction?.invoke() }
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var openDebugPanelAction: (() -> Unit)? = null

    fun register(activity: AppCompatActivity) {
        openDebugPanelAction = { DebugBottomSheet.show(activity.supportFragmentManager) }
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun unregister() {
        sensorManager.unregisterListener(shakeDetector)
        openDebugPanelAction = null
    }
}

