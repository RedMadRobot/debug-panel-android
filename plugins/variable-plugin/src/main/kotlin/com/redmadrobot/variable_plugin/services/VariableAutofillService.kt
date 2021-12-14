package com.redmadrobot.variable_plugin.services

import android.R
import android.app.assist.AssistStructure
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.*
import android.view.autofill.AutofillValue
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

        traverse(structure.getWindowNodeAt(0).rootViewNode)

        callback.onSuccess(
            FillResponse.Builder()
                .addDataset(
                    Dataset.Builder()
                        .setValue(
                            memes!!.autofillId!!,
                            AutofillValue.forText("MY"),
                            RemoteViews(
                                packageName,
                                R.layout.simple_list_item_1
                            ).also { it.setTextViewText(android.R.id.text1, "I KNOW IT") }
                        )
                        .build()
                )
                .addDataset(
                    Dataset.Builder()
                        .setValue(
                            memes!!.autofillId!!,
                            AutofillValue.forText("DICK"),
                            "Dada.*".toPattern(),
                            RemoteViews(
                                packageName,
                                R.layout.simple_list_item_1
                            ).also { it.setTextViewText(android.R.id.text1, "YOU WANT IT") }
                        )
                        .build()
                )
                .build()
        )
    }

    private var memes: AssistStructure.ViewNode? = null

    private fun traverse(viewNode: AssistStructure.ViewNode) {
        if (viewNode.isFocused) {
            memes = viewNode

            return
        }

        (0 until viewNode.childCount).forEach { childIndex ->
            viewNode.getChildAt(childIndex)?.let { traverse(it) }
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {

    }
}
