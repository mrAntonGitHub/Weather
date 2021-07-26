package com.i.o.mob.dev.weather.data.enums

enum class DateFormat(val pattern: String) {
    NameOfDaysOfTheWeek("EEEE"),
    DayMonthYearTimeWithSeconds("dd-MM-yyyy HH:mm:ss"),
    DayMonthTimeWithSeconds("dd MMM HH:mm:ss"),
    HoursMinutes("HH:mm"),
    DayMonth("dd MMM")
}