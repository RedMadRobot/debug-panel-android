package com.redmadrobot.debug_panel.inapp.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeListener(private val onAction: () -> Unit) : SensorEventListener {
    companion object {
        private const val TRIGGER_MOTION_BORDER = 3.0f
        private const val VECTOR_COUNT = 3
        private const val EVENT_TIME_DELAY = 1000L
    }

    private var lastTimestamp: Long = System.currentTimeMillis()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            val values = sensorEvent.values
            val commonVector = values.take(VECTOR_COUNT).map {
                val valueWithoutGravity = it / SensorManager.GRAVITY_EARTH
                valueWithoutGravity * valueWithoutGravity
            }.sum()
            val currentTime = System.currentTimeMillis()
            val isSignificantEvent = commonVector > TRIGGER_MOTION_BORDER
            val isNewEvent = currentTime > lastTimestamp + EVENT_TIME_DELAY
            if (isSignificantEvent && isNewEvent) {
                lastTimestamp = currentTime
                onAction.invoke()
            }
        }
    }
}
