package com.redmadrobot.debug.core.ui.debugpanel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.itemsadapter.bind
import com.redmadrobot.itemsadapter.itemsAdapter
import com.redmadrobot.debug.panel.core.R
import com.redmadrobot.debug.panel.core.databinding.ActivityDebugBinding
import com.redmadrobot.debug.panel.core.databinding.ItemPluginSettingBinding

internal class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setViews()
    }

    private fun ActivityDebugBinding.setViews() {
        debugFeatureList.layoutManager = LinearLayoutManager(root.context)
        debugFeatureList.adapter = itemsAdapter(getSettingItems()) { item ->
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
