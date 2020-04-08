package kz.iitu.iituboardandroid.api.response

data class Record(
    val ads_category: String?,
    val email: String?,
    val id: Int,
    val image1: String?,
    val image2: String?,
    val image3: String?,
    val instagram: String?,
    val phone: String?,
    val record_body: String,
    val record_title: String,
    val record_type: String,
    val telegram: String?,
    val vk: String?,
    val whatsapp: String?
)