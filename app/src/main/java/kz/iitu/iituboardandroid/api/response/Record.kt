package kz.iitu.iituboardandroid.api.response

import com.google.gson.annotations.SerializedName
import kz.iitu.iituboardandroid.toDate
import kz.iitu.iituboardandroid.toPrintableDate
import java.io.Serializable

data class Record(
    @SerializedName("ads_category") val adsCategory: String?,
    val email: String?,
    val id: Int,
    val image1: String?,
    val image2: String?,
    val image3: String?,
    val phone: String?,
    val record_body: String,
    val record_title: String,
    val record_type: String,
    val telegram: String?,
    val whatsapp: String?,
    val author: String?,
    @SerializedName("author_email") val authorEmail: String?,
    @SerializedName("created_at") val creationDate: String?
) : Serializable {
    fun getImage() =
        when {
            image1 != null && image1.isNotEmpty() -> image1
            image2 != null && image2.isNotEmpty() -> image2
            image3 != null && image3.isNotEmpty() -> image3
            else -> null
        }

    fun getPrintableCreationDate() = creationDate?.toPrintableDate()

    fun getFormattedCreationDate() = creationDate?.toDate()
}