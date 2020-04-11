package kz.iitu.iituboardandroid.api.response

data class RecordsResponse(
    val message: String?,
    val records: List<Record>?
)

data class RecordResponse(
    val message: String?,
    val record: Record?
)