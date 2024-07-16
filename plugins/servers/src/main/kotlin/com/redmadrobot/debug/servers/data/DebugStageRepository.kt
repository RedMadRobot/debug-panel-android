package com.redmadrobot.debug.servers.data

import android.content.Context
import androidx.core.content.edit
import com.redmadrobot.debug.servers.data.model.DebugStage
import com.redmadrobot.debug.servers.data.storage.DebugStagesDao
import com.redmadrobot.debug.servers.data.storage.SharedPreferencesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DebugStageRepository(
    private val context: Context,
    private val debugStagesDao: DebugStagesDao,
    private val preInstalledStages: List<DebugStage>,
) {

    companion object {
        private const val SELECTED_STAGE_HOSTS_HASH = "SELECTED_STAGE_HOSTS_HASH"
        private const val SELECTED_STAGE_NAME = "SELECTED_STAGE_NAME"
    }

    private val sharedPreferences by lazy {
        SharedPreferencesProvider.get(context)
    }

    fun getPreInstalledStages(): List<DebugStage> {
        return preInstalledStages
    }

    fun saveSelectedStage(selectedStage: DebugStage) {
        sharedPreferences.edit {
            putString(SELECTED_STAGE_NAME, selectedStage.name)
            putInt(SELECTED_STAGE_HOSTS_HASH, selectedStage.hosts.hashCode())
        }
    }

    fun getSelectedStage(): DebugStage {
        val stageName = sharedPreferences.getString(SELECTED_STAGE_NAME, null)
        val hostsHash = sharedPreferences.getInt(SELECTED_STAGE_HOSTS_HASH, -1)

        return if (stageName != null) {
            preInstalledStages
                .find { it.name == stageName && it.hosts.hashCode() == hostsHash }
                ?: debugStagesDao.getStage(stageName)
                ?: getDefault()
        } else {
            getDefault()
        }
    }

    fun getDefault(): DebugStage {
        return preInstalledStages.first { it.isDefault }
    }

    suspend fun getStages(): List<DebugStage> {
        return withContext(Dispatchers.IO) {
            debugStagesDao.getAll()
        }
    }

    suspend fun updateStage(server: DebugStage) {
        withContext(Dispatchers.IO) {
            debugStagesDao.update(server)
        }
    }
}
