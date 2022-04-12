package com.redmadrobot.debug_sample

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.account_plugin.plugin.AccountSelectedEvent
import com.redmadrobot.debug_panel_core.internal.DebugPanel
import com.redmadrobot.debug_sample.network.ApiFactory
import com.redmadrobot.debugpanel.R
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
import com.redmadrobot.servers_plugin.plugin.ServerSelectedEvent
import com.redmadrobot.variable_plugin.plugin.asDebugVariable
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        DebugPanel.observeEvents()
            .onEach { event ->
                when (event) {
                    is AccountSelectedEvent -> {
                        //Обработка выбора аккаунта
                    }
                    is ServerSelectedEvent -> {
                        //Обработка выбора сервера
                    }
                }
            }
            .launchIn(lifecycleScope)
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
        autofill_sample.setOnClickListener {
            val strings = """
                    ${"initString".asDebugVariable("filledString")}
                    ${"".asDebugVariable("initialyEmptyString")}
                """.trimIndent()
            val boolean = """
                ${true.asDebugVariable("initialy true")}
                ${false.asDebugVariable("initialy false")}
            """.trimIndent()
            val digits = """
                ${10.asDebugVariable("integer10")}
                ${100.asDebugVariable("integer100")}
                ${10F.asDebugVariable("float10")}
                ${.0.asDebugVariable("double0")}
            """.trimIndent()
            val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
                    .asDebugVariable("local_date_time")
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } else {
                ""
            }

            Toast.makeText(
                applicationContext,
                strings + "\n" + boolean + "\n" + digits + "\n" + date,
                Toast.LENGTH_LONG
            ).show()
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
        FlipperPlugin.addSource(
            "Test 1",
            mapOf(
                "id1" to FlipperValue.BooleanValue(false),
                "id2" to FlipperValue.BooleanValue(false),
                "id3" to FlipperValue.BooleanValue(false),
            )
        )
        FlipperPlugin.addSource(
            "Test 2",
            mapOf(
                "id1" to FlipperValue.BooleanValue(true),
                "id2" to FlipperValue.BooleanValue(false),
                "id3" to FlipperValue.BooleanValue(true),
            )
        )

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
            .find { (feature) -> feature == "id1" }
            ?.let { (_, value) ->
                (value as? FlipperValue.BooleanValue)?.value
            }
            ?: false

        val showSecond = changedToggles.entries
            .find { (feature) -> feature == "id2" }
            ?.let { (_, value) ->
                (value as? FlipperValue.BooleanValue)?.value
            }
            ?: true

        val showThird = changedToggles.entries
            .find { (feature) -> feature == "id3" }
            ?.let { (_, value) ->
                (value as? FlipperValue.BooleanValue)?.value
            }
            ?: false

        label_feature_toggle_1.visibility = if (showFirst) View.VISIBLE else View.GONE
        label_feature_toggle_2.visibility = if (showSecond) View.VISIBLE else View.GONE
        label_feature_toggle_3.visibility = if (showThird) View.VISIBLE else View.GONE
    }
}
