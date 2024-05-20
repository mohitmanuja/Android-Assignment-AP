package com.image.assignment.library

import com.image.assignment.utils.Util.dpToPx

data class DownscaleOptions(val widthDp: Float, val heightDp: Float){
    val widthPx: Int
        get() = dpToPx(widthDp)

    val heightPx: Int
        get() = dpToPx(heightDp)
}
