package pe.edu.upc.lib

object NewsModel{
    data class Response(
            val message: String,
            val status: String,
            val data: ArrayList<News>
    )
    data class News (
            val id: Int?,
            val image: String?,
            val content: String?
    )
}

