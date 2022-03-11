package com.redmadrobot.debug_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.account_plugin.plugin.AccountSelectedEvent
import com.redmadrobot.debug_panel_core.internal.DebugPanel
import com.redmadrobot.debug_sample.network.ApiFactory
import com.redmadrobot.debugpanel.R
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
import com.redmadrobot.servers_plugin.plugin.ServerSelectedEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setViews()

        observeFeatureToggles()

        DebugPanel.subscribeToEvents(this) { event ->
            when (event) {
                is AccountSelectedEvent -> {
                    //Обработка выбора аккаунта
                }
                is ServerSelectedEvent -> {
                    //Обработка выбора сервера
                }
            }
        }
    }

    private fun setViews() {
        choose_account.setOnClickListener {
            chooseAccount()
        }
        request_test.setOnClickListener {
            makeTestRequest()
        }
        open_second_activity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    /**
     * Создание тестового запроса для тестирования выбора сервера
     * */
    private fun makeTestRequest() {
        ApiFactory.getSampleApi {
            runOnUiThread {
                showTestRequestToast(it)
            }
        }
            .getTestData().enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    /*do nothing*/
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    /*do nothing*/
                }
            })
    }

    /**
     * Отображаем адрес тестового запроса
     * */
    private fun showTestRequestToast(requestEndPoint: String) {
        Toast.makeText(
            this,
            "Request to: $requestEndPoint",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun chooseAccount() {
        DebugPanel.showPanel(supportFragmentManager)
    }

    private fun observeFeatureToggles() {
        FlipperPlugin
            .observeChangedToggles()
            .onEach { changedToggles ->
                onFlipperTogglesChanged(changedToggles)
            }
            .flowOn(Dispatchers.Main)
            .launchIn(GlobalScope)
    }

    private fun onFlipperTogglesChanged(changedToggles: Map<String, FlipperValue>) {
        val showFirst = changedToggles.entries
            .find { (feature) -> feature.contains("id1", true) }
            ?.let { (_, value) ->
                (value as? FlipperValue.BooleanValue)?.value
            }
            ?: false

        val showSecond = changedToggles.entries
            .find { (feature) -> feature.contains("id2", true) }
            ?.let { (_, value) ->
                (value as? FlipperValue.BooleanValue)?.value
            }
            ?: true

        val showThird = changedToggles.entries
            .find { (feature) -> feature.contains("id3", true) }
            ?.let { (_, value) ->
                (value as? FlipperValue.BooleanValue)?.value
            }
            ?: false

        label_feature_toggle_1.visibility = if (showFirst) View.VISIBLE else View.GONE
        label_feature_toggle_2.visibility = if (showSecond) View.VISIBLE else View.GONE
        label_feature_toggle_3.visibility = if (showThird) View.VISIBLE else View.GONE
    }
}
