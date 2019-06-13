package pe.edu.upc.lib

data class Veterinary(
        val id: Int,
        val name: String,
        val preVideo: String,
        val phoneNumber: String,
        val longitude: Double,
        val latitude: Double,
        val openingHours: String,
        val rating: Float
)