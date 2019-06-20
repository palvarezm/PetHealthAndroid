package pe.edu.upc.lib

data class PHResponse<T>(
        var status: String = "",
        var message: String = "",
        var data: T?
)