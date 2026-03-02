package com.redmadrobot.debug_sample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.plugin.servers.ServerSelectedEvent
import com.redmadrobot.debug_sample.network.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { MainActivityScreen() }
        observeDebugPanelEvents()
    }

    @Composable
    private fun MainActivityScreen(modifier: Modifier = Modifier) {
        val secondActivityIntent = remember {
            Intent(
                this@MainActivity,
                SecondActivity::class.java
            )
        }

        MaterialTheme {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    space = 24.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { DebugPanel.showPanel(this@MainActivity) }) {
                    Text("Open debug panel")
                }
                Button(onClick = { makeTestRequest() }) {
                    Text("Test request")
                }
                Button(onClick = { startActivity(secondActivityIntent) }) {
                    Text("Open second activity")
                }
            }
        }
    }

    private fun observeDebugPanelEvents() {
        DebugPanel.subscribeToEvents(this) { event ->
            when (event) {
                is ServerSelectedEvent -> {
                    //Обработка выбора сервера
                }
            }
        }

        DebugPanel.observeEvents()
            .onEach { event ->
                when (event) {
                    is ServerSelectedEvent -> {
                        //Обработка выбора сервера
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun makeTestRequest() {
        lifecycleScope.launch {
            var requestedUrl: String? = null
            withContext(Dispatchers.IO) {
                runCatching { ApiFactory.getSampleApi { requestedUrl = it }.getTestData() }
            }
            requestedUrl?.let(::showTestRequestToast)
        }
    }

    private fun showTestRequestToast(requestEndPoint: String) {
        Toast.makeText(this, "Request to: $requestEndPoint", Toast.LENGTH_LONG).show()
    }
}
