package pe.edu.upc.clinic.network

import java.io.Serializable

class RestView<T> : Serializable {
    var status: String? = null
    var message: String? = null
    var data: T? = null
}