package com.vivek.marksapp.util

import android.widget.ImageView
import coil.load

fun ImageView.loadImage(res: Int) {
    load(res) {
        crossfade(true)
    }
}