package pe.upc.lib

data class Appointment(
        val id: String = "",
        val appt_date: String = "",
        val desc: String = "",
        val status: String = "",
        val start_t: String = "",
        val end_t: String = "",
        val register_date: String = "",
        val pet_photo: String = "",
        val pet_id: String = "",
        val vet_id: String = ""
)