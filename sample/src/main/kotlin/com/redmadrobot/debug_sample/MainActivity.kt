package com.redmadrobot.debug_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.debug_panel.accounts.ui.choose.AccountSelectBottomSheet
import com.redmadrobot.debugpanel.R
import kotlinx.android.synthetic.main.activity_main.*

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
    }

    private fun chooseAccount() {
        AccountSelectBottomSheet.show(supportFragmentManager)
    }
}
