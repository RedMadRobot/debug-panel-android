package com.redmadrobot.debug.plugin.konfeature

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.redmadrobot.debug.plugin.konfeature.util.JsonConverter
import com.redmadrobot.konfeature.source.FeatureValueSource
import com.redmadrobot.konfeature.source.Interceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

public class KonfeatureDebugPanelInterceptor(context: Context) : Interceptor {

    private val preferences by lazy {
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    private val _valuesFlow = MutableStateFlow(emptyMap<String, Any>())

    internal val valuesFlow = _valuesFlow.asStateFlow()

    private val mutex = Mutex()

    override val name: String = "DebugPanelInterceptor"

    init {
        CoroutineScope(Dispatchers.IO).launch { fetchValues() }
    }

    override fun intercept(valueSource: FeatureValueSource, key: String, value: Any): Any? {
        return _valuesFlow.value[key]
            ?.let { convertTypeIfNeeded(it, value) }
            ?.takeIf { it != value }
    }

    private suspend fun fetchValues() {
        _valuesFlow.value = withContext(Dispatchers.IO) {
            mutex.withLock { preferences.fetchValues() }
        }
    }

    private fun convertTypeIfNeeded(debugValue: Any, value: Any): Any {
        var result = when {
            debugValue is Int -> debugValue.toLong()
            debugValue is Float -> debugValue.toDouble()
            else -> debugValue
        }
        if (result is Long && value is Double) {
            result = result.toDouble()
        }
        return result
    }

    internal suspend fun setValue(key: String, value: Any) {
        _valuesFlow.update { it + (key to value) }
        updateValues(_valuesFlow.value)
    }

    internal suspend fun resetValue(key: String) {
        _valuesFlow.update { it - key }
        updateValues(_valuesFlow.value)
    }

    internal suspend fun resetAll() {
        _valuesFlow.value = emptyMap<String, Any>()
        updateValues(_valuesFlow.value)
    }

    private suspend fun updateValues(map: Map<String, Any>) {
        coroutineScope {
            launch(Dispatchers.IO) {
                mutex.withLock { preferences.updateValues(map) }
            }
        }
    }

    private fun SharedPreferences.fetchValues(): Map<String, Any> {
        return try {
            val jsonValues = preferences.getString(KEY, EMPTY_MAP) ?: EMPTY_MAP
            JsonConverter.toMap(JSONObject(jsonValues))
        } catch (error: Exception) {
            Timber.tag(TAG).e(error, "cant fetch debug values")
            preferences.edit(commit = true) { remove(KEY) }
            emptyMap<String, Any>()
        }
    }

    private fun SharedPreferences.updateValues(map: Map<String, Any>) {
        try {
            val jsonValues = JSONObject(map).toString()
            preferences.edit(commit = true) {
                putString(KEY, jsonValues)
            }
        } catch (error: Exception) {
            Timber.tag(TAG).e(error, "cant update debug values")
        }
    }

    private companion object {
        private const val EMPTY_MAP = "{}"
        private const val FILE_NAME = "debug_panel_interceptor_values"
        private const val KEY = "values"
        private const val TAG = "DebugPanelInterceptor"
    }
}
