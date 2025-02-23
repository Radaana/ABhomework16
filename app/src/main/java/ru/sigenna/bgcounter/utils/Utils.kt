package ru.sigenna.bgcounter.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

   fun formatDaleFromLong(time: Long): String {
       return dateFormatter.format(Date(time))
   }
}