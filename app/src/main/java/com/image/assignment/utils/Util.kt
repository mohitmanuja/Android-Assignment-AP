package com.image.assignment.utils

import android.content.res.Resources

object Util {

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }
}