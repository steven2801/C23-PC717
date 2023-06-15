package com.steven.foodqualitydetector.utils
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


object TimeFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatIsoStringToRelativeTime(isoString: String): String {
        val formatter = DateTimeFormatter.ISO_INSTANT
        val instant = Instant.from(formatter.parse(isoString))
        val now = Instant.now()

        val duration = Duration.between(instant, now)
        val seconds = duration.seconds

        return when {
            seconds < 60 -> "Just now"
            seconds < 3600 -> if ("${seconds / 60}" == "1") {
                "${seconds / 60} minute ago"
            } else {
                "${seconds / 60} minutes ago"
            }
            seconds < 86400 -> if ("${seconds / 3600}" == "1") {
                "${seconds / 3600} hour ago"
            } else {
                "${seconds / 3600} hours ago"
            }
            else -> {
                val zoneId = ZoneId.systemDefault()
                val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
                val yearDiff = ZonedDateTime.now().until(zonedDateTime, ChronoUnit.YEARS)
                if (yearDiff == 0L) {
                    zonedDateTime.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
                } else {
                    zonedDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                }
            }
        }
    }
}