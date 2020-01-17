package com.redmadrobot.debug_panel.shake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

class ShakeController(context: Context, openDebugPanelAction: () -> Unit) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shakeDetector =
        ShakeListener(openDebugPanelAction)
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    fun register() {
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun unregister() {
        sensorManager.unregisterListener(shakeDetector)
    }
}

