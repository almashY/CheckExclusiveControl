package com.example.checkexclusivecontrol.purchase

import android.app.AlertDialog
import android.content.Context

object PurchaseConfirmDialog {
    fun show(
        context: Context,
        onConfirm: () -> Unit,
        onCancel: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("商品の購入")
            .setMessage("こちらの商品を購入しますか？")
            .setPositiveButton("OK") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("キャンセル") { _, _ ->
                onCancel()
            }
            .show()
    }
}