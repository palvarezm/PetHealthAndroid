package pe.edu.upc.lib

import java.io.Serializable

data class User(
        val id: Int,
        val username: String,
        val password: String,
        val mail: String,
        val photo: String,
        val bio: String
) : Serializable