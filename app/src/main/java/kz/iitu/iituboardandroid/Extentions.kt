package kz.iitu.iituboardandroid

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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

fun Double.convertToStringBytes(context: Context): String {
    var text = ""
    val initValue = this
    val deliminator = ""
    if (initValue > 1000) {
        var value = initValue / 1024
        val ints = arrayOf(0, 1, 2)
        for (index: Int in ints) {
            if (value > 1000) {
                value /= 1024
                if (index != 2) continue
            }
            text = when (index) {
                0 -> value.applyPrecision() + deliminator + "KB"
                1 -> value.applyPrecision() + deliminator + "MB"
                2 -> value.applyPrecision() + deliminator + "GB"
                else -> "0"
            }
            break
        }
    } else {
        text = initValue.applyPrecision() + deliminator + "B"
    }
    return text
}

fun Double.applyPrecision(): String {
    val decimal = this.decimalNumbers()
    return if (decimal == "0") {
        this.toInt().toString()
    } else {
        String.format("%.2f", this)
    }
}

fun Double.decimalNumbers(decimalPoints: Int = 2) =
    if (this % 1 == 0.0) "0"
    else decimalPoints.toString()

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }

    return name
}