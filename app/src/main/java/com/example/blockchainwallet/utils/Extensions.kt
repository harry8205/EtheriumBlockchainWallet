package com.example.mintnft.utils

import android.content.Context
import android.view.View
import android.widget.Toast

object Extensions {
    fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}