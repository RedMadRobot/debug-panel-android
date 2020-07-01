package com.redmadrobot.debug_panel.ui.debugpanel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.core.extension.getAllPlugins
import com.redmadrobot.debug_panel.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : AppCompatActivity() {

    private val debugFeatureListAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setViews()
    }

    private fun setViews() {
        debug_feature_list.layoutManager = LinearLayoutManager(this)
        debug_feature_list.adapter = debugFeatureListAdapter
        debugFeatureListAdapter.update(getSettingItems())
    }

    private fun openSetting(settingFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.debug_activity_root, settingFragment)
            .addToBackStack(settingFragment.javaClass.simpleName)
            .commit()
    }

    private fun getSettingItems(): List<Item> {
        val plugins = getAllPlugins()
            /*Only Plugins with SettingFragment*/
            .filter { it.getSettingFragment() != null }

        return plugins.map { plugin ->
            PluginSettingItem(plugin.getName()) {
                openSetting(requireNotNull(plugin.getSettingFragment()))
            }
        }
    }
}
