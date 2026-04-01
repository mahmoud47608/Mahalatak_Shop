package com.mahalatk.util

object DateUtils {

    fun formatDateMillis(millis: Long): String {
        val totalDays = (millis / 86400000L).toInt()
        var year = 1970
        var remaining = totalDays

        while (true) {
            val daysInYear = if (isLeapYear(year)) 366 else 365
            if (remaining < daysInYear) break
            remaining -= daysInYear
            year++
        }

        val monthDays = intArrayOf(
            31, if (isLeapYear(year)) 29 else 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31,
        )

        var month = 0
        while (month < 12 && remaining >= monthDays[month]) {
            remaining -= monthDays[month]
            month++
        }

        val day = remaining + 1
        return "${day.pad()}/${(month + 1).pad()}/$year"
    }

    private fun isLeapYear(year: Int): Boolean =
        year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

    private fun Int.pad(): String = toString().padStart(2, '0')
}
