package com.redmadrobot.debug_sample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.plugin.accounts.AccountSelectedEvent
import com.redmadrobot.debug.plugin.servers.ServerSelectedEvent
import com.redmadrobot.debug_sample.network.ApiFactory
import com.redmadrobot.debugpanel.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setViews()

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
                        showSelectedAccount(event.debugAccount.login)
                    }

                    is ServerSelectedEvent -> {
                        //Обработка выбора сервера
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun ActivityMainBinding.setViews() {
        openDebugPanel.setOnClickListener {
            openDebugPanel()
        }
        requestTest.setOnClickListener {
            makeTestRequest()
        }
        openSecondActivity.setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
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

    private fun showSelectedAccount(account: String) {
        Toast.makeText(
            this,
            "Account $account selected",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun openDebugPanel() {
        DebugPanel.showPanel(this)
    }

}
