package pe.edu.upc.lib

data class Veterinary(
        val id: Int,
        val social_url_id: Int,
        val name: String,
        val phone: String,
        val location: String,
        val opening_hours: String,
        val longitude: Double,
        val latitude: Double,
        var distance: Double
)