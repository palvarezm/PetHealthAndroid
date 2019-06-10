package pe.edu.upc.lib

data class ClinicHistory(
        val history_id: Int,
        val motive: String,
        val diagnosis: String,
        val appointment_id: Int,
        val pet_id: Int
)