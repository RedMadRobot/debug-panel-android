package com.redmadrobot.debug_panel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.debug_panel.accounts.ui.add.AddAccountFragment

class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        //TODO Временное решение. Переделать на Navigation Component
        supportFragmentManager.beginTransaction()
            .add(R.id.debug_activity_root, AddAccountFragment.getInstance())
            .commit()
    }

}
