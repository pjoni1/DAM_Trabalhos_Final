package dam.a51421.nutriflow.data.model

data class MediaEntry(
    val id: String,
    val filePath: String,
    val storageUrl: String? = null,
    val category: String, // "Evolution" or "Food"
    val date: Long = System.currentTimeMillis()
)
