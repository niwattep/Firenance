package com.hobby.niwat.firenance

fun String.addCommas() = try {
    String.format("%,d", Integer.valueOf(this))
} catch (e: NumberFormatException) {
    ""
}

fun Int.toStringAddCommas() = String.format("%,d", this)