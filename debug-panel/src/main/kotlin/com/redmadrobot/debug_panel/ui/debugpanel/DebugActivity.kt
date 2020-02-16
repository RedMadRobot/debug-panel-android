package com.redmadrobot.debug_panel.ui.debugpanel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.ui.StubFragment
import com.redmadrobot.debug_panel.ui.accounts.add.AddAccountFragment
import com.redmadrobot.debug_panel.ui.servers.add.AddServerFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : AppCompatActivity() {

    private val debugFeatureListAdapter = GroupAdapter<GroupieViewHolder>()

    private val debugFeatureList by lazy {
        listOf<Item>(
            DebugFeatureItem(R.string.accounts, ::openDebugFeature),
            DebugFeatureItem(R.string.servers, ::openDebugFeature),
            DebugFeatureItem(R.string.settings, ::openDebugFeature)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setViews()
    }

    private fun setViews() {
        debug_feature_list.layoutManager = LinearLayoutManager(this)
        debug_feature_list.adapter = debugFeatureListAdapter
        debugFeatureListAdapter.update(debugFeatureList)
    }

    private fun openDebugFeature(featureNameId: Int) {
        val fragmentForNavigation = when (featureNameId) {
            R.string.accounts -> AddAccountFragment.getInstance()
            R.string.servers -> AddServerFragment.getInstance()
            R.string.settings -> StubFragment()
            else -> throw IllegalArgumentException("Unsupported feature name id")
        }

        //TODO Временное решение. Переделать на Navigation Component
        supportFragmentManager.beginTransaction()
            .replace(R.id.debug_activity_root, fragmentForNavigation)
            .addToBackStack(fragmentForNavigation.javaClass.simpleName)
            .commit()
    }
}
