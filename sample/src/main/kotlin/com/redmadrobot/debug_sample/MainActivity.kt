package com.redmadrobot.debug_sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.debug_panel_core.internal.DebugPanel
import com.redmadrobot.debug_sample.network.ApiFactory
import com.redmadrobot.debugpanel.R
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
    }

    private fun setViews() {
        choose_account.setOnClickListener {
            chooseAccount()
        }
        request_test.setOnClickListener {
            makeTestRequest()
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
}
