package com.redmadrobot.debug_panel_core.ui.debugpanel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel_core.extension.getAllPlugins
import com.redmadrobot.itemsadapter.bind
import com.redmadrobot.itemsadapter.itemsAdapter
import com.redmadrobot.panel_core.R
import com.redmadrobot.panel_core.databinding.ItemPluginSettingBinding
import kotlinx.android.synthetic.main.activity_debug.*

internal class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setViews()
    }

    private fun setViews() {
        debug_feature_list.layoutManager = LinearLayoutManager(this)
        debug_feature_list.adapter = itemsAdapter(getSettingItems()) { item ->
            bind<ItemPluginSettingBinding>(R.layout.item_plugin_setting) {
                itemDebugFeatureLabel.text = item.pluginName
                root.setOnClickListener { item.onClicked.invoke() }
            }
        }
    }

    private fun openSetting(settingFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.debug_activity_root, settingFragment)
            .addToBackStack(settingFragment.javaClass.simpleName)
            .commit()
    }

    private fun getSettingItems(): List<PluginsSettingItem> {
        return getAllPlugins()
            /*Only Plugins with SettingFragment*/
            .filter { it.getSettingFragment() != null }
            .map { plugin ->
                PluginsSettingItem(plugin.getName()) {
                    openSetting(requireNotNull(plugin.getSettingFragment()))
                }
            }
    }
}
