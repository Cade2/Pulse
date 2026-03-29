package com.cade2.pulse.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this))
}

fun String.toDisplayDate(
    inputPattern: String = "yyyy-MM-dd",
    outputPattern: String = "MMM d, yyyy"
): String {
    return try {
        val sdf = SimpleDateFormat(inputPattern, Locale.getDefault())
        val date = sdf.parse(this) ?: return this
        SimpleDateFormat(outputPattern, Locale.getDefault()).format(date)
    } catch (e: Exception) {
        this
    }
}

fun Int.toHourLabel(): String {
    val hour = this % 12
    val amPm = if (this < 12) "AM" else "PM"
    val displayHour = if (hour == 0) 12 else hour
    return "$displayHour:00 $amPm"
}

fun todayDateString(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
