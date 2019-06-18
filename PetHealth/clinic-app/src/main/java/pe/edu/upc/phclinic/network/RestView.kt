package pe.edu.upc.phclinic.network

import java.io.Serializable

class RestView<T> : Serializable {
    var status: String? = null
    var message: String? = null
    var data: T? = null
}