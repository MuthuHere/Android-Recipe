package com.muthu.fourtitude.utils

import android.content.Context
import android.widget.Toast

object AppMessages {


    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}