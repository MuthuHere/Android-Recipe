package com.muthu.fourtitude.utils

import android.content.Context

object AppDialogs {

    lateinit var dialog: androidx.appcompat.app.AlertDialog.Builder
    fun showDialogWith2Buttons(
        context: Context,
        message: String,
        title: String,
        listener: OnDialogClickListener
    ) {
        dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton("Yes") { p0, p1 ->
                listener.onPositiveListener()
            }
            .setNegativeButton("No") { p0, p1 ->
                listener.onNegativeListener()
                dialog
            }
        dialog.setCancelable(false)

        dialog.show()
    }


    interface OnDialogClickListener {
        fun onPositiveListener()
        fun onNegativeListener()
    }
}