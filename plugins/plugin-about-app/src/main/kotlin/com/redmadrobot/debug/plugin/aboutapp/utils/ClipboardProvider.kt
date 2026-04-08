package com.redmadrobot.debug.plugin.aboutapp.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

internal class ClipboardProvider(private val context: Context) {
    private val clipboardManager: ClipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    fun copyToClipboard(label: String, text: String) {
        val clipData = ClipData.newPlainText(label, text)

        clipboardManager.setPrimaryClip(clipData)
    }
}
