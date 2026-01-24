package com.example.learningapp.avatar

import com.example.learningapp.R

fun getVisemeDrawable(visemeId: Int): Int {
    return when (visemeId) {
        0 -> R.drawable.viseme_id_0
        1 -> R.drawable.viseme_id_1
        2 -> R.drawable.viseme_id_2
        3 -> R.drawable.viseme_id_3
        4 -> R.drawable.viseme_id_4
        5 -> R.drawable.viseme_id_5
        6 -> R.drawable.viseme_id_6
        7 -> R.drawable.viseme_id_7
        8 -> R.drawable.viseme_id_8
        9 -> R.drawable.viseme_id_9
        10 -> R.drawable.viseme_id_10
        11 -> R.drawable.viseme_id_11
        12 -> R.drawable.viseme_id_12
        13 -> R.drawable.viseme_id_13
        14 -> R.drawable.viseme_id_14
        15 -> R.drawable.viseme_id_15
        16 -> R.drawable.viseme_id_16
        17 -> R.drawable.viseme_id_17
        18 -> R.drawable.viseme_id_18
        19 -> R.drawable.viseme_id_19
        20 -> R.drawable.viseme_id_20
        21 -> R.drawable.viseme_id_21
        else -> R.drawable.viseme_id_0
    }
}