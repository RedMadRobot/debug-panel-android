package com.redmadrobot.variable_plugin.services

import android.R
import android.app.assist.AssistStructure
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.*
import android.widget.RemoteViews
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
public class VariableAutofillService : AutofillService() {

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback,
    ) {
        val structure = request.fillContexts.last().structure

        val focusedView = findFocusedView(structure.getWindowNodeAt(0).rootViewNode)
        if (focusedView != null) {
            try {
                callback.onSuccess(
                    FillResponse.Builder()
                        .addDataset(
                            Dataset.Builder()
                                .setValue(
                                    focusedView.autofillId!!,
                                    null,
                                    RemoteViews(
                                        packageName,
                                        R.layout.simple_list_item_1
                                    )
                                )
                                .build()
                        )
                        .setSaveInfo(
                            SaveInfo.Builder(
                                SaveInfo.SAVE_DATA_TYPE_GENERIC,
                                arrayOf(focusedView.autofillId!!)
                            ).build()
                        )
                        .build()
                )
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        callback.onSuccess()
    }

    private fun findFocusedView(viewNode: AssistStructure.ViewNode): AssistStructure.ViewNode? {
        if (viewNode.isFocused) {
            return viewNode
        }

        var focusedView: AssistStructure.ViewNode? = null

        for (childIndex in 0 until viewNode.childCount) {
            focusedView = viewNode.getChildAt(childIndex)?.let { findFocusedView(it) }

            if (focusedView != null) {
                break
            }
        }

        return focusedView
    }

}
