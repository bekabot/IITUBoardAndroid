package kz.iitu.iituboardandroid

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

fun String.toPrintableDate() = try {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = Integer.parseInt(this) * 1000L
    val date = calendar.time
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    sdf.format(date)
} catch (e: Exception) {
    null
}

fun String.toDate() = try {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = Integer.parseInt(this) * 1000L
    calendar.time
} catch (e: Exception) {
    null
}