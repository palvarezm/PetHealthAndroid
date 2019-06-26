package pe.edu.upc.lib.models

object ApptModel{
    data class Response(
            val message: String,
            val status: String,
            val data: ArrayList<AppointmentResponse>
    )
    data class AppointmentResponse(
            val appt: Appt,
            val pet: Pet,
            val veterinarian: Veterinarian,
            val veterinary: VeterinaryModel.Veterinary
    )
    data class Appt(
            val id: String = "",
            val appt_date: String = "",
            val desc: String = "",
            val status: String = "",
            val start_t: String = "",
            val end_t: String = "",
            val register_date: String = "",
            val pet_photo: String = "",
            val pet_id: String = "",
            val vet_id: String = "",
            val type: String = ""
    )
}
