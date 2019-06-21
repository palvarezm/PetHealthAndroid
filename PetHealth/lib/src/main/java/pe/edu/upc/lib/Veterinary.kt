package pe.edu.upc.lib
import java.io.Serializable

data class Veterinary(
        val id: Int,
        val social_url_id: Int,
        val name: String,
        val phone: String,
        val location: String,
        val opening_hours: String,
        val longitude: Double,
        val latitude: Double
) : Serializable