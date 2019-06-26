package pe.edu.upc.lib.models

object VeterinaryModel{
    data class Response(
            val message: String,
            val status: String,
            val data: ArrayList<VeterinaryDistance>
    )
    data class Veterinary(
            val id: Int?,
            val social_url_id: Int,
            val name: String,
            val phone: String,
            val location: String,
            val opening_hours: String,
            val longitude: Double,
            val latitude: Double,
            val logo: String?
    )
    data class VeterinaryDistance(
            val veterinary: Veterinary,
            val distance: Double
    )
}
