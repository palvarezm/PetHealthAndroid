package pe.edu.upc.lib.models

data class ClinicHistory(
        val id: Int,
        val motive: String,
        val diagnosis: String,
        val appointment_id: Int,
        val pet_id: Int
)